package Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityMainBinding;
import com.example.my_first_project.databinding.NavigationHaederBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import Activity.PopupActivity.PopupCheckPasswordActivity;
import Fragment.MainFragment;
import Fragment.MyMonthNuFragment;
import Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SearchFragment searchFragment = new SearchFragment();
    private MyMonthNuFragment myMenuFragmet = new MyMonthNuFragment();
    private MainFragment mainFragment = new MainFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //액티비티 바인딩 객체 할당 및 뷰 설정
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        fragmentManager.beginTransaction().replace(R.id.frame_Layout, mainFragment).commit(); //시작 화면을 mainFragment로

        setSupportActionBar(mainBinding.toolbar); //툴바 설정

        mainBinding.toolbar.setBackgroundColor(Color.parseColor("#464646"));
        mainBinding.toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainBinding.drawerLayout, mainBinding.toolbar, R.string.app_name, R.string.app_name);
        mainBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        NavigationHaederBinding navigationHaederBinding = NavigationHaederBinding.bind(mainBinding.menuNavigation.getHeaderView(0));
        navigationHaederBinding.navID.setText(sharedPreferences.getString("ID", null) +"님"); //아이디를 네비게이션 헤더에 표시

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
                    transaction.replace(R.id.frame_Layout, mainFragment).commit();
                }
                else if(id == R.id.menu_my){
                    transaction.replace(R.id.frame_Layout, myMenuFragmet).commit();
                }
                else if(id == R.id.menu_search){
                    transaction.replace(R.id.frame_Layout, searchFragment).commit();
                }
                return true;
            }
        });
    }

}