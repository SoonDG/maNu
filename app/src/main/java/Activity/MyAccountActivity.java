package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityMyAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Request.AccountRequest;
import Request.RegisterRequest;

public class MyAccountActivity extends AppCompatActivity {
    private ActivityMyAccountBinding myAccountBinding;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccountBinding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        View view = myAccountBinding.getRoot();
        setContentView(view);

        context = this; //EditInformationActivity에서 MyAccountActivity의 함수에 접근할 수 있도록 Context 객체 생성.
        set_myAccount(); //회원 정보 표시


        myAccountBinding.toEditInformationBtn.setOnClickListener(new View.OnClickListener() { //회원 정보 수정
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, EditInformationActivity.class);
                startActivity(intent); //회원 정보 수정 창 띄우기
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
                        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                        //회원 탈퇴 기능 작성 부분
                        String ID = sharedPreferences.getString("ID", null);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    if(success == 0){ //데이터 베이스에서 회원 정보 삭제 성공.
                                        Toast.makeText(MyAccountActivity.this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                        SharedPreferences.Editor autoLogin = sharedPreferences.edit(); //앱 내부의 회원 정보 초기화.
                                        autoLogin.clear();
                                        autoLogin.commit();
                                        dialogInterface.dismiss();
                                        finish(); //창 닫고 로그인 창으로 이동
                                    }
                                    else if(success == 1){
                                        Toast.makeText(MyAccountActivity.this, "로그인 데이터 전송 실패", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                    else if(success == 2){
                                        Toast.makeText(MyAccountActivity.this, "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(MyAccountActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    throw new RuntimeException(e);
                                }
                            }
                        };

                        AccountRequest accountRequest = new AccountRequest(ID, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MyAccountActivity.this);
                        queue.add(accountRequest);
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

    public void set_myAccount(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        myAccountBinding.myId.setText(sharedPreferences.getString("ID", null));
        myAccountBinding.myPassword.setText(sharedPreferences.getString("Password", null));
        myAccountBinding.myAge.setText(String.valueOf(sharedPreferences.getInt("Age", 0)));
        myAccountBinding.myGender.setText(sharedPreferences.getString("Gender", null));
    }
}