package Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.databinding.FragmentMyMenuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;

import Activity.PopupDetailShowNuActivity;
import Model.Food;
import Request.GetEatFoodRequest;

public class MyMenuFragment extends Fragment {
    private FragmentMyMenuBinding fragmentMyMenuBinding;
    private double sum_kcal = 0, sum_carbs = 0, sum_protein = 0, sum_fat = 0, sum_sugars = 0, sum_sodium = 0, sum_CH = 0, sum_Sat_fat = 0, sum_trans_fat = 0; //아래 영양분 표에 표시될 영양분 정보를 담는 변수
    private int year = 0, month = 0, day = 0; //현재 표시 되는 년도, 월, 일
    private int cur_year = 0, cur_month = 0, cur_day = 0; //오늘 날짜

    private int first_day = 0, last_day = 0; //현재 표시 되는 년도, 월의 첫번째 날(정확힌 첫번째 날의 요일(일 = 0, 월 = 1 ... 토 = 6), 마지막 날

    private Calendar calendar = Calendar.getInstance(); //달력 제작에 사용되는 Calendar 객체

    private TextView [] textViews = new TextView[43]; //달력의 1 ~ 42번 칸을 나타내는 TextView
    public MyMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMyMenuBinding = FragmentMyMenuBinding.inflate(inflater, container, false);
        View view = fragmentMyMenuBinding.getRoot();

        for(int i = 1; i <= 42; i++) { //42개의 칸을 초기화
            textViews[i] = new TextView(getContext());
        }

        for(int i = 0; i <= 5; i++) { //6개의 열, 각 열의 날짜 칸들을 달력 화면에 추가.
            TableRow tableRow = new TableRow(getContext());
            for (int j = 1; j <= 7; j++) {
                TextView textView = textViews[i * 7 + j];
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        set_display_Nu(Integer.parseInt(textView.getText().toString())); //선택한 날의 정보를 표시

                        if(day != 0) { //이전에 클릭하여 영양분 정보를 표시 중인 날이 있었다면
                            return_display_click_date_to_default();
                        } //해당 날짜의 표시를 없애기

                        textView.setTextColor(Color.parseColor("#008000")); //선택한 날을 가시적으로 표현하기 위해 초록색으로 설정
                        textView.setClickable(false);
                        day = Integer.parseInt(textView.getText().toString()); //선택한 날을 저장
                    }
                });

                tableRow.addView(textView);
            }
            fragmentMyMenuBinding.calendarView.addView(tableRow);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String eat_date = format.format(System.currentTimeMillis());

        cur_year = Integer.parseInt(eat_date.substring(0, 4)); cur_month = Integer.parseInt(eat_date.substring(5, 7)); cur_day = Integer.parseInt(eat_date.substring(8, 10));
        year = cur_year; month = cur_month;

        setting_Calendar(); //달력 설정, 각 날짜의 영양분 정보 가져오기.. 등 달력 제작, 달의 영양분 정보 저장 역할

        fragmentMyMenuBinding.preMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentMyMenuBinding.preMonthBtn.setClickable(false); //연속적으로 달력의 달을 바꾸는 것을 막기 위해 버튼 비활성화
                fragmentMyMenuBinding.nextMonthBtn.setClickable(false);

                ///////////// 이전 달의 정보를 남김 없이 삭제
                clear_display();
                ///////////// 이전 달의 정보를 남김 없이 삭제

                month -= 1;
                if(month == 0){ //달 설정
                    year -= 1;
                    month = 12;
                }

                setting_Calendar(); //캘린더에 표시되는 년도와 달을 변경한 년도와 달로 수정

                //처음 표시되는 날짜 설정
                if(year == cur_year && month == cur_month){ //이번 달이면 오늘 정보를 표시
                    textViews[cur_day + first_day].performClick();
                }
                else if(year < cur_year || (year == cur_year && month < cur_month)) textViews[1 + first_day].performClick(); //이전 달인 경우 1일의 정보 표시
                //미래(이후)의 달에 대해서는 처음 표시되는 날짜X

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentMyMenuBinding.preMonthBtn.setClickable(true); //달력 작업이 끝난 후 버튼 활성화
                        fragmentMyMenuBinding.nextMonthBtn.setClickable(true);
                    }
                }, 500); //모든 작업이 끝난 후 0.5초 뒤에 활성화 -> 연속적으로 클릭하는 경우를 방지
            }
        });

        fragmentMyMenuBinding.nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentMyMenuBinding.preMonthBtn.setClickable(false); //연속적으로 달력의 달을 바꾸는 것을 막기 위해 버튼 비활성화
                fragmentMyMenuBinding.nextMonthBtn.setClickable(false);

                ///////////// 이전 달의 정보를 남김 없이 삭제
                clear_display();
                ///////////// 이전 달의 정보를 남김 없이 삭제

                month += 1;
                if(month == 13){ //달 설정
                    year += 1;
                    month = 1;
                }

                setting_Calendar(); //캘린더에 표시되는 년도와 달을 변경한 년도와 달로 수정

                if(year == cur_year && month == cur_month){ //이번 달이면 오늘 정보를 표시
                    textViews[cur_day + first_day].performClick();
                }
                else if(year < cur_year || (year == cur_year && month < cur_month)) textViews[1 + first_day].performClick(); //이전 달인 경우 1일의 정보 표시
                //미래(이후)의 달에 대해서는 처음 표시되는 날짜X

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentMyMenuBinding.preMonthBtn.setClickable(true); //달력 작업이 끝난 후 버튼 활성화
                        fragmentMyMenuBinding.nextMonthBtn.setClickable(true);
                    }
                }, 500); //모든 작업이 끝난 후 0.5초 뒤에 활성화
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            set_display_Nu(day); //자세히 보기 화면에서 변경된 음식 정보와 관련하여 바뀐 영양분 정보를 다시 표시
                            check_Nu(year + "-" + month + "-" + day, textViews[first_day + day]); //변경된 영양분정보가 적합한지 확인
                        }
                    }
                });

        fragmentMyMenuBinding.showDetailNuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //그 날의 먹은 음식을 보여주는 팝업 창 띄우기
                if(day != 0) {
                    String eat_date = year + "-" + month + "-" + day;
                    Intent intent = new Intent(getContext(), PopupDetailShowNuActivity.class);
                    intent.putExtra("eat_date", eat_date);

                    activityResultLauncher.launch(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMyMenuBinding = null; //프래그먼트는 뷰보다 오래 지속되므로 결합 클래스 인스턴스 참조를 정리
    }

    ////////// 영양분 정보를 표시하는 함수들
    public void set_display_Nu(int display_day){ //선택한 날의 영양분 정보를 데이터베이스로부터 가져와 아래 레이아웃에 표시.
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if(success == 0) {
                        sum_kcal = 0; sum_carbs = 0; sum_protein = 0; sum_fat = 0; sum_sugars = 0; sum_sodium = 0; sum_CH = 0; sum_Sat_fat = 0; sum_trans_fat = 0;
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int serving = jsonObject.getInt("serving");
                            sum_kcal += jsonObject.getDouble("food_kcal") * serving;
                            sum_carbs += jsonObject.getDouble("food_carbs") * serving;
                            sum_protein += jsonObject.getDouble("food_protein") * serving;
                            sum_fat += jsonObject.getDouble("food_fat") * serving;
                            sum_sugars += jsonObject.getDouble("food_sugars") * serving;
                            sum_sodium += jsonObject.getDouble("food_sodium") * serving;
                            sum_CH += jsonObject.getDouble("food_CH") * serving;
                            sum_Sat_fat += jsonObject.getDouble("food_Sat_fat") * serving;
                            sum_trans_fat += jsonObject.getDouble("food_trans_fat") * serving;
                        }
                        set_My_Nu_Val(); //위에 값을 저장한 변수(sum_***)의 값을 통해 아래에 있는 영양분 표에 값을 표싯
                    }
                    else if(success == 1){
                        Toast.makeText(getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                    }
                    else if(success == 2){
                        Toast.makeText(getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        GetEatFoodRequest getEatfoodRequest = new GetEatFoodRequest(sharedPreferences.getString("ID", null), year + "-" + month + "-" + display_day, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getEatfoodRequest);
    }

    public void set_My_Nu_Val(){ //레이아웃 밑에 있는 영양분 표시를 수정하는 함수.
        fragmentMyMenuBinding.monthMyKcalVal.setText(String.format("%.2f(kcal)", sum_kcal));
        fragmentMyMenuBinding.monthMyCarbsVal.setText(String.format("%.2f(g)", sum_carbs));
        fragmentMyMenuBinding.monthMyProteinVal.setText(String.format("%.2f(g)", sum_protein));
        fragmentMyMenuBinding.monthMyFatVal.setText(String.format("%.2f(g)", sum_fat));
        fragmentMyMenuBinding.monthMySugarsVal.setText(String.format("%.2f(g)", sum_sugars));
        fragmentMyMenuBinding.monthMySodiumVal.setText(String.format("%.2f(mg)", sum_sodium));
        fragmentMyMenuBinding.monthMyCHVal.setText(String.format("%.2f(mg)", sum_CH));
        fragmentMyMenuBinding.monthMySatFatVal.setText(String.format("%.2f(g)", sum_Sat_fat));
        fragmentMyMenuBinding.monthMyTransFatVal.setText(String.format("%.2f(g)", sum_trans_fat));
    }
    ////////// 영양분 정보를 표시하는 함수들

    ////////// 각 날짜의 영양분 정보를 가져오는 함수
    public void check_Nu(String eat_date, TextView textView){ //표시되는 달의 각 날짜의 영양분 정보를 데이터 베이스에서 가져와서, 적합한 영양분 섭취했는지 확인
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if(success == 0) {
                        int serving;
                        double food_kcal = 0, food_carbs = 0, food_protein = 0, food_fat = 0, food_sugars = 0, food_sodium = 0, food_CH = 0, food_Sat_fat = 0, food_trans_fat = 0;
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            serving = jsonObject.getInt("serving");
                            food_kcal += jsonObject.getDouble("food_kcal") * serving;
                            food_carbs += jsonObject.getDouble("food_carbs") * serving;
                            food_protein += jsonObject.getDouble("food_protein") * serving;
                            food_fat += jsonObject.getDouble("food_fat") * serving;
                            food_sugars += jsonObject.getDouble("food_sugars") * serving;
                            food_sodium += jsonObject.getDouble("food_sodium") * serving;
                            food_CH += jsonObject.getDouble("food_CH") * serving;
                            food_Sat_fat += jsonObject.getDouble("food_Sat_fat") * serving;
                            food_trans_fat += jsonObject.getDouble("food_trans_fat") * serving;
                        }
                        if(food_kcal > 3000 || food_carbs > 2000 || food_protein > 2000 || food_fat > 2000 || food_sugars > 500 || food_sodium > 500 || food_CH > 500 || food_Sat_fat > 100 || food_trans_fat > 100){
                            textView.setBackgroundColor(Color.parseColor("#808080")); //적절하지 않은 영양분 섭취 시 해당 요일의 백그라운드 색을 회색으로 변경
                            int bad = Integer.parseInt(fragmentMyMenuBinding.badDay.getText().toString()) + 1;
                            fragmentMyMenuBinding.badDay.setText(String.valueOf(bad));
                        }
                        else {
                            textView.setBackgroundColor(Color.parseColor("#00ff0000")); //배경색이 존재(이전 달에 부적절한 영양분 섭취 날로 계산되어)된 것을 지움
                            int good = Integer.parseInt(fragmentMyMenuBinding.goodDay.getText().toString()) + 1;
                            fragmentMyMenuBinding.goodDay.setText(String.valueOf(good));
                        }
                    }
                    else if(success == 1){
                        Toast.makeText(getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                    }
                    else if(success == 2){
                        Toast.makeText(getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        GetEatFoodRequest getEatfoodRequest = new GetEatFoodRequest(sharedPreferences.getString("ID", null), eat_date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getEatfoodRequest);
    }
    ////////// 각 날짜의 영양분 정보를 가져오는 함수

    ////////// 달력 제작하는 함수
    public void setting_Calendar(){ //설정한 년도와 달에 따라 달력을 만들고, 날짜 클릭이벤트를 설정
        fragmentMyMenuBinding.calendarDate.setText(year + "." + month);

        int textView_day = 0; //last_day = 캘린더에 표시된 달의 마지막 날, first_day = 캘린더에 표시된 달의 첫번째 날, day = 각 칸이 나타내는 날짜

        calendar.set(year, month - 1, 1); //첫번째 날 정보를 구하기 위해 Calendar 객체 정보를 해당 달의 1일로 설정
        first_day = calendar.get(Calendar.DAY_OF_WEEK) - 1; //일요일 = 0, 월 = 1, 화 = 2 ... 토 = 6
        last_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); //마지막 날 구하기

        for(int i = 1; i <= 6; i++){
            for(int j = 1; j <= 7; j++){
                textView_day += 1;
                TextView textView = textViews[textView_day];
                textView.setBackgroundColor(Color.parseColor("#00ff0000")); //배경색이 존재(이전 달에 부적절한 영양분 섭취 날로 계산되어)된 것을 지움
                if(textView_day > first_day && (textView_day - first_day) <= last_day) { //달의 시작 날짜 ~ 마지막 날짜 사이의 textView 설정
                    textView.setText(String.valueOf(textView_day - first_day)); //첫 주 일때 시작 위치에 따라 설정, 각 달의 마지막 날 뒤로는 비활성화
                    textView.setTextSize(35);
                    textView.setGravity(Gravity.CENTER);
                    if (j == 1) {
                        textView.setTextColor(Color.parseColor("#ff0000")); //일요일은 빨간색
                    } else if (j == 7) {
                        textView.setTextColor(Color.parseColor("#0067a3")); //토요일은 파란색
                    }

                    String eat_date = year + "-" + month + "-" + (textView_day - first_day);
                    if(year < cur_year || (year == cur_year && month < cur_month) || (year == cur_year && month == cur_month && (textView_day - first_day) <= cur_day)) {
                        check_Nu(eat_date, textView); //해당 날의 영양분 정보를 확인하고 적합한지의 여부에 따라 배경 변경 및 레이아웃 아래의 정보 수정
                        textView.setClickable(true);
                    } //클릭할 수 있는 경우는 달력에 표시되는 날이자, 오늘을 포함해 이전 날인 경우
                    else {
                        textView.setClickable(false); //미래의 날짜는 클릭X
                    }
                }
                else { //달력에 표시 안되는 칸일 경우
                    textView.setText(""); //날짜 지우기
                    textView.setClickable(false); //클릭 비활성화
                }
            }
        }
    }
    ////////// 달력 제작하는 함수

    ////////// 표시되는 정보 제거하는 함수들
    public void clear_display(){ //현재 표시되는 정보 제거
        if(day != 0) {
            return_display_click_date_to_default(); //이전에 클릭 한 날짜 표시를 제거
            day = 0;
        }

        sum_kcal = 0;
        sum_carbs = 0;
        sum_protein = 0;
        sum_fat = 0;
        sum_sugars = 0;
        sum_sodium = 0;
        sum_CH = 0;
        sum_Sat_fat = 0;
        sum_trans_fat = 0;
        set_My_Nu_Val(); //영양분 정보 모두 초기화

        fragmentMyMenuBinding.goodDay.setText("0"); //달의 적합한&적합하지 않은 영양분 섭취 날 정보를 0으로 수정
        fragmentMyMenuBinding.badDay.setText("0");
    }

    public void return_display_click_date_to_default(){ //이전에 선택한 날짜의 표시(초록색)을 제거하는 함수
        TextView textView = textViews[day + first_day];

        calendar.set(year, month - 1, day);
        if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
            textView.setTextColor(Color.parseColor("#ff0000")); //일요일이면 빨간색으로 되돌림
        }
        else if(calendar.get(Calendar.DAY_OF_WEEK) == 7){
            textView.setTextColor(Color.parseColor("#0067a3")); //토요일이면 파란색으로 되돌림
        }
        else {
            textView.setTextColor(Color.parseColor("#ffffff")); //평일이면 하얀색으로 되돌림
        }

        textView.setClickable(true);
    }
    ////////// 표시되는 정보 제거하는 함수들

}