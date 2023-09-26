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
                popupCheckResetEatFoodBinding.popupCheckResetEatFoodTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
                popupCheckResetEatFoodBinding.resetEatFoodBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style4));
                popupCheckResetEatFoodBinding.cancleResetEatFoodBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
                popupCheckResetEatFoodBinding.cancleResetEatFoodBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));

                break;
        }

        popupCheckResetEatFoodBinding.resetEatFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK); //MainActivity에서 먹은 음식 정보를 초기화하도록 결과를 RESULT_OK로 설정 후 팝업 창 닫기
                finish();
            }
        });

        popupCheckResetEatFoodBinding.cancleResetEatFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //팝업 창 닫기
            }
        });
    }
}