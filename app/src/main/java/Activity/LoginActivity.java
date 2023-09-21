package Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.WeakHashMap;

import Activity.PopupActivity.PopupInformationActivity;
import Request.LoginRequest;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private String LoginID, LoginPass;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                loginBinding.loginTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                loginBinding.loginIDText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_edit_text_style));
                loginBinding.loginIDText.setHintTextColor(Color.parseColor("#464646"));
                loginBinding.loginPasswordText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_edit_text_style));
                loginBinding.loginPasswordText.setHintTextColor(Color.parseColor("#464646"));
                loginBinding.loginBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style2));
                loginBinding.toRegBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style));
                loginBinding.toRegBtn.setTextColor(Color.parseColor("#ffffff"));

                break;
            case Configuration.UI_MODE_NIGHT_NO: //나이트 모드가 아니라면
                loginBinding.loginTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style2));
                loginBinding.loginIDText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_style));
                loginBinding.loginIDText.setHintTextColor(Color.parseColor("#A6A6A6"));
                loginBinding.loginPasswordText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_style));
                loginBinding.loginPasswordText.setHintTextColor(Color.parseColor("#A6A6A6"));
                loginBinding.loginBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style2));
                loginBinding.toRegBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                loginBinding.toRegBtn.setTextColor(Color.parseColor("#A6A6A6"));

                break;
        }

        //자동 로그인
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        LoginID = sharedPreferences.getString("ID", null);
        LoginPass = sharedPreferences.getString("Password", null);
        if(LoginID != null && LoginPass != null){ //로그인 한 기록이 있다면
            Intent intent = new Intent(LoginActivity.this, MainActivity.class); //메인화면으로 이동
            startActivity(intent);
            finish();
        }

        loginBinding.toRegBtn.setOnClickListener(new View.OnClickListener() { //회원가입 화면으로 전환
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() { //로그인 기능 호출
            @Override
            public void onClick(View view) {
                String ID = loginBinding.loginIDText.getText().toString();
                String Password = loginBinding.loginPasswordText.getText().toString();
                if(ID.isEmpty() || Password.isEmpty()){
                    Intent intent = new Intent(LoginActivity.this, PopupInformationActivity.class);
                    intent.putExtra("Title", "비어있는 칸을 모두 채워주세요.");
                    startActivity(intent);
                }
                else check_login(ID, Password); //입력한 정보로 로그인
            }
        });
    }

    public void check_login(String ID, String Password) { //로그인 함수
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 0){
                        Toast.makeText(LoginActivity.this, ID + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                        if(LoginID == null && LoginPass == null){ //자동 로그인 정보가 저장 되어 있지 않다면
                            int Age = jsonObject.getInt("Age");
                            String Gender = jsonObject.getString("Gender");
                            double Height = jsonObject.getDouble("Height");
                            double Weight = jsonObject.getDouble("Weight");

                            SharedPreferences.Editor autoLogin = sharedPreferences.edit(); //자동 로그인 되도록 입력한 정보를 저장
                            autoLogin.putString("ID", ID);
                            autoLogin.putString("Password", Password);
                            autoLogin.putInt("Age", Age);
                            autoLogin.putString("Gender", Gender);
                            autoLogin.putLong("Height", Double.doubleToRawLongBits(Height)); //putDouble이 없으므로 비트 낭비없이 담기 위해서 원시 long 비트로 변환하고 long값으로 저장
                            autoLogin.putLong("Weight", Double.doubleToRawLongBits(Weight));
                            autoLogin.commit();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(success == 1){
                        Intent intent = new Intent(LoginActivity.this, PopupInformationActivity.class);
                        intent.putExtra("Title", "로그인 데이터 전송 실패");
                        startActivity(intent);
                    }
                    else if(success == 2){
                        Intent intent = new Intent(LoginActivity.this, PopupInformationActivity.class);
                        intent.putExtra("Title", "sql뮨 실행 실패");
                        startActivity(intent);
                    }
                    else if(success == 3){
                        Intent intent = new Intent(LoginActivity.this, PopupInformationActivity.class);
                        intent.putExtra("Title", "일치하는 계정이 존재하지 않습니다.");
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(ID, Password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }
}