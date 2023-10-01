package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            popupCheckPasswordBinding.popupCheckPasswordTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            popupCheckPasswordBinding.checkPasswordTextLayout.setHintTextColor(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
            popupCheckPasswordBinding.checkPasswordTextLayout.setBoxBackgroundColor(ContextCompat.getColor(this, R.color.MyNuBlack));
            popupCheckPasswordBinding.checkPasswordTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this, R.color.night_textinputlayout_color));
            popupCheckPasswordBinding.checkPasswordTextLayout.setEndIconTintList(ContextCompat.getColorStateList(this, R.color.MyNuWhite));
            popupCheckPasswordBinding.checkPasswordBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style4));
            popupCheckPasswordBinding.cancleCheckPasswordBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
            popupCheckPasswordBinding.cancleCheckPasswordBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));
        }

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        popupCheckPasswordBinding.checkPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Password = popupCheckPasswordBinding.checkPasswordText.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                if(Password.isEmpty()){
                    popupCheckPasswordBinding.checkPasswordTextLayout.setError("값을 입력해 주세요.");
                }
                else if(Password.equals(sharedPreferences.getString("Password", null))){ //입력한 비밀번호가 맞다면,
                    setResult(RESULT_OK); //MainActivity에 MyAccount Activity로 가도록 결과 전달
                    finish(); //종료
                }
                else {
                    popupCheckPasswordBinding.checkPasswordTextLayout.setError("비밀번호가 틀렸습니다.");
                }
            }
        });

        popupCheckPasswordBinding.cancleCheckPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        popupCheckPasswordBinding.checkPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                popupCheckPasswordBinding.checkPasswordTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}