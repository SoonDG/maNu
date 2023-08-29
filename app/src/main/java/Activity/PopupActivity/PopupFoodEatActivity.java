package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupFoodEatBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Year;

import Request.EatFoodRequest;

public class PopupFoodEatActivity extends AppCompatActivity {
    private ActivityPopupFoodEatBinding popupFoodEatBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupFoodEatBinding = ActivityPopupFoodEatBinding.inflate(getLayoutInflater());
        View view = popupFoodEatBinding.getRoot();
        setContentView(view);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        Intent intent = getIntent();
        String food_code = intent.getStringExtra("food_code");
        String food_name = intent.getStringExtra("food_name");

        String [] serving_Data = this.getResources().getStringArray(R.array.serving);
        String [] year_Data = this.getResources().getStringArray(R.array.Year);
        String [] month_Data = this.getResources().getStringArray(R.array.Month);
        String [] day_Data = this.getResources().getStringArray(R.array.Day);

        ArrayAdapter servingAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, serving_Data);
        ArrayAdapter yearAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, year_Data);
        ArrayAdapter monthAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, month_Data);
        ArrayAdapter dayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, day_Data);

        popupFoodEatBinding.foodServingSpinner.setAdapter(servingAdapter);
        popupFoodEatBinding.foodYearSpinner.setAdapter(yearAdapter);
        popupFoodEatBinding.foodMonthSpinner.setAdapter(monthAdapter);
        popupFoodEatBinding.foodDaySpinner.setAdapter(dayAdapter);

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
                EatFoodRequest eatFoodRequest = new EatFoodRequest(sharedPreferences.getString("ID", null), Integer.parseInt(popupFoodEatBinding.foodServingSpinner.getSelectedItem().toString()), food_code, responseListener);
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(eatFoodRequest);
            }
        });

        popupFoodEatBinding.canclePopupFoodEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}