package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = registerBinding.getRoot();
        setContentView(view);

        String [] age_Data = getResources().getStringArray(R.array.age);
        String [] gen_Data = getResources().getStringArray(R.array.gender);
        ArrayAdapter ageAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, age_Data);
        ArrayAdapter genderAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gen_Data);

        registerBinding.ageSpinner.setAdapter(ageAdapter);
        registerBinding.genderSpinner.setAdapter(genderAdapter);

        registerBinding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = registerBinding.regIDText.getText().toString();
                String Password = registerBinding.regPassText.getText().toString();
                String Repeat_Password = registerBinding.repeatRegPassText.getText().toString();
                int Age = Integer.parseInt(registerBinding.ageSpinner.getSelectedItem().toString());
                String Gender = registerBinding.genderSpinner.getSelectedItem().toString();

                if(ID.isEmpty() || Password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "비어있는 칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(ID.length() > 20 || Password.length() > 20){
                    Toast.makeText(RegisterActivity.this, "아이디 또는 비밀번호의 길이를 20자 내로 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!Password.equals(Repeat_Password)){
                    Toast.makeText(RegisterActivity.this, "비밀번호가 다릅니다. 비밀번호를 똑같이 2번 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                if (success == 0) {
                                    Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    finish(); //회원가입 창 닫고 로그인 창으로 이동
                                } else if (success == 1) {
                                    Toast.makeText(RegisterActivity.this, "로그인 데이터 전송 실패", Toast.LENGTH_SHORT).show();
                                } else if (success == 2) {
                                    Toast.makeText(RegisterActivity.this, "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(ID, Password, Age, Gender, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}