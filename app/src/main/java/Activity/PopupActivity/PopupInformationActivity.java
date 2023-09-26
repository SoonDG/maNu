package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupInformationBinding;

public class PopupInformationActivity extends AppCompatActivity {

    private ActivityPopupInformationBinding popupInformationBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupInformationBinding = ActivityPopupInformationBinding.inflate(getLayoutInflater());
        View view = popupInformationBinding.getRoot();
        setContentView(view);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                popupInformationBinding.popupInformationTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
                popupInformationBinding.closeInformationBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style3));
                popupInformationBinding.closeInformationBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));

                break;
        }

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        Intent intent = getIntent();
        String contents = intent.getStringExtra("Contents");
        popupInformationBinding.popupInformationContents.setText(contents);

        popupInformationBinding.closeInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}