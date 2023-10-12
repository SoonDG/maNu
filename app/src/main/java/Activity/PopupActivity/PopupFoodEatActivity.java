package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupFoodEatBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import Request.EatFoodRequest;

public class PopupFoodEatActivity extends AppCompatActivity {
    private ActivityPopupFoodEatBinding popupFoodEatBinding;
    private Calendar calendar;
    private int year, month, day;
    private double food_kcal, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupFoodEatBinding = ActivityPopupFoodEatBinding.inflate(getLayoutInflater());
        View view = popupFoodEatBinding.getRoot();
        setContentView(view);

        String [] serving_Data = this.getResources().getStringArray(R.array.serving);
        ArrayAdapter servingAdapter = new ArrayAdapter(this, R.layout.spinner_item, serving_Data);

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            popupFoodEatBinding.popupFoodEatTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            popupFoodEatBinding.selecteEatDateBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.MyNuWhite));
            popupFoodEatBinding.foodServingSpinner.setBackground(ContextCompat.getDrawable(this, R.drawable.night_spinner_style));
            popupFoodEatBinding.servingLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style));
            popupFoodEatBinding.foodEatBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style4));
            popupFoodEatBinding.cancleFoodEatBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
            popupFoodEatBinding.cancleFoodEatBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));

            servingAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, serving_Data);
        }

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        Intent intent = getIntent();
        String food_code = intent.getStringExtra("food_code");
        String food_name = intent.getStringExtra("food_name");

        food_kcal = intent.getDoubleExtra("food_kcal", 0);
        food_carbs = intent.getDoubleExtra("food_carbs", 0);
        food_protein = intent.getDoubleExtra("food_protein", 0);
        food_fat = intent.getDoubleExtra("food_fat", 0);
        food_sugars = intent.getDoubleExtra("food_sugars", 0);
        food_sodium = intent.getDoubleExtra("food_sodium", 0);
        food_CH = intent.getDoubleExtra("food_CH", 0);
        food_Sat_fat = intent.getDoubleExtra("food_Sat_fat", 0);
        food_trans_fat = intent.getDoubleExtra("food_trans_fat", 0);
        set_Nu(1); //1인분 기준 영양분을 표시

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        set_Date();

        popupFoodEatBinding.foodServingSpinner.setAdapter(servingAdapter);

        popupFoodEatBinding.foodServingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                set_Nu(Integer.parseInt(popupFoodEatBinding.foodServingSpinner.getSelectedItem().toString())); //선택한 인분 정보에 맞게 영양분 정보를 갱신
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        popupFoodEatBinding.selecteEatDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PopupFoodEatActivity.this, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selected_Year, int selected_Month, int selected_Day) {
                        year = selected_Year; month = selected_Month; day = selected_Day;
                        set_Date();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        popupFoodEatBinding.foodEatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if(success == 0){
                                Toast.makeText(view.getContext(), food_name + " " + popupFoodEatBinding.foodServingSpinner.getSelectedItem().toString() + "인분을 먹은 음식에 담았습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else if(success == -1){
                                Toast.makeText(view.getContext(), food_name + "은 이미 해당 날짜의 먹은 음식에 포함 되어 있습니다.", Toast.LENGTH_SHORT).show();
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
                        finish();
                    }
                };

                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                EatFoodRequest eatFoodRequest = new EatFoodRequest(sharedPreferences.getString("ID", null), year + "-" + (month + 1) + "-" + day, Integer.parseInt(popupFoodEatBinding.foodServingSpinner.getSelectedItem().toString()), food_code, responseListener);
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(eatFoodRequest);
            }
        });

        popupFoodEatBinding.cancleFoodEatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void set_Date(){
        popupFoodEatBinding.foodYear.setText(String.valueOf(year));
        popupFoodEatBinding.foodMonth.setText(String.valueOf(month + 1));
        popupFoodEatBinding.foodDay.setText(String.valueOf(day));
    }

    private void set_Nu(int serving){
        popupFoodEatBinding.foodEatKcal.setText("칼로리 : " + String.format("%.2f", food_kcal * serving) + " (kcal)");
        popupFoodEatBinding.foodEatCarbs.setText("탄수화물 : " + String.format("%.2f",food_carbs * serving) + " (g)");
        popupFoodEatBinding.foodEatProtein.setText("단백질 : " + String.format("%.2f",food_protein * serving) + " (g)");
        popupFoodEatBinding.foodEatFat.setText("지방 : " + String.format("%.2f",food_fat * serving) + " (g)");
        popupFoodEatBinding.foodEatSugars.setText("당분 : " + String.format("%.2f",food_sugars * serving) + " (g)");
        popupFoodEatBinding.foodEatSodium.setText("나트륨 : " + String.format("%.2f",food_sodium * serving) + " (mg)");
        popupFoodEatBinding.foodEatCH.setText("콜레스테롤 : " + String.format("%.2f",food_CH * serving) + " (mg)");
        popupFoodEatBinding.foodEatSatFat.setText("포화지방 : " + String.format("%.2f",food_Sat_fat * serving) + " (g)");
        popupFoodEatBinding.foodEatTransFat.setText("트랜스지방 : " + String.format("%.2f",food_trans_fat * serving) + " (g)");

    }
}