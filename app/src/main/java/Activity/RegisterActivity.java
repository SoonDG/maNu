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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    EditText ID_Text, Pass_Text;
    Button reg_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner age_spi = (Spinner) findViewById(R.id.spinner);
        Spinner gen_spi = (Spinner)findViewById(R.id.spinner3);
        String [] age_Data = getResources().getStringArray(R.array.age);
        String [] gen_Data = getResources().getStringArray(R.array.gender);
        ArrayAdapter ageAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, age_Data);
        ArrayAdapter genderAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, gen_Data);

        age_spi.setAdapter(ageAdapter);
        gen_spi.setAdapter(genderAdapter);

        ID_Text = findViewById(R.id.reg_ID_Text);
        Pass_Text = findViewById(R.id.reg_Pass_Text);

        reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = ID_Text.getText().toString();
                String Password = Pass_Text.getText().toString();
                int Age = Integer.parseInt(age_spi.getSelectedItem().toString());
                String Gender = gen_spi.getSelectedItem().toString();

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