package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Header;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Activity.PopupActivity.PopupInformationActivity;
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
        ArrayAdapter ageAdapter = new ArrayAdapter(this, R.layout.spinner_item, age_Data);
        ArrayAdapter genderAdapter = new ArrayAdapter(this, R.layout.spinner_item, gen_Data);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                registerBinding.registerTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));

                registerBinding.registerIDTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
                registerBinding.registerIDTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));

                registerBinding.registerPasswordTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
                registerBinding.registerPasswordTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
                registerBinding.registerPasswordTextLayout.setEndIconTintList(ContextCompat.getColorStateList(this, R.color.MyNuWhite));

                registerBinding.registerRepeatPasswordTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
                registerBinding.registerRepeatPasswordTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
                registerBinding.registerRepeatPasswordTextLayout.setEndIconTintList(ContextCompat.getColorStateList(this, R.color.MyNuWhite));

                registerBinding.registerHeightTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
                registerBinding.registerHeightTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));

                registerBinding.registerWeightTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
                registerBinding.registerWeightTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));

                registerBinding.regBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style2));

                ageAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, age_Data);
                genderAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, gen_Data);
                break;
        }

        registerBinding.ageSpinner.setAdapter(ageAdapter);
        registerBinding.genderSpinner.setAdapter(genderAdapter);

        registerBinding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = registerBinding.registerIDText.getText().toString();
                String Password = registerBinding.registerPasswordText.getText().toString();
                String Repeat_Password = registerBinding.registerRepeatPasswordText.getText().toString();
                int Age = Integer.parseInt(registerBinding.ageSpinner.getSelectedItem().toString());
                String Gender = registerBinding.genderSpinner.getSelectedItem().toString();
                double Height = Double.parseDouble(registerBinding.registerHeightText.getText().toString());
                double Weight = Double.parseDouble(registerBinding.registerWeightText.getText().toString());
                Intent intent = new Intent(RegisterActivity.this, PopupInformationActivity.class);
                if(ID.isEmpty() || Password.isEmpty()){
                    intent.putExtra("Contents", "비어있는 칸을 모두 채워주세요.");
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "비어있는 칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(ID.length() > 20 || Password.length() > 20){
                    intent.putExtra("Contents", "아이디 또는 비밀번호의 길이를 20자 내로 해주세요.");
                    startActivity(intent);
                }
                else if(!Password.equals(Repeat_Password)){
                    intent.putExtra("Contents", "비밀번호가 다릅니다. 비밀번호를 똑같이 2번 입력해 주세요.");
                    startActivity(intent);
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");

                                Intent intent = new Intent(RegisterActivity.this, PopupInformationActivity.class);
                                if (success == 0) {
                                    intent.putExtra("Contents", "회원가입 성공");
                                    startActivity(intent);
                                    finish(); //회원가입 창 닫고 로그인 창으로 이동
                                } else if (success == 1) {
                                    intent.putExtra("Contents", "데이터 전송 실패");
                                    startActivity(intent);
                                } else if (success == 2) {
                                    intent.putExtra("Contents", "sql문 실행 실패");
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(ID, Password, Age, Gender, Height, Weight, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}