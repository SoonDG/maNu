package Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
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

import Activity.PopupActivity.PopupInformationActivity;
import Request.EditUserRequest;

public class EditInformationActivity extends AppCompatActivity {

    private ActivityEditInformationBinding editInformationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editInformationBinding = ActivityEditInformationBinding.inflate(getLayoutInflater());
        View view = editInformationBinding.getRoot();
        setContentView(view);

        String [] age_Data = getResources().getStringArray(R.array.age);
        String [] gen_Data = getResources().getStringArray(R.array.gender);
        ArrayAdapter ageAdapter = null;
        ArrayAdapter genderAdapter = null;

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                editInformationBinding.accountInformationTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                editInformationBinding.accountInformationTable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_tablelayout_style));
                editInformationBinding.userInformationTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                editInformationBinding.userInformationTable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_tablelayout_style));
                editInformationBinding.editInformationBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style2));
                editInformationBinding.toEditPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style2));
                editInformationBinding.cancleEditInformationBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style));
                editInformationBinding.cancleEditInformationBtn.setTextColor(Color.parseColor("#ffffff"));

                ageAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, age_Data);
                genderAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, gen_Data);
                break;
            case Configuration.UI_MODE_NIGHT_NO: //나이트 모드가 아니라면
                editInformationBinding.accountInformationTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style2));
                editInformationBinding.accountInformationTable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tablelayout_style));
                editInformationBinding.userInformationTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style2));
                editInformationBinding.userInformationTable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tablelayout_style));
                editInformationBinding.editInformationBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style2));
                editInformationBinding.toEditPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style2));
                editInformationBinding.cancleEditInformationBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                editInformationBinding.cancleEditInformationBtn.setTextColor(Color.parseColor("#A6A6A6"));

                ageAdapter = new ArrayAdapter(this, R.layout.spinner_item, age_Data);
                genderAdapter = new ArrayAdapter(this, R.layout.spinner_item, gen_Data);
                break;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        editInformationBinding.curID.setText(sharedPreferences.getString("ID", null));
        editInformationBinding.curPassword.setText(sharedPreferences.getString("Password", null));
        editInformationBinding.curAge.setText(String.valueOf(sharedPreferences.getInt("Age", 0)));
        editInformationBinding.curGender.setText(sharedPreferences.getString("Gender", null));

        double Height = Double.longBitsToDouble(sharedPreferences.getLong("Height", 0));
        double Weight = Double.longBitsToDouble(sharedPreferences.getLong("Weight", 0));
        editInformationBinding.curHeight.setText(String.valueOf(Height));
        editInformationBinding.curWeight.setText(String.valueOf(Weight));

        editInformationBinding.editAge.setAdapter(ageAdapter);
        editInformationBinding.editGender.setAdapter(genderAdapter);

        editInformationBinding.editAge.setSelection(sharedPreferences.getInt("Age", 0) - 1);
        if(sharedPreferences.getString("Gender", null).equals("남자")){
            editInformationBinding.editGender.setSelection(0);
        }
        else {
            editInformationBinding.editGender.setSelection(1);
        }
        editInformationBinding.editHeight.setText(String.valueOf(Height));
        editInformationBinding.editWeight.setText(String.valueOf(Weight));

        ActivityResultLauncher<Intent> editPasswordResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            setResult(RESULT_OK); //비밀번호가 변경되었어도 MyAccount에서 변경되도록 수정
                            finish(); //변경 창을 닫고, MyAccountActivity 창으로 전환
                        }
                    }
                });

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
                editPasswordResultLauncher.launch(intent);
            }
        });

        editInformationBinding.editInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = sharedPreferences.getString("ID", null);
                int Age = Integer.parseInt(editInformationBinding.editAge.getSelectedItem().toString());
                String Gender = editInformationBinding.editGender.getSelectedItem().toString();
                double Height = Double.parseDouble(editInformationBinding.editHeight.getText().toString());
                double Weight = Double.parseDouble(editInformationBinding.editWeight.getText().toString());

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");

                            Intent intent = new Intent(EditInformationActivity.this, PopupInformationActivity.class);
                            if (success == 0) {
                                intent.putExtra("Title", "회원정보 수정 성공");
                                startActivity(intent);

                                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE); //자동 로그인 정보를 초기화
                                SharedPreferences.Editor autoLogin = sharedPreferences.edit();
                                autoLogin.putInt("Age", Age);
                                autoLogin.putString("Gender", Gender); //Password, Age, Gender 정보를 입력한 값으로 갱신
                                autoLogin.putLong("Height", Double.doubleToRawLongBits(Height));
                                autoLogin.putLong("Weight", Double.doubleToRawLongBits(Weight));
                                autoLogin.commit(); //커밋

                                setResult(RESULT_OK);
                                finish(); //창 닫고 회원 정보 창으로 이동
                            } else if (success == 1) {
                                intent.putExtra("Title", "데이터 전송 실패");
                                startActivity(intent);
                            } else if (success == 2) {
                                intent.putExtra("Title", "sql문 실행 실패");
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EditInformationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                };

                EditUserRequest editUserRequest = new EditUserRequest(ID, Age, Gender, Height, Weight, responseListener);
                RequestQueue queue = Volley.newRequestQueue(EditInformationActivity.this);
                queue.add(editUserRequest);
            }
        });
    }
}