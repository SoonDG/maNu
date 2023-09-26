package Activity.PopupActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupDetailShowNuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.EatFoodAdapter;
import Interface.ListItemClickInterface;
import Model.Food;
import Request.GetEatFoodRequest;

public class PopupDetailShowNuActivity extends AppCompatActivity implements ListItemClickInterface {
    private ActivityPopupDetailShowNuBinding popupDetailShowNuBinding;
    private EatFoodAdapter eatFoodAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Food> arrayList;
    private ActivityResultLauncher<Intent> eatFoodEditResultLauncher;
    private String eat_date;
    private int selected_index; //선택된 RecyclerView의 itemView의 index 정보 -> 이를 통해 클릭한 itemView의 위치를 알 수 있음
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupDetailShowNuBinding = ActivityPopupDetailShowNuBinding.inflate(getLayoutInflater());
        View view = popupDetailShowNuBinding.getRoot();
        setContentView(view);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                popupDetailShowNuBinding.popupDetailShowNuTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
                popupDetailShowNuBinding.closeDetailShowNu.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
                popupDetailShowNuBinding.closeDetailShowNu.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));

                break;
        }

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels);
        getWindow().getAttributes().width = width;
        //크기조절

        Intent intent = getIntent();
        eat_date = intent.getStringExtra("eat_date");

        popupDetailShowNuBinding.popupDetailShowNuRecyclerView.setHasFixedSize(true); //리사이클러 뷰 설정
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        popupDetailShowNuBinding.popupDetailShowNuRecyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>(); //데이터 베이스에서 먹은 음식 테이블로 부터 유저id, 날짜를 통해 오늘 먹은 음식을 가져와 담음
        eatFoodAdapter = new EatFoodAdapter(arrayList, this);
        popupDetailShowNuBinding.popupDetailShowNuRecyclerView.setAdapter(eatFoodAdapter);

        popupDetailShowNuBinding.closeDetailShowNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        eatFoodEditResultLauncher = registerForActivityResult( //PopupActivity의 결과값을 받으면 발생하는 이벤트 작성 부분
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == 1){ //먹은 음식 수정
                            Food food = arrayList.get(selected_index);
                            int serving = result.getData().getIntExtra("serving", -1); //변경된 인분 정보
                            int pre_serving = food.getServing(); //변경되기 전의 인분 정보

                            if(serving == -1){ //오류 발생, 예외 처리 필요
                                Toast.makeText(getApplicationContext(), "데이터 전송 오류 발생", Toast.LENGTH_SHORT).show();
                            }
                            else {
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

                                arrayList.set(selected_index, food);
                                eatFoodAdapter.notifyItemChanged(selected_index);
                            }
                        }
                        else if(result.getResultCode() == 2){ //먹은 음식 삭제
                            Food food = arrayList.get(selected_index); //삭제된 음식 정보를 가져와서
                            //아래의 영야분 표에서 삭제된 음식의 영양분 정보를 제거
                            arrayList.remove(selected_index); //arrayList에서 삭제한 음식정보 제거
                            eatFoodAdapter.notifyItemRemoved(selected_index); //RecyclerView에 삭제된 음식 정보 반영
                        }
                    }
                });

        set_Food_list();
    }

    @Override
    public void onItemClick(View v, int position) { //RecyclerView의 ItemView 클릭 이벤트
        selected_index = position;
        Intent intent = new Intent(this, PopupEatFoodEditActivity.class);
        intent.putExtra("food_code", arrayList.get(position).getFood_code());
        intent.putExtra("eat_date", getEat_date());
        intent.putExtra("food_name", arrayList.get(position).getFood_name());
        intent.putExtra("serving", arrayList.get(position).getServing());
        eatFoodEditResultLauncher.launch(intent);
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