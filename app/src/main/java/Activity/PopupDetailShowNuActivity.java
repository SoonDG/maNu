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

        popupDetailShowNuBinding.canclePopupDetailShowNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        set_Food_list();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
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

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        GetEatFoodRequest getEatfoodRequest = new GetEatFoodRequest(sharedPreferences.getString("ID", null), eat_date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getEatfoodRequest);
    }

    public String getEat_date(){
        return eat_date;
    }

}