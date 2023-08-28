package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.my_first_project.databinding.ActivityPopupInformationBinding;

public class PopupInformationActivity extends AppCompatActivity {

    private ActivityPopupInformationBinding popupInformationBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupInformationBinding = ActivityPopupInformationBinding.inflate(getLayoutInflater());
        View view = popupInformationBinding.getRoot();
        setContentView(view);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        popupInformationBinding.popupInformationTitle.setText(title);

        popupInformationBinding.popupInformationCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}