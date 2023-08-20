package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.example.my_first_project.databinding.ActivityPopupDetailShowNuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.EatFoodAdapter;
import Model.Food;
import Request.GetEatFoodRequest;

public class PopupDetailShowNuActivity extends AppCompatActivity {
    private ActivityPopupDetailShowNuBinding popupDetailShowNuBinding;
    private EatFoodAdapter eatFoodAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Food> arrayList;
    private String eat_date;
    private Food food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupDetailShowNuBinding = ActivityPopupDetailShowNuBinding.inflate(getLayoutInflater());
        View view = popupDetailShowNuBinding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        eat_date = intent.getStringExtra("eat_date");

        popupDetailShowNuBinding.popupDetailShowNuRecyclerView.setHasFixedSize(true); //리사이클러 뷰 설정
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        popupDetailShowNuBinding.popupDetailShowNuRecyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>(); //데이터 베이스에서 먹은 음식 테이블로 부터 유저id, 날짜를 통해 오늘 먹은 음식을 가져와 담음
        eatFoodAdapter = new EatFoodAdapter(arrayList, this);
        popupDetailShowNuBinding.popupDetailShowNuRecyclerView.setAdapter(eatFoodAdapter);

        popupDetailShowNuBinding.canclePopipDetailShwoNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        set_Food_list();
    }

    public void set_Food_list(){ //해당 부분 수정 필요 -> 먹은 음식 테이블에서 가져오도록
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
                        }
                        eatFoodAdapter.notifyDataSetChanged(); //리스트의 변경 내용을 리스트 뷰에 반영
                    }
                    else if(success == 1){
                        Toast.makeText(getApplicationContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                    }
                    else if(success == 2){
                        Toast.makeText(getApplicationContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        GetEatFoodRequest getEatfoodRequest = new GetEatFoodRequest(sharedPreferences.getString("ID", null), eat_date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getEatfoodRequest);
    }

    public void EatFoodDelete(double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat) {
        food.setFood_kcal(food.getFood_kcal() - food_kcal);
        food.setFood_carbs(food.getFood_carbs() - food_carbs);
        food.setFood_protein(food.getFood_protein() - food_protein);
        food.setFood_fat(food.getFood_fat() - food_fat);
        food.setFood_sugars(food.getFood_sugars() - food_sugars);
        food.setFood_sodium(food.getFood_sodium() - food_sodium);
        food.setFood_CH(food.getFood_CH() - food_CH);
        food.setFood_Sat_fat(food.getFood_Sat_fat() - food_Sat_fat);
        food.setFood_trans_fat(food.getFood_trans_fat() - food_trans_fat);
        if(food.getFood_kcal() < 0) food.setFood_kcal(0);
        if(food.getFood_carbs() < 0) food.setFood_carbs(0);
        if(food.getFood_protein() < 0) food.setFood_protein(0);
        if(food.getFood_fat() < 0) food.setFood_fat(0);
        if(food.getFood_sugars() < 0) food.setFood_sugars(0);
        if(food.getFood_sodium() < 0) food.setFood_sodium(0);
        if(food.getFood_CH() < 0) food.setFood_CH(0);
        if(food.getFood_Sat_fat() < 0) food.setFood_Sat_fat(0);
        if(food.getFood_trans_fat() < 0) food.setFood_trans_fat(0);

    } //먹은 음식을 클릭하여 삭제했을 때 실행되는 메소드.

    public void EatFoodAdd(double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat){
        food.setFood_kcal(food.getFood_kcal() + food_kcal);
        food.setFood_carbs(food.getFood_carbs() + food_carbs);
        food.setFood_protein(food.getFood_protein() + food_protein);
        food.setFood_fat(food.getFood_fat() + food_fat);
        food.setFood_sugars(food.getFood_sugars() + food_sugars);
        food.setFood_sodium(food.getFood_sodium() + food_sodium);
        food.setFood_CH(food.getFood_CH() + food_CH);
        food.setFood_Sat_fat(food.getFood_Sat_fat() + food_Sat_fat);
        food.setFood_trans_fat(food.getFood_trans_fat() + food_trans_fat);
    } //병경된 인분 값과 이전 인분 값을 통해 변경된 값 만큼 반영

    public String getEat_date(){
        return eat_date;
    }

}