package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.my_first_project.databinding.ActivityEditPasswordBinding;

public class EditPasswordActivity extends AppCompatActivity {

    private ActivityEditPasswordBinding editPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editPasswordBinding = ActivityEditPasswordBinding.inflate(getLayoutInflater());
        View view = editPasswordBinding.getRoot();
        setContentView(view);

        editPasswordBinding.editPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_Password = editPasswordBinding.editTextTextPassword2.getText().toString();
                String new_Password_Repeat = editPasswordBinding.editTextTextPassword.getText().toString();
                if(new_Password.equals(new_Password_Repeat)){
                    //비밀번호 변경하는 request 호출
                }
                else {
                    Toast.makeText(EditPasswordActivity.this, "동일한 비밀번호를 두 번 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editPasswordBinding.cancleEditPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });
    }
}