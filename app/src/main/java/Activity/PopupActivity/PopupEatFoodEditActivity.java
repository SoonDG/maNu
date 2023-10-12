package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupEatFoodEditBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Model.Food;
import Request.DeleteEatFoodRequest;
import Request.EditEatFoodRequest;

public class PopupEatFoodEditActivity extends AppCompatActivity {
    private ActivityPopupEatFoodEditBinding popupEatFoodEditBinding;
    private int serving;
    private double food_kcal, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupEatFoodEditBinding = ActivityPopupEatFoodEditBinding.inflate(getLayoutInflater());
        View view = popupEatFoodEditBinding.getRoot();
        setContentView(view);

        String [] serving_Data = getApplicationContext().getResources().getStringArray(R.array.serving);
        ArrayAdapter servingAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, serving_Data);

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            popupEatFoodEditBinding.popupEatFoodEditTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            popupEatFoodEditBinding.editFoodName.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style));
            popupEatFoodEditBinding.eatFoodServingSpinner.setBackground(ContextCompat.getDrawable(this, R.drawable.night_spinner_style));
            popupEatFoodEditBinding.editServingLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style));
            popupEatFoodEditBinding.eatFoodEditBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style4));
            popupEatFoodEditBinding.eatFoodDeleteBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style4));
            popupEatFoodEditBinding.cancleEatFoodEditBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
            popupEatFoodEditBinding.cancleEatFoodEditBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));

            servingAdapter = new ArrayAdapter(getApplicationContext(), R.layout.night_spinner_item, serving_Data);
        }

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        Intent intent = getIntent();
        String food_code = intent.getStringExtra("food_code");
        String eat_date = intent.getStringExtra("eat_date");
        String food_name = intent.getStringExtra("food_name");

        popupEatFoodEditBinding.editFoodName.setText(food_name);

        serving = intent.getIntExtra("serving", -1);
        food_kcal = intent.getDoubleExtra("food_kcal", 0);
        food_carbs = intent.getDoubleExtra("food_carbs", 0);
        food_protein = intent.getDoubleExtra("food_protein", 0);
        food_fat = intent.getDoubleExtra("food_fat", 0);
        food_sugars = intent.getDoubleExtra("food_sugars", 0);
        food_sodium = intent.getDoubleExtra("food_sodium", 0);
        food_CH = intent.getDoubleExtra("food_CH", 0);
        food_Sat_fat = intent.getDoubleExtra("food_Sat_fat", 0);
        food_trans_fat = intent.getDoubleExtra("food_trans_fat", 0);
        set_Nu(serving); //현재 인분에 대한 먹은 음식의 영양분을 표시

        if(serving == -1){
            Toast.makeText(getApplicationContext(), "데이터 전송 오류 발생", Toast.LENGTH_SHORT).show();
            finish();
        }

        popupEatFoodEditBinding.eatFoodServingSpinner.setAdapter(servingAdapter);

        popupEatFoodEditBinding.eatFoodServingSpinner.setSelection(serving - 1); //이전 serving값이 default 값

        popupEatFoodEditBinding.eatFoodServingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                set_Nu(Integer.parseInt(popupEatFoodEditBinding.eatFoodServingSpinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        popupEatFoodEditBinding.eatFoodEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int serving = Integer.parseInt(popupEatFoodEditBinding.eatFoodServingSpinner.getSelectedItem().toString());
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if(success == 0){ //데이터 베이스에서 변경이 성공했다면.
                                Intent intent = new Intent();
                                intent.putExtra("serving", serving); //클릭 이벤트를 호출한 쪽에 변경된 인분 정보를 전달
                                setResult(1, intent);
                                finish();
                            }
                            else if(success == 1){
                                Toast.makeText(view.getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                            }
                            else if(success == 2){
                                Toast.makeText(view.getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                EditEatFoodRequest editEatFoodRequest = new EditEatFoodRequest(serving, sharedPreferences.getString("ID", null), eat_date, food_code, responseListener);
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(editEatFoodRequest);
            }
        });
        popupEatFoodEditBinding.eatFoodDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if(success == 0){ //데이터 베이스에서 제거가 되었다면
                                setResult(2);
                                finish();
                            }
                            else if(success == 1){
                                Toast.makeText(view.getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                            }
                            else if(success == 2){
                                Toast.makeText(view.getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                DeleteEatFoodRequest deleteEatFoodRequest = new DeleteEatFoodRequest(sharedPreferences.getString("ID", null), eat_date, food_code, responseListener);
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(deleteEatFoodRequest);
            }
        });

        popupEatFoodEditBinding.cancleEatFoodEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void set_Nu(int selecte_serving){ //먹은 음식의 경우 인분값에 따른 영양분 정보가 들어있으므로 1인분 영양분을 계산하기 위해 먼저 기존의 인분 정보로 나눈 후 변경된 인분 정보에 대한 영양분 표시
        popupEatFoodEditBinding.eatFoodEditKcal.setText("칼로리 : " + String.format("%.2f", food_kcal / serving * selecte_serving) + " (kcal)");
        popupEatFoodEditBinding.eatFoodEditCarbs.setText("탄수화물 : " + String.format("%.2f", food_carbs / serving* selecte_serving) + " (g)");
        popupEatFoodEditBinding.eatFoodEditProtein.setText("단백질 : " + String.format("%.2f", food_protein / serving* selecte_serving) + " (g)");
        popupEatFoodEditBinding.eatFoodEditFat.setText("지방 : " + String.format("%.2f", food_fat / serving * selecte_serving) + " (g)");
        popupEatFoodEditBinding.eatFoodEditSugars.setText("당분 : " + String.format("%.2f", food_sugars / serving * selecte_serving) + " (g)");
        popupEatFoodEditBinding.eatFoodEditSodium.setText("나트륨 : " + String.format("%.2f", food_sodium / serving * selecte_serving) + " (mg)");
        popupEatFoodEditBinding.eatFoodEditCH.setText("콜레스테롤 : " + String.format("%.2f", food_CH / serving * selecte_serving) + " (mg)");
        popupEatFoodEditBinding.eatFoodEditSatFat.setText("포화지방 : " + String.format("%.2f", food_Sat_fat / serving * selecte_serving) + " (g)");
        popupEatFoodEditBinding.eatFoodEditTransFat.setText("트랜스지방 : " + String.format("%.2f", food_trans_fat / serving * selecte_serving) + " (g)");

    }
}