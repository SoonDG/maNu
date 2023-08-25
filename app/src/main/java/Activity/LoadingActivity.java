package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.my_first_project.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String ID = sharedPreferences.getString("ID", null);
        String Password = sharedPreferences.getString("Password", null);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(ID != null & Password != null) {
                    intent = new Intent(getApplicationContext(), MainActivity.class); //자동 로그인이 되어있다면 메인화면으로 바로 이동
                }
                else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class); //아니면 로그인 화면으로 이동
                }
                startActivity(intent);
                finish();
            }
        }, 500); //0.5초 뒤 MainActivity로 이동
    }
}