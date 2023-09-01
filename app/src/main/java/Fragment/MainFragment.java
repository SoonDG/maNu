package Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.databinding.FragmentMainBinding;

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
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int selected_index;

    //오늘 유저가 먹은 음식의 영양분의 합을 저장할 변수들
    private double sum_kcal = 0, sum_carbs = 0, sum_protein = 0, sum_fat = 0, sum_sugars = 0, sum_sodium = 0, sum_CH = 0, sum_Sat_fat = 0, sum_trans_fat = 0;

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

        set_Food_list(); //오늘 먹은 음식 데이터를 데이터베이스로 부터 가져와 리사이클러 뷰에 표시

        activityResultLauncher = registerForActivityResult( //RecyclerView의 아이템 클릭시 발생하는 클릭 이벤트 작성 부분
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == 1){ //먹은 음식 수정
                            Food food = arrayList.get(selected_index);
                            int serving = result.getData().getIntExtra("serving", -1); //변경된 인분 정보
                            int pre_serving = food.getServing(); //변경되기 전의 인분 정보

                            if(serving == -1){ //오류 발생, 예외 처리 필요

                            }
                            else {
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
                        }
                        else if(result.getResultCode() == 2){ //먹은 음식 삭제
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
        activityResultLauncher.launch(intent);
    }

    public void set_Food_list(){ //해당 부분 수정 필요 -> 먹은 음식 테이블에서 가져오도록
        sum_kcal = 0; sum_carbs = 0; sum_protein = 0; sum_fat = 0; sum_sugars = 0; sum_sodium = 0; sum_CH = 0; sum_Sat_fat = 0; sum_trans_fat = 0;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if(success == 0) {
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
        if(sum_kcal < 0) sum_kcal = 0;
        if(sum_carbs < 0) sum_carbs = 0;
        if(sum_protein < 0) sum_protein = 0;
        if(sum_fat < 0) sum_fat = 0;
        if(sum_sugars < 0) sum_sugars = 0;
        if(sum_sodium < 0) sum_sodium = 0;
        if(sum_CH < 0) sum_CH = 0;
        if(sum_Sat_fat < 0) sum_Sat_fat = 0;
        if(sum_trans_fat < 0) sum_trans_fat = 0;
        set_My_Nu_Val(); //삭제된 음식의 영양분 정보를 표에 반영
    } //먹은 음식을 클릭하여 삭제했을 때 실행되는 메소드.

    public void EatFoodAdd(double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat){
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

    public void set_My_Nu_Val(){ //영양분 정보를 표에 표시
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

    public void check_Nu(){ //영양분을 적절히 섭취 했는지 확인
        if(sum_kcal > 3000){
            fragmentMainBinding.myKcalVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.myKcalVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_carbs > 2000){
            fragmentMainBinding.myCarbsVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.myCarbsVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_protein > 2000){
            fragmentMainBinding.myProteinVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.myProteinVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_fat > 2000){
            fragmentMainBinding.myFatVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.myFatVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_sugars > 500){
            fragmentMainBinding.mySugarsVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.mySugarsVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_sodium > 500){
            fragmentMainBinding.mySodiumVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.mySodiumVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_CH > 500){
            fragmentMainBinding.myCHVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.myCHVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_Sat_fat > 100){
            fragmentMainBinding.mySatFatVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.mySatFatVal.setTextColor(Color.parseColor("#ffffff"));
        }

        if(sum_trans_fat > 100){
            fragmentMainBinding.myTransFatVal.setTextColor(Color.parseColor("#808080"));
        }
        else {
            fragmentMainBinding.myTransFatVal.setTextColor(Color.parseColor("#ffffff"));
        }
    }
}