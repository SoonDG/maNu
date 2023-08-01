package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityMyAccountBinding;

public class MyAccountActivity extends AppCompatActivity {
    private ActivityMyAccountBinding myAccountBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccountBinding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        View view = myAccountBinding.getRoot();
        setContentView(view);

        //임시 코드
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        myAccountBinding.textView.setText(sharedPreferences.getString("ID", null));
        myAccountBinding.textView3.setText(sharedPreferences.getString("Password", null));
        myAccountBinding.textView4.setText(String.valueOf(sharedPreferences.getInt("Age", 0)));
        myAccountBinding.textView6.setText(sharedPreferences.getString("Gender", null));
    }
}