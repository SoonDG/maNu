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
import com.example.my_first_project.databinding.ActivityPopupEatFoodEditBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Model.Food;
import Request.DeleteEatFoodRequest;
import Request.EditEatFoodRequest;

public class PopupEatFoodEditActivity extends AppCompatActivity {
    private ActivityPopupEatFoodEditBinding popupEatFoodEditBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupEatFoodEditBinding = ActivityPopupEatFoodEditBinding.inflate(getLayoutInflater());
        View view = popupEatFoodEditBinding.getRoot();
        setContentView(view);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        Intent intent = getIntent();
        String food_code = intent.getStringExtra("food_code");
        String eat_date = intent.getStringExtra("eat_date");
        int serving = intent.getIntExtra("serving", -1);
        if(serving == -1){
            Toast.makeText(getApplicationContext(), "데이터 전송 오류 발생", Toast.LENGTH_SHORT).show();
            finish();
        }

        String [] serving_Data = getApplicationContext().getResources().getStringArray(R.array.serving);
        ArrayAdapter servingAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, serving_Data);
        popupEatFoodEditBinding.eatFoodServingSpinner.setAdapter(servingAdapter);

        popupEatFoodEditBinding.eatFoodServingSpinner.setSelection(serving - 1); //이전 serving값이 default 값

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

        popupEatFoodEditBinding.canclePopupEatFoodEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}