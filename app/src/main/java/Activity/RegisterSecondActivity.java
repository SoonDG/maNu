package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityRegisterSecondBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Activity.PopupActivity.PopupInformationActivity;
import Request.RegisterRequest;

public class RegisterSecondActivity extends AppCompatActivity {
private ActivityRegisterSecondBinding registerSecondBinding;
private String ID, Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerSecondBinding = ActivityRegisterSecondBinding.inflate(getLayoutInflater());
        View view = registerSecondBinding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        Password = intent.getStringExtra("Password");

        String [] age_Data = getResources().getStringArray(R.array.age);
        String [] gen_Data = getResources().getStringArray(R.array.gender);
        String [] act_Data = getResources().getStringArray(R.array.Activity);
        ArrayAdapter ageAdapter = new ArrayAdapter(this, R.layout.spinner_item, age_Data) {
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        ArrayAdapter genderAdapter = new ArrayAdapter(this, R.layout.spinner_item, gen_Data){
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };;
        ArrayAdapter activityAdapter = new ArrayAdapter(this, R.layout.spinner_item, act_Data){
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };;

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면
            registerSecondBinding.registerSecondTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));

            registerSecondBinding.ageSpinner.setBackground(ContextCompat.getDrawable(this, R.drawable.night_spinner_style));
            registerSecondBinding.genderSpinner.setBackground(ContextCompat.getDrawable(this, R.drawable.night_spinner_style));

            registerSecondBinding.registerHeightTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
            registerSecondBinding.registerHeightTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));

            registerSecondBinding.registerWeightTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
            registerSecondBinding.registerWeightTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));

            registerSecondBinding.registerBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style2));

            ageAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, age_Data){
                @Override
                public int getCount() {
                    return super.getCount() - 1;
                }
            };
            genderAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, gen_Data){
                @Override
                public int getCount() {
                    return super.getCount() - 1;
                }
            };
            activityAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, act_Data){
                @Override
                public int getCount() {
                    return super.getCount() - 1;
                }
            };
        }

        registerSecondBinding.ageSpinner.setAdapter(ageAdapter);
        registerSecondBinding.genderSpinner.setAdapter(genderAdapter);
        registerSecondBinding.activitySpinner.setAdapter(activityAdapter);

        registerSecondBinding.ageSpinner.setSelection(ageAdapter.getCount());
        registerSecondBinding.genderSpinner.setSelection(genderAdapter.getCount());
        registerSecondBinding.activitySpinner.setSelection(activityAdapter.getCount());
        registerSecondBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerSecondBinding.ageSpinner.getSelectedItem().toString().equals("나이")){
                    Intent intent1 = new Intent(RegisterSecondActivity.this, PopupInformationActivity.class);
                    intent1.putExtra("Contents", "정확한 나이를 선택 해 주세요.");
                    startActivity(intent1);
                }
                else if(registerSecondBinding.genderSpinner.getSelectedItem().toString().equals("성별")){
                    Intent intent1 = new Intent(RegisterSecondActivity.this, PopupInformationActivity.class);
                    intent1.putExtra("Contents", "정확한 성별을 선택 해 주세요.");
                    startActivity(intent1);
                }
                else if(registerSecondBinding.activitySpinner.getSelectedItem().toString().equals("활동량")){
                    Intent intent1 = new Intent(RegisterSecondActivity.this, PopupInformationActivity.class);
                    intent1.putExtra("Contents", "정확한 활동량을 선택 해 주세요.");
                    startActivity(intent1);
                }
                else if(registerSecondBinding.registerHeightText.getText().toString().length() == 0){
                    registerSecondBinding.registerHeightTextLayout.setError("값을 입력해 주세요.");
                }
                else if(registerSecondBinding.registerHeightText.getText().toString().equals(".")){
                    registerSecondBinding.registerHeightTextLayout.setError("유효한 값을 입력해 주세요.");
                }
                else if(registerSecondBinding.registerWeightText.getText().toString().length() == 0l){
                    registerSecondBinding.registerWeightTextLayout.setError("값을 입력해 주세요.");
                }
                else if(registerSecondBinding.registerWeightText.getText().toString().equals(".")){
                    registerSecondBinding.registerWeightTextLayout.setError("유효한 값을 입력해 주세요.");
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");

                                Intent intent = new Intent(RegisterSecondActivity.this, PopupInformationActivity.class);
                                if (success == 0) {
                                    setResult(RESULT_OK);
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
                                Toast.makeText(RegisterSecondActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    int Age = Integer.parseInt(registerSecondBinding.ageSpinner.getSelectedItem().toString());
                    String Gender = registerSecondBinding.genderSpinner.getSelectedItem().toString();
                    double Height = Double.parseDouble(registerSecondBinding.registerHeightText.getText().toString());
                    double Weight = Double.parseDouble(registerSecondBinding.registerWeightText.getText().toString());
                    int Activity = registerSecondBinding.activitySpinner.getSelectedItemPosition();

                    RegisterRequest registerRequest = new RegisterRequest(ID, Password, Age, Gender, Height, Weight, Activity, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterSecondActivity.this);
                    queue.add(registerRequest);
                }
            }
        });

        registerSecondBinding.registerHeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerSecondBinding.registerHeightTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        registerSecondBinding.registerWeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                registerSecondBinding.registerWeightTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
}