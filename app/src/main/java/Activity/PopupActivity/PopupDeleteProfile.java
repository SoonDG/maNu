package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupDeleteProfileBinding;

public class PopupDeleteProfile extends AppCompatActivity {
    private ActivityPopupDeleteProfileBinding popupDeleteProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupDeleteProfileBinding = ActivityPopupDeleteProfileBinding.inflate(getLayoutInflater());
        View view = popupDeleteProfileBinding.getRoot();
        setContentView(view);

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면
        popupDeleteProfileBinding.popupDeleteProfileTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));

        popupDeleteProfileBinding.checkDeleteProfileBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style4));

        popupDeleteProfileBinding.cancleDeleteProfileBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
        popupDeleteProfileBinding.cancleDeleteProfileBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));
        }

        popupDeleteProfileBinding.checkDeleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        popupDeleteProfileBinding.cancleDeleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}