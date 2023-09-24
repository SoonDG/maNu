package Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityMainBinding;
import com.example.my_first_project.databinding.NavigationHaederBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import Activity.PopupActivity.PopupCheckPasswordActivity;
import Activity.PopupActivity.PopupCheckResetEatFoodActivity;
import Activity.PopupActivity.PopupExitActivity;
import Fragment.MainFragment;
import Fragment.MyMonthNuFragment;
import Fragment.SearchFragment;
import Request.ResetEatFoodRequest;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SearchFragment searchFragment = null;
    private MyMonthNuFragment myMonthNuFragment = null;
    private MainFragment mainFragment = null;
    private ActivityResultLauncher<Intent> exitResultLauncher;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //액티비티 바인딩 객체 할당 및 뷰 설정
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        mainFragment = new MainFragment(); //초기 화면에 해당하는 mainFragment는 시작과 함께 생성
        fragmentManager.beginTransaction().replace(R.id.frame_Layout, mainFragment).commit(); //시작 화면을 mainFragment로

        setSupportActionBar(mainBinding.toolbar); //툴바 설정

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        NavigationHaederBinding navigationHaederBinding = NavigationHaederBinding.bind(mainBinding.menuNavigation.getHeaderView(0));
        navigationHaederBinding.navID.setText(sharedPreferences.getString("ID", null) +"님"); //아이디를 네비게이션 헤더에 표시

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                mainBinding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.MyNuGray)); //배경 색은 짙은 회색

                mainBinding.bottomMenuNavigation.setBackgroundColor(ContextCompat.getColor(this, R.color.MyNuGray));
                mainBinding.bottomMenuNavigation.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.night_menu_item_color));
                mainBinding.bottomMenuNavigation.setItemTextColor(ContextCompat.getColorStateList(this, R.color.night_menu_item_color));

                mainBinding.menuNavigation.setBackground(ContextCompat.getDrawable(this, R.color.MyNuBlack));
                mainBinding.menuNavigation.getHeaderView(0).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.night_textview_style));
                navigationHaederBinding.profileImage.setImageResource(R.drawable.night_profile_icon); //이후에 프로필 이미지 추가 기능을 넣을 때 유저가 정한 프로필 이미지가 있는지 확인하는 과정이 필요함.
                mainBinding.menuNavigation.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.night_menu_item_color));
                mainBinding.menuNavigation.setItemTextColor(ContextCompat.getColorStateList(this, R.color.night_menu_item_color));
                break;
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainBinding.drawerLayout, mainBinding.toolbar, R.string.app_name, R.string.app_name);
        mainBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActivityResultLauncher<Intent> checkPasswordResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        ActivityResultLauncher<Intent> checkRestEatFoodResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int success = jsonObject.getInt("success");
                                        if(success == 0){ //음식 정보 초기화 성공
                                            Toast.makeText(getApplicationContext(), "먹은 음식 정보가 초기화 되었습니다.", Toast.LENGTH_SHORT).show();
                                            if(mainFragment != null){
                                                mainFragment.set_Food_list();
                                            }
                                            if(myMonthNuFragment != null){
                                                myMonthNuFragment.renewal_Display_Nu();
                                            }
                                        }
                                        else if(success == 1){
                                            Toast.makeText(getApplicationContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(success == 2){
                                            Toast.makeText(getApplicationContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            };

                            ResetEatFoodRequest resetEatFoodRequest = new ResetEatFoodRequest(sharedPreferences.getString("ID", null), responseListener);
                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            queue.add(resetEatFoodRequest);
                        }
                    }
                });

        mainBinding.menuNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {//사이드 바 메뉴 클릭 이벤트
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int id = item.getItemId();
               if(id == R.id.account){ //계정 정보 창으로 전환
                   Intent intent = new Intent(MainActivity.this, PopupCheckPasswordActivity.class);
                   checkPasswordResultLauncher.launch(intent);
               }
               else if(id == R.id.logout){ //로그아웃 후, 다시 로그인 화면으로 전환
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE); //자동 로그인 정보를 초기화
                    SharedPreferences.Editor autoLogin = sharedPreferences.edit();
                    autoLogin.clear();
                    autoLogin.commit();
                    finish();
               }
               else if(id == R.id.food_Reset) {
                   Intent intent = new Intent(MainActivity.this, PopupCheckResetEatFoodActivity.class);
                   checkRestEatFoodResultLauncher.launch(intent);
               }

               return true;
            }
        });

        mainBinding.bottomMenuNavigation.setSelectedItemId(R.id.menu_home); //처음 화면에 맞춰 처음엔 홈버튼이 선택되도록 설정
        mainBinding.bottomMenuNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() { //하단 메뉴 클릭 이벤트
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                int id = item.getItemId();
                if(id == R.id.menu_home){
                    if(mainFragment == null) {
                        mainFragment = new MainFragment();
                        transaction.add(R.id.frame_Layout, mainFragment); //mainfragment가 null이면 객체 생성 후 add() 함
                    }
                    else {
                        mainFragment.set_Food_list(); //mainFragment의 아래표 갱신
                        transaction.show(mainFragment);
                    }
                    if(searchFragment != null) transaction.hide(searchFragment); //searchFragment가 null이 아니면 hide()
                    if(myMonthNuFragment != null) transaction.hide(myMonthNuFragment); //myMonthNuFragment가 null이 아니면 hide()

                }
                else if(id == R.id.menu_my){
                    if(myMonthNuFragment == null) {
                        myMonthNuFragment = new MyMonthNuFragment();
                        transaction.add(R.id.frame_Layout, myMonthNuFragment); //myMonthNufragment가 null이면 객체 생성 후 add() 함
                    }
                    else {
                        myMonthNuFragment.renewal_Display_Nu();
                        transaction.show(myMonthNuFragment);
                    }
                    if(mainFragment != null) transaction.hide(mainFragment); //mainFragment가 null이 아니면 hide()
                    if(searchFragment != null) transaction.hide(searchFragment); //searchFragment가 null이 아니면 hide()
                }
                else if(id == R.id.menu_search){
                    if(searchFragment == null) {
                        searchFragment = new SearchFragment();
                        transaction.add(R.id.frame_Layout, searchFragment); //searchfragment가 null이면 객체 생성 후 add() 함
                    }
                    else transaction.show(searchFragment);
                    if(mainFragment != null) transaction.hide(mainFragment); //mainFragment가 null이 아니면 hide()
                    if(myMonthNuFragment != null) transaction.hide(myMonthNuFragment); //myMonthNuFragment가 null이 아니면 hide()
                }

                transaction.commit(); //변경된 내용을 commit
                return true;
            }
        });

        exitResultLauncher = registerForActivityResult( //뒤로 가기 버튼 누를 시 나타나는 팝업 창에서 종료 버튼 눌렀을 때 프로그램 종료 시키는 함수
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
       Intent intent = new Intent(this, PopupExitActivity.class);
       exitResultLauncher.launch(intent);
    }
}