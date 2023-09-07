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
import Activity.PopupActivity.PopupExitActivity;
import Fragment.MainFragment;
import Fragment.MyMonthNuFragment;
import Fragment.SearchFragment;
import Model.Food;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SearchFragment searchFragment = null;
    private MyMonthNuFragment myMonthNuFragment = null;
    private MainFragment mainFragment = null;
    private ActivityResultLauncher<Intent> exitResultLauncher;

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
                    if(mainFragment == null) {
                        mainFragment = new MainFragment();
                        transaction.add(R.id.frame_Layout, mainFragment).commit(); //mainfragment가 null이면 객체 생성 후 add() 함
                    }
                    else {
                        mainFragment.set_Food_list(); //mainFragment의 아래표 갱신
                        transaction.show(mainFragment).commit();
                    }
                    if(searchFragment != null) transaction.hide(searchFragment); //searchFragment가 null이 아니면 hide()
                    if(myMonthNuFragment != null) transaction.hide(myMonthNuFragment); //myMonthNuFragment가 null이 아니면 hide()

                }
                else if(id == R.id.menu_my){
                    if(myMonthNuFragment == null) {
                        myMonthNuFragment = new MyMonthNuFragment();
                        transaction.add(R.id.frame_Layout, myMonthNuFragment).commit(); //myMonthNufragment가 null이면 객체 생성 후 add() 함
                    }
                    else {
                        myMonthNuFragment.setting_Calendar(); //myMonthNuFragment의 화면의 정보 갱신
                        myMonthNuFragment.clear_display(); //클릭한 날짜 정보를 표시하던 것을 없앰 -> 수정할 필요
                        transaction.show(myMonthNuFragment).commit();
                    }
                    if(mainFragment != null) transaction.hide(mainFragment); //mainFragment가 null이 아니면 hide()
                    if(searchFragment != null) transaction.hide(searchFragment); //searchFragment가 null이 아니면 hide()
                }
                else if(id == R.id.menu_search){
                    if(searchFragment == null) {
                        searchFragment = new SearchFragment();
                        transaction.add(R.id.frame_Layout, searchFragment).commit(); //searchfragment가 null이면 객체 생성 후 add() 함
                    }
                    else transaction.show(searchFragment).commit();
                    if(mainFragment != null) transaction.hide(mainFragment); //mainFragment가 null이 아니면 hide()
                    if(myMonthNuFragment != null) transaction.hide(myMonthNuFragment); //myMonthNuFragment가 null이 아니면 hide()
                }
                return true;
            }
        });

        exitResultLauncher = registerForActivityResult( //RecyclerView의 아이템 클릭시 발생하는 클릭 이벤트 작성 부분
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