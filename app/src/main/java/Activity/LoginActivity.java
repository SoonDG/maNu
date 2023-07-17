package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;

import org.json.JSONException;
import org.json.JSONObject;

import Request.LoginRequest;

public class LoginActivity extends AppCompatActivity {

    private EditText ID_Text, Pass_Text;
    private Button login_btn, main_btn, to_reg_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ID_Text = findViewById(R.id.login_ID_Text);
        Pass_Text = findViewById(R.id.login_Pass_Text);

        login_btn = findViewById(R.id.login_btn);
        main_btn = findViewById(R.id.imsi_btn); //로그인 기능 만들기 전까지 메인 화면 전환용 임시 버튼
        to_reg_btn = findViewById(R.id.to_reg_btn); //회원가입 버튼
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        to_reg_btn.setOnClickListener(new View.OnClickListener() { //회원가입 화면으로 전환
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = ID_Text.getText().toString();
                String Password = Pass_Text.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String ID = jsonObject.getString("ID");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("ID", ID);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT);
                                return;
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
        });
    }
}