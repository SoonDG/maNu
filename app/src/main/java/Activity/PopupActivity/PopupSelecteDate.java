package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityPopupSelecteDateBinding;

public class PopupSelecteDate extends AppCompatActivity {

    private ActivityPopupSelecteDateBinding popupSelecteDateBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupSelecteDateBinding = ActivityPopupSelecteDateBinding.inflate(getLayoutInflater());
        View view = popupSelecteDateBinding.getRoot();
        setContentView(view);

        String [] year_Data = this.getResources().getStringArray(R.array.Year);
        String [] month_Data = this.getResources().getStringArray(R.array.Month);
        ArrayAdapter yearAdapter = null;
        ArrayAdapter monthAdapter = null;

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                popupSelecteDateBinding.popupSelecteDateTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style2));
                popupSelecteDateBinding.selecteDateBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style4));
                popupSelecteDateBinding.cancleSelecteDateBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_button_style3));
                popupSelecteDateBinding.cancleSelecteDateBtn.setTextColor(Color.parseColor("#ffffff"));

                yearAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, year_Data);
                monthAdapter = new ArrayAdapter(this, R.layout.night_spinner_item, month_Data);
                break;
            case Configuration.UI_MODE_NIGHT_NO: //나이트 모드가 아니라면
                popupSelecteDateBinding.popupSelecteDateTitle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_style2));
                popupSelecteDateBinding.selecteDateBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style4));
                popupSelecteDateBinding.cancleSelecteDateBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style3));
                popupSelecteDateBinding.cancleSelecteDateBtn.setTextColor(Color.parseColor("#A6A6A6"));

                yearAdapter = new ArrayAdapter(this, R.layout.spinner_item, year_Data);
                monthAdapter = new ArrayAdapter(this, R.layout.spinner_item, month_Data);
                break;
        }

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;

        popupSelecteDateBinding.dateYearSpinner.setAdapter(yearAdapter);
        popupSelecteDateBinding.dateMonthSpinner.setAdapter(monthAdapter);

        Intent intent = getIntent();
        int cur_year = intent.getIntExtra("year", -1);
        int cur_month = intent.getIntExtra("month", -1);
        if(cur_year == -1 || cur_month == -1){
            Toast.makeText(getApplicationContext(), "데이터 전송 오류 발생", Toast.LENGTH_SHORT).show();
            finish();
        }

        popupSelecteDateBinding.dateYearSpinner.setSelection(cur_year - 2000); //2023년일 경우 23번 index에 있는 2023이 자동 선택
        popupSelecteDateBinding.dateMonthSpinner.setSelection(cur_month - 1); //12월일 경우 11번 index에 있는 12이 자동 선택

        popupSelecteDateBinding.selecteDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("year", Integer.parseInt(popupSelecteDateBinding.dateYearSpinner.getSelectedItem().toString()));
                intent.putExtra("month", Integer.parseInt(popupSelecteDateBinding.dateMonthSpinner.getSelectedItem().toString()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        popupSelecteDateBinding.cancleSelecteDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}