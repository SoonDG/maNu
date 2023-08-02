package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        myAccountBinding.myId.setText(sharedPreferences.getString("ID", null));
        myAccountBinding.myPassword.setText(sharedPreferences.getString("Password", null));
        myAccountBinding.myAge.setText(String.valueOf(sharedPreferences.getInt("Age", 0)));
        myAccountBinding.myGender.setText(sharedPreferences.getString("Gender", null));

        myAccountBinding.editInformationBtn.setOnClickListener(new View.OnClickListener() { //회원 정보 수정
            @Override
            public void onClick(View view) {

            }
        });

        myAccountBinding.withdrawalBtn.setOnClickListener(new View.OnClickListener() { //회원 탈퇴
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                ad.setMessage("정말로 탈퇴 하시겠습니까?");

                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //회원 탈퇴 기능 작성 부분
                        dialogInterface.dismiss();
                    }
                });

                ad.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); //그냥 창을 닫는다.
                    }
                });

                ad.show();
            }
        });
    }
}