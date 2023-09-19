package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupCheckPasswordBinding;

public class PopupCheckPasswordActivity extends AppCompatActivity {

    private ActivityPopupCheckPasswordBinding popupCheckPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupCheckPasswordBinding = ActivityPopupCheckPasswordBinding.inflate(getLayoutInflater());
        View view = popupCheckPasswordBinding.getRoot();
        setContentView(view);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                popupCheckPasswordBinding.checkPasswordLable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                popupCheckPasswordBinding.checkPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style));
                popupCheckPasswordBinding.checkPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style4));
                popupCheckPasswordBinding.canclePopupCheckPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style3));
                popupCheckPasswordBinding.canclePopupCheckPasswordBtn.setTextColor(Color.parseColor("#ffffff"));

                break;
            case Configuration.UI_MODE_NIGHT_NO: //나이트 모드가 아니라면
                popupCheckPasswordBinding.checkPasswordLable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style2));
                popupCheckPasswordBinding.checkPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style));
                popupCheckPasswordBinding.checkPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style4));
                popupCheckPasswordBinding.canclePopupCheckPasswordBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style3));
                popupCheckPasswordBinding.canclePopupCheckPasswordBtn.setTextColor(Color.parseColor("#A6A6A6"));

                break;
        }

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