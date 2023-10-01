package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupExitBinding;

public class PopupExitActivity extends AppCompatActivity {
    private ActivityPopupExitBinding popupExitBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupExitBinding = ActivityPopupExitBinding.inflate(getLayoutInflater());
        View view = popupExitBinding.getRoot();
        setContentView(view);

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            popupExitBinding.popupExitTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            popupExitBinding.exitBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style4));
            popupExitBinding.cancleExitBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
            popupExitBinding.cancleExitBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));
        }

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        popupExitBinding.exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        popupExitBinding.cancleExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}