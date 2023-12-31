package Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityMyAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Activity.PopupActivity.PopupInformationActivity;
import Request.DeleteProfileRequest;
import Request.EditEatFoodRequest;
import Request.EditProfileRequest;
import Request.WithdrawalRequest;

public class MyAccountActivity extends AppCompatActivity {
    private ActivityMyAccountBinding myAccountBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccountBinding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        View view = myAccountBinding.getRoot();
        setContentView(view);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        if(sharedPreferences.getString("Profile", null) != null){ //미리 설정해둔 Profile 이미지가 있다면
            String imgstr = sharedPreferences.getString("Profile", null);
            byte[] bytes = Base64.decode(imgstr, Base64.DEFAULT); //String을 Base64방식으로 byte 배열로 변환
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //byte배열을 BitmapFactory의 메소드로 bitmap으로 변환
            myAccountBinding.myAccountProfile.setImageBitmap(bitmap); //해당 bitmap을 imageView에 넣기.
        }
        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            if(sharedPreferences.getString("Profile", null) == null) { //프로필이 설정 된 것이 없어 기본 프로필을 사용 중일 때
                myAccountBinding.myAccountProfile.setImageTintList(ContextCompat.getColorStateList(this, R.color.MyNuWhite)); //기본 프로필의 색상을 나이트 모드에서 잘 보이는 색으로 변경
            }
            myAccountBinding.changeProfileBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style2));
            myAccountBinding.deleteProfileBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style));
            myAccountBinding.deleteProfileBtn.setTextColor(ContextCompat.getColor(this, R.color.MyNuWhite));

            myAccountBinding.myAccountAccountInformationTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            myAccountBinding.myAccountAccountTable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_tablelayout_style));
            myAccountBinding.myIdLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            myAccountBinding.myPasswordLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));

            myAccountBinding.MyAccountUserInformationTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            myAccountBinding.myAccountUserTable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_tablelayout_style));
            myAccountBinding.myAgeLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            myAccountBinding.myGenderLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            myAccountBinding.myHeightLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));
            myAccountBinding.myWeightLable.setBackground(ContextCompat.getDrawable(this, R.drawable.night_textview_style2));

            myAccountBinding.toEditInformationBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style2));
            myAccountBinding.withdrawalBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.night_button_style2));
        }

        set_myAccount(); //회원 정보 표시

        ActivityResultLauncher<Intent> changeProfileResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent intent = result.getData();
                                Uri uri = intent.getData();
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
                                        if(bitmap != null){
                                            myAccountBinding.myAccountProfile.setImageBitmap(bitmap);
                                            myAccountBinding.myAccountProfile.setImageTintList(null);
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos); //Bitmap을 압축(70%로)
                                            byte[] bytes = baos.toByteArray(); //압축된 Bitmap을 byte 배열로 변환
                                            String imgstr = Base64.encodeToString(bytes, Base64.DEFAULT); //Base64 방식으로 byte 배열을 String으로 변환

                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        int success = jsonObject.getInt("success");
                                                        if(success == 0){ //데이터 베이스에서 변경이 성공했다면.
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("Profile", imgstr); //String으로 변환된 이미지(Bitmap)을 sharedPreferences로 기기에 저장
                                                            editor.commit();
                                                            setResult(RESULT_OK); //MainActivity로 프로필이 변경됨을 알림.

                                                            Intent intent1 = new Intent(MyAccountActivity.this, PopupInformationActivity.class);
                                                            intent1.putExtra("Contents", "프로필 이미지가 설정되었습니다.");
                                                            startActivity(intent1);
                                                        }
                                                        else if(success == 1){
                                                            Toast.makeText(view.getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if(success == 2){
                                                            Toast.makeText(view.getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            };

                                            EditProfileRequest editProfileRequest = new EditProfileRequest(sharedPreferences.getString("ID", null), imgstr, responseListener);
                                            RequestQueue queue = Volley.newRequestQueue(view.getContext());
                                            queue.add(editProfileRequest);
                                        }
                                    }
                                    else {
                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                        if(bitmap != null){
                                            myAccountBinding.myAccountProfile.setImageBitmap(bitmap);
                                            myAccountBinding.myAccountProfile.setImageTintList(null);

                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos); //Bitmap을 압축(70%로)
                                            byte[] bytes = baos.toByteArray(); //압축된 Bitmap을 byte 배열로 변환
                                            String imgstr = Base64.encodeToString(bytes, Base64.DEFAULT); //Base64 방식으로 byte 배열을 String으로 변환

                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        int success = jsonObject.getInt("success");
                                                        if(success == 0){ //데이터 베이스에서 변경이 성공했다면.
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("Profile", imgstr); //String으로 변환된 이미지(Bitmap)을 sharedPreferences로 기기에 저장
                                                            editor.commit();
                                                            setResult(RESULT_OK); //MainActivity로 프로필이 변경됨을 알림.

                                                            Intent intent1 = new Intent(MyAccountActivity.this, PopupInformationActivity.class);
                                                            intent1.putExtra("Contents", "프로필 이미지가 설정되었습니다.");
                                                            startActivity(intent1);
                                                        }
                                                        else if(success == 1){
                                                            Toast.makeText(MyAccountActivity.this, "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if(success == 2){
                                                            Toast.makeText(MyAccountActivity.this, "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            };

                                            EditProfileRequest editProfileRequest = new EditProfileRequest(sharedPreferences.getString("ID", null), imgstr, responseListener);
                                            RequestQueue queue = Volley.newRequestQueue(MyAccountActivity.this);
                                            queue.add(editProfileRequest);
                                        }
                                    }
                                }catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    }
                });

        myAccountBinding.changeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                changeProfileResultLauncher.launch(intent);
            }
        });

        myAccountBinding.deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if(success == 0){ //데이터 베이스에서 변경이 성공했다면.
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("Profile"); //SharedPreferences에서도 제거
                                editor.commit();

                                myAccountBinding.myAccountProfile.setImageResource(R.drawable.profile_icon);
                                if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
                                    myAccountBinding.myAccountProfile.setImageTintList(ContextCompat.getColorStateList(MyAccountActivity.this, R.color.MyNuWhite)); //기본 프로필의 색상을 나이트 모드에서 잘 보이는 색으로 변경
                                } //MyAccount의 프로필을 기본 이미지로 변환

                                setResult(RESULT_OK); //MainActivity로 프로필이 변경됨을 알림.

                                Intent intent1 = new Intent(MyAccountActivity.this, PopupInformationActivity.class);
                                intent1.putExtra("Contents", "프로필 이미지가 초기화 되었습니다.");
                                startActivity(intent1);
                            }
                            else if(success == 1){
                                Toast.makeText(MyAccountActivity.this, "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                            }
                            else if(success == 2){
                                Toast.makeText(MyAccountActivity.this, "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                DeleteProfileRequest deleteProfileRequest = new DeleteProfileRequest(sharedPreferences.getString("ID", null), responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyAccountActivity.this);
                queue.add(deleteProfileRequest);
            }
        });


        ActivityResultLauncher<Intent> editInformationResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            set_myAccount();
                        }
                    }
                });

        myAccountBinding.toEditInformationBtn.setOnClickListener(new View.OnClickListener() { //회원 정보 수정
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, EditInformationActivity.class);
                editInformationResultLauncher.launch(intent); //회원 정보 수정 창 띄우기
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

                        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(ID, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MyAccountActivity.this);
                        queue.add(withdrawalRequest);
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
        double Height = Double.longBitsToDouble(sharedPreferences.getLong("Height", 0));
        double Weight = Double.longBitsToDouble(sharedPreferences.getLong("Weight", 0));
        myAccountBinding.myHeight.setText(String.valueOf(Height));
        myAccountBinding.myWeight.setText(String.valueOf(Weight));
    }
}