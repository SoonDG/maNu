package Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.FragmentMainBinding;
import com.tomergoldst.tooltips.ToolTipsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Activity.PopupActivity.PopupEatFoodEditActivity;
import Adapter.EatFoodAdapter;
import Interface.ListItemClickInterface;
import Model.Food;
import Request.GetEatFoodRequest;

public class MainFragment extends Fragment implements ListItemClickInterface {

    private FragmentMainBinding fragmentMainBinding;
    private EatFoodAdapter eatFoodAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Food> arrayList;
    private ActivityResultLauncher<Intent> eatFoodEditResultLauncher;
    ;
    private int selected_index;

    //오늘 유저가 먹은 음식의 영양분의 합을 저장할 변수들
    private double sum_kcal = 0, sum_carbs = 0, sum_protein = 0, sum_fat = 0, sum_sugars = 0, sum_sodium = 0, sum_CH = 0, sum_Sat_fat = 0, sum_trans_fat = 0; //오늘 먹은 영양분 총합
    private double rec_kcal = 0, rec_Max_carbs = 0, rec_Min_carbs = 0, rec_Max_protein = 0, rec_Min_protein = 0, rec_Max_fat = 0, rec_Min_fat = 0, rec_sugars = 0, rec_sodium = 0, rec_CH = 0, rec_Sat_fat = 0, rec_trans_fat = 0; //권장되는 영양분
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        View view = fragmentMainBinding.getRoot();

        fragmentMainBinding.mainRecyclerView.setHasFixedSize(true); //리사이클러 뷰 설정
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentMainBinding.mainRecyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>(); //데이터 베이스에서 먹은 음식 테이블로 부터 유저id, 날짜를 통해 오늘 먹은 음식을 가져와 담음
        eatFoodAdapter = new EatFoodAdapter(arrayList, this);

        fragmentMainBinding.mainRecyclerView.setAdapter(eatFoodAdapter);

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            fragmentMainBinding.myNuTitle.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.MyNuGray));
            fragmentMainBinding.myNuTable.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_tablelayout_style));
        }

        cal_Recommend_Nu(); //권장 영양분 섭취량 계산
        set_Food_list(); //오늘 먹은 음식 데이터를 데이터베이스로 부터 가져와 리사이클러 뷰에 표시

        eatFoodEditResultLauncher = registerForActivityResult( //RecyclerView의 아이템 클릭시 발생하는 클릭 이벤트 작성 부분
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) { //먹은 음식 수정
                            Food food = arrayList.get(selected_index);
                            int serving = result.getData().getIntExtra("serving", -1); //변경된 인분 정보
                            int pre_serving = food.getServing(); //변경되기 전의 인분 정보

                            if (serving == -1) { //오류 발생, 예외 처리 필요

                            } else {
                                EatFoodDelete(food.getFood_kcal(), food.getFood_carbs(), food.getFood_protein(), food.getFood_fat(), food.getFood_sugars(), food.getFood_sodium(), food.getFood_CH(), food.getFood_Sat_fat(), food.getFood_trans_fat());

                                food.setServing(serving);
                                food.setFood_size(food.getFood_size() / pre_serving * serving);
                                food.setFood_kcal(food.getFood_kcal() / pre_serving * serving);
                                food.setFood_carbs(food.getFood_carbs() / pre_serving * serving);
                                food.setFood_protein(food.getFood_protein() / pre_serving * serving);
                                food.setFood_fat(food.getFood_fat() / pre_serving * serving);
                                food.setFood_sugars(food.getFood_sugars() / pre_serving * serving);
                                food.setFood_sodium(food.getFood_sodium() / pre_serving * serving);
                                food.setFood_CH(food.getFood_CH() / pre_serving * serving);
                                food.setFood_Sat_fat(food.getFood_Sat_fat() / pre_serving * serving);
                                food.setFood_trans_fat(food.getFood_trans_fat() / pre_serving * serving);
                                //arrayList의 음식 정보를 수정

                                EatFoodAdd(food.getFood_kcal(), food.getFood_carbs(), food.getFood_protein(), food.getFood_fat(), food.getFood_sugars(), food.getFood_sodium(), food.getFood_CH(), food.getFood_Sat_fat(), food.getFood_trans_fat());

                                arrayList.set(selected_index, food);
                                eatFoodAdapter.notifyItemChanged(selected_index);
                            }
                        } else if (result.getResultCode() == 2) { //먹은 음식 삭제
                            Food food = arrayList.get(selected_index); //삭제된 음식 정보를 가져와서
                            EatFoodDelete(food.getFood_kcal(), food.getFood_carbs(), food.getFood_protein(), food.getFood_fat(), food.getFood_sugars(), food.getFood_sodium(), food.getFood_CH(), food.getFood_Sat_fat(), food.getFood_trans_fat());
                            //아래의 영야분 표에서 삭제된 음식의 영양분 정보를 제거
                            arrayList.remove(selected_index); //arrayList에서 삭제한 음식정보 제거
                            eatFoodAdapter.notifyItemRemoved(selected_index); //RecyclerView에 삭제된 음식 정보 반영
                        }
                    }
                });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMainBinding = null;
    }

    @Override
    public void onItemClick(View v, int position) {
        selected_index = position;
        Intent intent = new Intent(getContext(), PopupEatFoodEditActivity.class);
        intent.putExtra("food_code", arrayList.get(position).getFood_code());
        intent.putExtra("eat_date", "");
        intent.putExtra("food_name", arrayList.get(position).getFood_name());
        intent.putExtra("serving", arrayList.get(position).getServing());
        eatFoodEditResultLauncher.launch(intent);
    }

    public void set_Food_list() {
        sum_kcal = 0;
        sum_carbs = 0;
        sum_protein = 0;
        sum_fat = 0;
        sum_sugars = 0;
        sum_sodium = 0;
        sum_CH = 0;
        sum_Sat_fat = 0;
        sum_trans_fat = 0;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if (success == 0) {
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int serving = jsonObject.getInt("serving");
                            String food_code = jsonObject.getString("food_code");
                            String food_name = jsonObject.getString("food_name");
                            double food_kcal = jsonObject.getDouble("food_kcal") * serving;
                            int food_size = jsonObject.getInt("food_size") * serving;
                            double food_carbs = jsonObject.getDouble("food_carbs") * serving;
                            double food_protein = jsonObject.getDouble("food_protein") * serving;
                            double food_fat = jsonObject.getDouble("food_fat") * serving;
                            double food_sugars = jsonObject.getDouble("food_sugars") * serving;
                            double food_sodium = jsonObject.getDouble("food_sodium") * serving;
                            double food_CH = jsonObject.getDouble("food_CH") * serving;
                            double food_Sat_fat = jsonObject.getDouble("food_Sat_fat") * serving;
                            double food_trans_fat = jsonObject.getDouble("food_trans_fat") * serving;
                            arrayList.add(new Food(serving, food_code, food_name, food_kcal, food_size, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat));
                            sum_kcal += food_kcal;
                            sum_carbs += food_carbs;
                            sum_protein += food_protein;
                            sum_fat += food_fat;
                            sum_sugars += food_sugars;
                            sum_sodium += food_sodium;
                            sum_CH += food_CH;
                            sum_Sat_fat += food_Sat_fat;
                            sum_trans_fat += food_trans_fat;
                        }
                        set_My_Nu_Val(); //영양분 합을 아래 표에 반영
                        eatFoodAdapter.notifyDataSetChanged(); //리스트의 변경 내용을 리스트 뷰에 반영
                    } else if (success == 1) {
                        Toast.makeText(getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                    } else if (success == 2) {
                        Toast.makeText(getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        GetEatFoodRequest getEatfoodRequest = new GetEatFoodRequest(sharedPreferences.getString("ID", null), "", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getEatfoodRequest);
    }

    public void EatFoodDelete(double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat) {
        sum_kcal -= food_kcal;
        sum_carbs -= food_carbs;
        sum_protein -= food_protein;
        sum_fat -= food_fat;
        sum_sugars -= food_sugars;
        sum_sodium -= food_sodium;
        sum_CH -= food_CH;
        sum_Sat_fat -= food_Sat_fat;
        sum_trans_fat -= food_trans_fat;
        if (sum_kcal < 0) sum_kcal = 0;
        if (sum_carbs < 0) sum_carbs = 0;
        if (sum_protein < 0) sum_protein = 0;
        if (sum_fat < 0) sum_fat = 0;
        if (sum_sugars < 0) sum_sugars = 0;
        if (sum_sodium < 0) sum_sodium = 0;
        if (sum_CH < 0) sum_CH = 0;
        if (sum_Sat_fat < 0) sum_Sat_fat = 0;
        if (sum_trans_fat < 0) sum_trans_fat = 0;
        set_My_Nu_Val(); //삭제된 음식의 영양분 정보를 표에 반영
    } //먹은 음식을 클릭하여 삭제했을 때 실행되는 메소드.

    public void EatFoodAdd(double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat) {
        sum_kcal += food_kcal;
        sum_carbs += food_carbs;
        sum_protein += food_protein;
        sum_fat += food_fat;
        sum_sugars += food_sugars;
        sum_sodium += food_sodium;
        sum_CH += food_CH;
        sum_Sat_fat += food_Sat_fat;
        sum_trans_fat += food_trans_fat;
        set_My_Nu_Val(); //삭제된 음식의 영양분 정보를 표에 반영
    } //병경된 인분 값과 이전 인분 값을 통해 변경된 값 만큼 반영

    public void set_My_Nu_Val() { //영양분 정보를 표에 표시
        fragmentMainBinding.myKcalVal.setText(String.format("%.2f(kcal)", sum_kcal));
        fragmentMainBinding.myCarbsVal.setText(String.format("%.2f(g)", sum_carbs));
        fragmentMainBinding.myProteinVal.setText(String.format("%.2f(g)", sum_protein));
        fragmentMainBinding.myFatVal.setText(String.format("%.2f(g)", sum_fat));
        fragmentMainBinding.mySugarsVal.setText(String.format("%.2f(g)", sum_sugars));
        fragmentMainBinding.mySodiumVal.setText(String.format("%.2f(mg)", sum_sodium));
        fragmentMainBinding.myCHVal.setText(String.format("%.2f(mg)", sum_CH));
        fragmentMainBinding.mySatFatVal.setText(String.format("%.2f(g)", sum_Sat_fat));
        fragmentMainBinding.myTransFatVal.setText(String.format("%.2f(g)", sum_trans_fat));
        check_Nu();
    }

    public void check_Nu() { //영양분을 적절히 섭취 했는지 확인
        if(sum_kcal < rec_kcal){
            fragmentMainBinding.myKcalVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.myKcalVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            }
            else {
                fragmentMainBinding.myKcalVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_carbs < rec_Min_carbs || sum_carbs > rec_Max_carbs){
            fragmentMainBinding.myCarbsVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.myCarbsVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            }
            else {
                fragmentMainBinding.myCarbsVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_protein < rec_Min_protein || sum_protein > rec_Max_protein){
            fragmentMainBinding.myProteinVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.myProteinVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            }
            else {
                fragmentMainBinding.myProteinVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_fat < rec_Min_fat || sum_fat > rec_Max_fat){
            fragmentMainBinding.myFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.myFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            }
            else {
                fragmentMainBinding.myFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_sugars > rec_sugars){
            fragmentMainBinding.mySugarsVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.mySugarsVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            }
            else {
                fragmentMainBinding.mySugarsVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_sodium > rec_sodium){
            fragmentMainBinding.mySodiumVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.mySodiumVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            }
            else {
                fragmentMainBinding.mySodiumVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_CH > rec_CH){
            fragmentMainBinding.myCHVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.myCHVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            }
            else {
            fragmentMainBinding.myCHVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_Sat_fat > rec_Sat_fat){
            fragmentMainBinding.mySatFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.mySatFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            } else {
                fragmentMainBinding.mySatFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }

        if(sum_trans_fat > rec_trans_fat){
            fragmentMainBinding.myTransFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuRed));
        }
        else {
            if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면4
                fragmentMainBinding.myTransFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
            } else {
                fragmentMainBinding.myTransFatVal.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuBlack));
            }
        }
    }

    public void cal_Recommend_Nu(){ //권장 영양분 섭취량 계산
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        int Age = sharedPreferences.getInt("Age", 0);
        int Gender = (sharedPreferences.getString("Gender", null).equals("남자")) ? 0 : 1;
        int Activity = sharedPreferences.getInt("Activity", -1);
        double Height = Double.longBitsToDouble(sharedPreferences.getLong("Height", 0));
        double Weight = Double.longBitsToDouble(sharedPreferences.getLong("Weight", 0));

        if(Gender == 0){ //남자의 경우 표준 체중이 키(m)^2 * 22
            rec_kcal = (Height / 100) * (Height / 100) * 22;
        }
        else if(Gender == 1){//여자의 경우 표준 체중이 키(m)^2 * 22
            rec_kcal = (Height / 100) * (Height / 100) * 21;
        }
        if(Activity == 0){ //활동량이 거의 없다면 표준 체중의 * 25 ~ 30정도가 권장 칼로리
            rec_kcal *= 28;
        }
        else if(Activity == 1){ //활동량이 보통이라면 표준 체중의 * 30 ~ 35정도가 권장 칼로리
            rec_kcal *= 33;
        }
        else if(Activity == 2){ //활동량이 많다면 표준 체중의 * 35 ~ 40정도가 권장 칼로리
            rec_kcal *= 38;
        }

        rec_Max_carbs = rec_kcal * 0.65 / 4; //탄수화물의 권장 최대 섭취량은 권장 섭취 칼로리 * 권장 최대 탄수화물 비율(65%) / 4g
        rec_Min_carbs = rec_kcal * 0.55 / 4; //탄수화물의 권장 최소 섭취량은 권장 섭취 칼로리 * 권장 최소 탄수화물 비율(55%) / 4g

        rec_Max_protein = rec_kcal * 0.2 / 4; //단백질의 권장 최대 섭취량은 권장 섭취 칼로리 * 권장 최대 단백질 비율(20%) / 4g
        rec_Min_protein = rec_kcal * 0.07 / 4; //단백질의 권장 최소 섭취량은 권장 섭취 칼로리 * 권장 최소 단백질 비율(7%) / 4g

        if(Age <= 2){ //나이가 1 ~ 2세의 유아일 경우
            rec_Max_fat = rec_kcal * 0.35 / 9; //지방의 권장 최대 섭취량은 권장 섭취 칼로리 * 권장 최대 지방 비율(35%) / 9g
            rec_Min_fat = rec_kcal * 0.2 / 9; //지방의 권장 최소 섭취량은 권장 섭취 칼로리 * 권장 최소 지방 비율(20%) / 9g
        }
        else {
            rec_Max_fat = rec_kcal * 0.3 / 9; //지방의 권장 최대 섭취량은 권장 섭취 칼로리 * 권장 최대 지방 비율(30%) / 9g
            rec_Min_fat = rec_kcal * 0.15 / 9; //지방의 권장 최소 섭취량은 권장 섭취 칼로리 * 권장 최소 지방 비율(15%) / 9g
        }

        rec_sugars = rec_kcal * 0.2 / 4; //당류의 권장 섭취량은 권장 섭취 칼로리 * 권장 당류 비율(20%) / 4g
        rec_sodium = 2300; //나트륨의 하루 권장량은 2300mg 미만
        rec_CH = 300; //콜레스테롤의 하루 권장량은 300mg 미만

        if(Age >= 3 && Age <= 18){ //나이가 3 ~ 18세일 경우
            rec_Sat_fat = rec_kcal * 0.08 / 9; //포화지방의 권장 섭취량은 권장 섭취 칼로리 * 권장 포화 지방 비율(8%) / 9g
        }
        else if(Age >= 19) {
            rec_Sat_fat = rec_kcal * 0.07 / 9; //포화지방의 권장 섭취량은 권장 섭취 칼로리 * 권장 포화 지방 비율(7%) / 9g
        }

        rec_trans_fat = rec_kcal * 0.01 / 9; //트랜스지방의 권장 섭취량은 권장 섭취 칼로리 * 권장 트랜스 지방 비율(1%) / 9g
        set_Tooltips();
    }
    public void set_Tooltips() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fragmentMainBinding.myKcalLable.setTooltipText("권장 칼로리는 " + rec_kcal + "(kcal) 입니다.");
            fragmentMainBinding.myCarbsLable.setTooltipText("탄수화물의 권장 섭취량은 " + rec_Min_carbs + "(g) ~ " + rec_Max_carbs + "(g) 입니다.");
            fragmentMainBinding.myProteinLable.setTooltipText("단백질의 권장 섭취량은 " + rec_Min_protein + "(g) ~ " + rec_Max_protein + "(g) 입니다.");
            fragmentMainBinding.myFatLable.setTooltipText("지방의 권장 섭취량은 " + rec_Min_fat + "(g) ~ " + rec_Max_fat + "(g) 입니다.");
            fragmentMainBinding.mySugarsLable.setTooltipText("당류의 권장 섭취량은 " + rec_sugars + "(g) 입니다.");
            fragmentMainBinding.mySodiumLable.setTooltipText("나트륨의 권장 섭취량은 " + rec_sodium + "(mg) 입니다.");
            fragmentMainBinding.myCHLable.setTooltipText("콜레스테롤의 권장 섭취량은" + rec_CH + "(mg) 입니다.");
            fragmentMainBinding.mySatFatLable.setTooltipText("포화지방의 권장 섭취량은 " + rec_Sat_fat + "(g) 입니다.");
            fragmentMainBinding.myTransFatLable.setTooltipText("트랜스지방의 권장 섭취량은 " + rec_trans_fat + "(g) 입니다.");
        }
    }
}