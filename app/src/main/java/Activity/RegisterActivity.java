package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = registerBinding.getRoot();
        setContentView(view);

        String [] age_Data = getResources().getStringArray(R.array.age);
        String [] gen_Data = getResources().getStringArray(R.array.gender);
        ArrayAdapter ageAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, age_Data);
        ArrayAdapter genderAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gen_Data);

        registerBinding.ageSpinner.setAdapter(ageAdapter);
        registerBinding.genderSpinner.setAdapter(genderAdapter);

        registerBinding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = registerBinding.regIDText.getText().toString();
                String Password = registerBinding.regPassText.getText().toString();
                int Age = Integer.parseInt(registerBinding.ageSpinner.getSelectedItem().toString());
                String Gender = registerBinding.genderSpinner.getSelectedItem().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT);
                                finish(); //회원가입 창 닫고 로그인 창으로 이동
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT);
                                return;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            throw new RuntimeException(e);
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(ID, Password, Age, Gender, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });
    }
}