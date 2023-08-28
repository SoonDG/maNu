package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.my_first_project.databinding.ActivityPopupCheckPasswordBinding;

public class PopupCheckPasswordActivity extends AppCompatActivity {

    private ActivityPopupCheckPasswordBinding popupCheckPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupCheckPasswordBinding = ActivityPopupCheckPasswordBinding.inflate(getLayoutInflater());
        View view = popupCheckPasswordBinding.getRoot();
        setContentView(view);

        popupCheckPasswordBinding.checkPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Password = popupCheckPasswordBinding.checkPassword.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                if(Password.equals(sharedPreferences.getString("Password", null))){ //입력한 비밀번호가 맞다면,
                    setResult(RESULT_OK); //MainActivity에 MyAccount Activity로 가도록 결과 전달
                    finish(); //종료
                }
                else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        popupCheckPasswordBinding.canclePopupCheckPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}