package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupCheckResetEatFoodBinding;

public class PopupCheckResetEatFoodActivity extends AppCompatActivity {
    private ActivityPopupCheckResetEatFoodBinding popupCheckResetEatFoodBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupCheckResetEatFoodBinding = ActivityPopupCheckResetEatFoodBinding.inflate(getLayoutInflater());
        View view = popupCheckResetEatFoodBinding.getRoot();
        setContentView(view);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                popupCheckResetEatFoodBinding.checkResetEatFoodLable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                popupCheckResetEatFoodBinding.resetEatFoodBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style4));
                popupCheckResetEatFoodBinding.canclePopupCheckRestEatFoodBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style3));
                popupCheckResetEatFoodBinding.canclePopupCheckRestEatFoodBtn.setTextColor(Color.parseColor("#ffffff"));

                break;
            case Configuration.UI_MODE_NIGHT_NO: //나이트 모드가 아니라면
                popupCheckResetEatFoodBinding.checkResetEatFoodLable.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                popupCheckResetEatFoodBinding.resetEatFoodBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style4));
                popupCheckResetEatFoodBinding.canclePopupCheckRestEatFoodBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style3));
                popupCheckResetEatFoodBinding.canclePopupCheckRestEatFoodBtn.setTextColor(Color.parseColor("#A6A6A6"));

                break;
        }

        popupCheckResetEatFoodBinding.resetEatFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK); //MainActivity에서 먹은 음식 정보를 초기화하도록 결과를 RESULT_OK로 설정 후 팝업 창 닫기
                finish();
            }
        });

        popupCheckResetEatFoodBinding.canclePopupCheckRestEatFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //팝업 창 닫기
            }
        });
    }
}