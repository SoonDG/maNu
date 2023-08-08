package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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
import java.util.Calendar;
import java.util.Date;

import Model.Food;
import Request.EatFoodRequest;

public class MyMenuFragment extends Fragment {
    private FragmentMyMenuBinding fragmentMyMenuBinding;
    private double sum_kcal = 0, sum_carbs = 0, sum_protein = 0, sum_fat = 0, sum_sugars = 0, sum_sodium = 0, sum_CH = 0, sum_Sat_fat = 0, sum_trans_fat = 0;
    private int year = 0, month = 0, day = 0; //달력에 표시되는 날짜
    private int cur_year = 0, cur_month = 0, cur_day = 0; //오늘 날짜

    private TextView [] textViews = new TextView[43]; //1 ~ 42번 칸
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

        for(int i = 1; i <= 6; i++) { //6개의 열, 각 열의 날짜 칸들을 달력 화면에 추가.
            TableRow tableRow = new TableRow(getContext());
            for (int j = 1; j <= 7; j++) {
                TextView textView = textViews[(i - 1) * 7 + j];
                tableRow.addView(textView);
            }
            fragmentMyMenuBinding.Calendar.addView(tableRow);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String eat_date = format.format(System.currentTimeMillis());
        set_date(eat_date); //처음 시작 시 오늘 먹은 영양분 표시하도록 설정

        cur_year = Integer.parseInt(eat_date.substring(0, 4)); cur_month = Integer.parseInt(eat_date.substring(5, 7)); cur_day = Integer.parseInt(eat_date.substring(8, 10));
        year = cur_year; month = cur_month; day = cur_day;
        setting_Calendar(); //달력 설정

        fragmentMyMenuBinding.preMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month -= 1;
                if(month == 0){ //달 설정
                    year -= 1;
                    month = 12;
                }
                if(year == cur_year && month == cur_month){ //일 설정
                    day = cur_day;
                }
                else day = 1;

                fragmentMyMenuBinding.goodDay.setText("0"); //달의 적합한&적합하지 않은 영양분 섭취 날 정보를 0으로 수정
                fragmentMyMenuBinding.badDay.setText("0");

                setting_Calendar(); //캘린더에 표시되는 년도와 달을 변경한 년도와 달로 수정
                set_date(year + "-" + month + "-" + day); //변경한 달의 1일 또는 오늘의 영양분 정보를 가져옴
            }
        });

        fragmentMyMenuBinding.nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                month += 1;
                if(month == 13){ //달 설정
                    year += 1;
                    month = 1;
                }
                if(year == cur_year && month == cur_month){ //일 설정
                    day = cur_day;
                }
                else day = 1;

                fragmentMyMenuBinding.goodDay.setText("0"); //달의 적합한&적합하지 않은 영양분 섭취 날 정보를 0으로 수정
                fragmentMyMenuBinding.badDay.setText("0");

                setting_Calendar(); //캘린더에 표시되는 년도와 달을 변경한 년도와 달로 수정
                set_date(year + "-" + month + "-" + day); //변경한 달의 1일 또는 오늘의 영양분 정보를 가져옴
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMyMenuBinding = null; //프래그먼트는 뷰보다 오래 지속되므로 결합 클래스 인스턴스 참조를 정리
    }

    public void set_date(String eat_date){ //특정 날의 영양분 정보를 가져오는 함수, 수정 필요...
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if(success == 0) {
                        sum_kcal = 0; sum_carbs = 0; sum_protein = 0; sum_fat = 0; sum_sugars = 0; sum_sodium = 0; sum_CH = 0; sum_Sat_fat = 9; sum_trans_fat = 0;
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int serving = jsonObject.getInt("serving");
                            double food_kcal = jsonObject.getDouble("food_kcal");
                            double food_carbs = jsonObject.getDouble("food_carbs");
                            double food_protein = jsonObject.getDouble("food_protein");
                            double food_fat = jsonObject.getDouble("food_fat");
                            double food_sugars = jsonObject.getDouble("food_sugars");
                            double food_sodium = jsonObject.getDouble("food_sodium");
                            double food_CH = jsonObject.getDouble("food_CH");
                            double food_Sat_fat = jsonObject.getDouble("food_Sat_fat");
                            double food_trans_fat = jsonObject.getDouble("food_trans_fat");
                            sum_kcal += food_kcal * serving;
                            sum_carbs += food_carbs * serving;
                            sum_protein += food_protein * serving;
                            sum_fat += food_fat * serving;
                            sum_sugars += food_sugars * serving;
                            sum_sodium += food_sodium * serving;
                            sum_CH += food_CH * serving;
                            sum_Sat_fat += food_Sat_fat * serving;
                            sum_trans_fat += food_trans_fat * serving;
                        }
                        set_My_Nu_Val(); //영양분 합을 아래 표에 반영
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
        EatFoodRequest eatfoodRequest = new EatFoodRequest(sharedPreferences.getString("ID", null), eat_date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(eatfoodRequest);
    }

    public void check_Nu(String eat_date, TextView textView){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if(success == 0) {
                        sum_kcal = 0; sum_carbs = 0; sum_protein = 0; sum_fat = 0; sum_sugars = 0; sum_sodium = 0; sum_CH = 0; sum_Sat_fat = 9; sum_trans_fat = 0;
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int serving = jsonObject.getInt("serving");
                            double food_kcal = jsonObject.getDouble("food_kcal");
                            double food_carbs = jsonObject.getDouble("food_carbs");
                            double food_protein = jsonObject.getDouble("food_protein");
                            double food_fat = jsonObject.getDouble("food_fat");
                            double food_sugars = jsonObject.getDouble("food_sugars");
                            double food_sodium = jsonObject.getDouble("food_sodium");
                            double food_CH = jsonObject.getDouble("food_CH");
                            double food_Sat_fat = jsonObject.getDouble("food_Sat_fat");
                            double food_trans_fat = jsonObject.getDouble("food_trans_fat");
                            sum_kcal += food_kcal * serving;
                            sum_carbs += food_carbs * serving;
                            sum_protein += food_protein * serving;
                            sum_fat += food_fat * serving;
                            sum_sugars += food_sugars * serving;
                            sum_sodium += food_sodium * serving;
                            sum_CH += food_CH * serving;
                            sum_Sat_fat += food_Sat_fat * serving;
                            sum_trans_fat += food_trans_fat * serving;
                        }
                        if(sum_kcal > 3000 || sum_carbs > 2000 || sum_protein > 2000 || sum_fat > 2000 || sum_sugars > 500 || sum_sodium > 500 || sum_CH > 500 || sum_Sat_fat > 100 || sum_trans_fat > 100){
                            textView.setBackgroundColor(Color.parseColor("#808080")); //적절하지 않은 영양분 섭취 시 해당 요일의 백그라운드 색을 회색으로 변경
                            int bad = Integer.parseInt(fragmentMyMenuBinding.badDay.getText().toString()) + 1;
                            fragmentMyMenuBinding.badDay.setText(String.valueOf(bad));
                        }
                        else {
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
        EatFoodRequest eatfoodRequest = new EatFoodRequest(sharedPreferences.getString("ID", null), eat_date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(eatfoodRequest);
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

    public void setting_Calendar(){ //설정한 년도와 달에 따라 달력을 만들고, 날짜 클릭이벤트를 설정
        fragmentMyMenuBinding.CalendarDate.setText(year + "." + month);

        int last_day = 0, first_day, day = 0;
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){ //말일 설정 -> 변경 필요
            last_day = 31;
        }
        else if(month == 4 || month == 6 || month == 9 || month == 11){
            last_day = 30;
        }
        else {
            last_day = 28;
            if(year % 4 == 0){
                if(year % 100 != 0 || year % 400 == 0){
                    last_day = 29;
                }
            }
        }

        Calendar calendar = Calendar.getInstance(); //달의 첫째 날의 요일 찾기
        calendar.set(year, month - 1, 1);
        first_day = calendar.get(Calendar.DAY_OF_WEEK) - 1; //일요일 = 0, 월 = 1, 화 = 2 ... 토 = 6

        for(int i = 1; i <= 6; i++){
            for(int j = 1; j <= 7; j++){
                day += 1;
                TextView textView = textViews[day];
                textView.setBackgroundColor(Color.parseColor("#00ff0000")); //배경색이 존재(이전 달에 부적절한 영양분 섭취 날로 계산되어)된 것을 지움
                if(day > first_day && (day - first_day) <= last_day) { //달의 시작 날짜 ~ 마지막 날짜 사이의 textView 설정
                    textView.setText(String.valueOf(day - first_day)); //첫 주 일때 시작 위치에 따라 설정, 각 달의 마지막 날 뒤로는 비활성화
                    textView.setTextSize(35);
                    textView.setGravity(Gravity.CENTER);
                    if (j == 1) {
                        textView.setTextColor(Color.parseColor("#ff0000")); //일요일은 빨간색
                    } else if (j == 7) {
                        textView.setTextColor(Color.parseColor("#0067a3")); //토요일은 파란색
                    }

                    String eat_date = year + "-" + month + "-" + (day - first_day);
                    check_Nu(eat_date, textView); //해당 날의 영양분 정보를 확인하고 적합한지의 여부에 따라 배경 변경 및 레이아웃 아래의 정보 수정

                    textView.setOnClickListener(new View.OnClickListener() { //자동으로 setClickable(true);
                        @Override
                        public void onClick(View view) {
                            set_date(year + "-" + month + "-" + textView.getText());
                        }
                    });
                }
                else { //달력에 표시 안되는 칸일 경우
                    textView.setText(""); //날짜 지우기
                    textView.setClickable(false); //클릭 비활성화
                }
            }
        }
    }

}