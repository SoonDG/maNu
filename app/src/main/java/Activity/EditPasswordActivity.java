package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityEditPasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Activity.PopupActivity.PopupInformationActivity;
import Request.EditPasswordRequest;

public class EditPasswordActivity extends AppCompatActivity {

    private ActivityEditPasswordBinding editPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editPasswordBinding = ActivityEditPasswordBinding.inflate(getLayoutInflater());
        View view = editPasswordBinding.getRoot();
        setContentView(view);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                editPasswordBinding.EditPasswordLable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                editPasswordBinding.newPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style));
                editPasswordBinding.newPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style));
                editPasswordBinding.editPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style2));
                editPasswordBinding.cancleEditPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style));
                editPasswordBinding.cancleEditPasswordBtn.setTextColor(Color.parseColor("#ffffff"));

                break;
            case Configuration.UI_MODE_NIGHT_NO: //나이트 모드가 아니라면
                editPasswordBinding.EditPasswordLable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style2));
                editPasswordBinding.newPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style));
                editPasswordBinding.newPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style));
                editPasswordBinding.editPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style2));
                editPasswordBinding.cancleEditPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style));
                editPasswordBinding.cancleEditPasswordBtn.setTextColor(Color.parseColor("#A6A6A6"));

                break;
        }

        editPasswordBinding.editPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_Password = editPasswordBinding.newPassword.getText().toString();
                String new_Password_Repeat = editPasswordBinding.newPasswordRepeat.getText().toString();

                Intent intent = new Intent(EditPasswordActivity.this, PopupInformationActivity.class);
                if(!new_Password.equals(new_Password_Repeat)){
                    intent.putExtra("Title", "동일한 비밀번호를 두 번 입력해 주세요.");
                    startActivity(intent);
                }
                else if(new_Password.length() > 20){
                    intent.putExtra("Title", "변경할 비밀번호의 길이를 20자 내로 해주세요.");
                    startActivity(intent);
                }
                else{
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                if (success == 0) {
                                    intent.putExtra("Title", "비밀번호 변경 성공");
                                    startActivity(intent);

                                    SharedPreferences.Editor autoLogin = sharedPreferences.edit();
                                    autoLogin.putString("Password", new_Password); //Password, Age, Gender 정보를 입력한 값으로 갱신
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
                                Toast.makeText(EditPasswordActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    EditPasswordRequest editPasswordRequest = new EditPasswordRequest(sharedPreferences.getString("ID", null), new_Password, listener);
                    RequestQueue queue = Volley.newRequestQueue(EditPasswordActivity.this);
                    queue.add(editPasswordRequest);
                }
            }
        });

        editPasswordBinding.cancleEditPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });
    }
}