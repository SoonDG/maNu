package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;

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