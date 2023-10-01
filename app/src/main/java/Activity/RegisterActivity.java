package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import Request.CheckIDRequest;
import Request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding registerBinding;
    private boolean checkID = false;

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

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            registerBinding.registerTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));

            registerBinding.registerIDTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
            registerBinding.registerIDTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));

            registerBinding.checkIDBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style2));

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

            registerBinding.regBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style2));

            ageAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, age_Data);
            genderAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, gen_Data);
        }

        registerBinding.ageSpinner.setAdapter(ageAdapter);
        registerBinding.genderSpinner.setAdapter(genderAdapter);

        registerBinding.checkIDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = registerBinding.registerIDText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");

                            Intent intent = new Intent(RegisterActivity.this, PopupInformationActivity.class);
                            if (success == 0) {
                                checkID = true; //중복 확인 표시
                                registerBinding.registerIDTextLayout.setError(null); //중복 확인 요청 에러 제거.
                                intent.putExtra("Contents", "사용 가능한 아이디 입니다.");
                                startActivity(intent);
                            } else if (success == 1) {
                                intent.putExtra("Contents", "데이터 전송 실패");
                                startActivity(intent);
                            } else if (success == 2) {
                                intent.putExtra("Contents", "sql문 실행 실패");
                                startActivity(intent);
                            }
                            else if(success == -1){
                                intent.putExtra("Contents", "이미 사용중인 아이디 입니다. 다른 아이디를 입력해 주세요");
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                };

                CheckIDRequest checkIDRequest = new CheckIDRequest(ID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(checkIDRequest);
            }
        });

        registerBinding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerBinding.registerIDText.getText().toString().length() == 0){
                    registerBinding.registerIDTextLayout.setError("값을 입력해 주세요.");
                }
                else if(!checkID){
                    registerBinding.registerIDTextLayout.setError("아이디 중복 확인을 해주세요.");
                }
                else if(registerBinding.registerPasswordText.getText().toString().length() == 0){
                    registerBinding.registerPasswordTextLayout.setError("값을 입력해 주세요.");
                }
                else if(registerBinding.registerIDText.getText().toString().length() > 20 || registerBinding.registerPasswordText.getText().toString().length() > 20){
                    Intent intent = new Intent(RegisterActivity.this, PopupInformationActivity.class);
                    intent.putExtra("Contents", "아이디 또는 비밀번호의 길이를 20자 내로 해주세요.");
                    startActivity(intent);
                }
                else if(!registerBinding.registerPasswordText.getText().toString().equals(registerBinding.registerRepeatPasswordText.getText().toString())){
                    registerBinding.registerRepeatPasswordTextLayout.setError("비밀번호가 일치하지 않습니다.");
                }
                else if(registerBinding.registerHeightText.getText().toString().length() == 0){
                    registerBinding.registerHeightTextLayout.setError("값을 입력해 주세요.");
                }
                else if(registerBinding.registerHeightText.getText().toString().equals(".")){
                    registerBinding.registerHeightTextLayout.setError("유효한 값을 입력해 주세요.");
                }
                else if(registerBinding.registerWeightText.getText().toString().length() == 0l){
                    registerBinding.registerWeightTextLayout.setError("값을 입력해 주세요.");
                }
                else if(registerBinding.registerWeightText.getText().toString().equals(".")){
                    registerBinding.registerWeightTextLayout.setError("유효한 값을 입력해 주세요.");
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

                    String ID = registerBinding.registerIDText.getText().toString();
                    String Password = registerBinding.registerPasswordText.getText().toString();
                    String Repeat_Password = registerBinding.registerRepeatPasswordText.getText().toString();
                    int Age = Integer.parseInt(registerBinding.ageSpinner.getSelectedItem().toString());
                    String Gender = registerBinding.genderSpinner.getSelectedItem().toString();
                    double Height = Double.parseDouble(registerBinding.registerHeightText.getText().toString());
                    double Weight = Double.parseDouble(registerBinding.registerWeightText.getText().toString());

                    RegisterRequest registerRequest = new RegisterRequest(ID, Password, Age, Gender, Height, Weight, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });

        registerBinding.registerIDText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkID = false; //중복 확인 후 아이디 값을 변경 시 변경된 값으로 다시 중복 확인 하도록 함.
                registerBinding.registerIDTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        registerBinding.registerIDText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b && !checkID){ //ID텍스트 창에서 포커스 뺏겼을 때 && 중복 확인을 하지 않았을 때 중복확인을 요청 함.
                    registerBinding.registerIDTextLayout.setError("아이디 중복 확인을 해 주세요.");
                }
            }
        });

        registerBinding.registerPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerBinding.registerPasswordTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        registerBinding.registerRepeatPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerBinding.registerRepeatPasswordTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        registerBinding.registerHeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerBinding.registerHeightTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        registerBinding.registerWeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerBinding.registerWeightTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}