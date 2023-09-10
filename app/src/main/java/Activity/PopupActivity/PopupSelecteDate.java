package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int)(displayMetrics.widthPixels * 0.9);
        getWindow().getAttributes().width = width;


        String [] year_Data = this.getResources().getStringArray(R.array.Year);
        String [] month_Data = this.getResources().getStringArray(R.array.Month);
        ArrayAdapter yearAdapter = new ArrayAdapter(this, R.layout.spinner_item, year_Data);
        ArrayAdapter monthAdapter = new ArrayAdapter(this, R.layout.spinner_item, month_Data);
        popupSelecteDateBinding.dateYearSpinner.setAdapter(yearAdapter);
        popupSelecteDateBinding.dateMonthSpinner.setAdapter(monthAdapter);

        Intent intent = getIntent();
        int cur_year = intent.getIntExtra("cur_year", -1);
        int cur_month = intent.getIntExtra("cur_month", -1);
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