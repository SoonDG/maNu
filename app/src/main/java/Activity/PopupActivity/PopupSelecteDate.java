package Activity.PopupActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;

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