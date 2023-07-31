package Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

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

        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        LoginID = sharedPreferences.getString("ID", null);
        LoginPass = sharedPreferences.getString("Password", null);
        if(LoginID != null && LoginPass != null){
            check_login(LoginID, LoginPass); //자동 로그인
        }

        loginBinding.toRegBtn.setOnClickListener(new View.OnClickListener() { //회원가입 화면으로 전환
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = loginBinding.loginIDText.getText().toString();
                String Password = loginBinding.loginPassText.getText().toString();
                if(ID.isEmpty() || Password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "비어있는 칸을 모두 채워주세요.", Toast.LENGTH_SHORT);
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
                        int Age = jsonObject.getInt("Age");
                        String Gender = jsonObject.getString("Gender");
                        if(LoginID == null && LoginPass == null){ //자동 로그인 정보가 저장되어 있지 않다면
                            SharedPreferences.Editor autoLogin = sharedPreferences.edit(); //자동 로그인 되도록 입력한 정보를 저장
                            autoLogin.putString("ID", ID);
                            autoLogin.putString("Password", Password);
                            autoLogin.putInt("Age", Age);
                            autoLogin.putString("Gender", Gender);
                            autoLogin.commit();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(success == 1){
                        Toast.makeText(getApplicationContext(), "로그인 데이터 전송 실패", Toast.LENGTH_SHORT);
                    }
                    else if(success == 2){
                        Toast.makeText(getApplicationContext(), "sql문 실행 실패", Toast.LENGTH_SHORT);
                    }
                    else if(success == 3){
                        Toast.makeText(getApplicationContext(), "일치하는 계정이 존재하지 않습니다.", Toast.LENGTH_SHORT);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                    throw new RuntimeException(e);
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(ID, Password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }
}