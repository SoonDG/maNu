package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityEditInformationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Request.EditUserRequest;

public class EditInformationActivity extends AppCompatActivity {

    private ActivityEditInformationBinding editInformationBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editInformationBinding = ActivityEditInformationBinding.inflate(getLayoutInflater());
        View view = editInformationBinding.getRoot();
        setContentView(view);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        editInformationBinding.curID.setText(sharedPreferences.getString("ID", null));
        editInformationBinding.curPassword.setText(sharedPreferences.getString("Password", null));
        editInformationBinding.curAge.setText(String.valueOf(sharedPreferences.getInt("Age", 0)));
        editInformationBinding.curGender.setText(sharedPreferences.getString("Gender", null));

        String [] age_Data = getResources().getStringArray(R.array.age);
        String [] gen_Data = getResources().getStringArray(R.array.gender);
        ArrayAdapter ageAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, age_Data);
        ArrayAdapter genderAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gen_Data);

        editInformationBinding.editAge.setAdapter(ageAdapter);
        editInformationBinding.editGender.setAdapter(genderAdapter);

        editInformationBinding.cancleEditInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editInformationBinding.toEditPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditInformationActivity.this, EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        editInformationBinding.editInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = sharedPreferences.getString("ID", null);
                int Age = Integer.parseInt(editInformationBinding.editAge.getSelectedItem().toString());
                String Gender = editInformationBinding.editGender.getSelectedItem().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 0) {
                                Toast.makeText(EditInformationActivity.this, "회원정보 수정 성공", Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE); //자동 로그인 정보를 초기화
                                SharedPreferences.Editor autoLogin = sharedPreferences.edit();
                                autoLogin.putInt("Age", Age);
                                autoLogin.putString("Gender", Gender); //Password, Age, Gender 정보를 입력한 값으로 갱신
                                autoLogin.commit(); //커밋

                                ((MyAccountActivity) MyAccountActivity.context).set_myAccount(); //MyAccount Layout의 회원정보를 갱신

                                finish(); //창 닫고 회원 정보 창으로 이동
                            } else if (success == 1) {
                                Toast.makeText(EditInformationActivity.this, "로그인 데이터 전송 실패", Toast.LENGTH_SHORT).show();
                            } else if (success == 2) {
                                Toast.makeText(EditInformationActivity.this, "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EditInformationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                };

                EditUserRequest editUserRequest = new EditUserRequest(ID, Age, Gender, responseListener);
                RequestQueue queue = Volley.newRequestQueue(EditInformationActivity.this);
                queue.add(editUserRequest);
            }
        });
    }
}