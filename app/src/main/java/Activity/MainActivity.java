package Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.my_first_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import Fragment.MainFragment;
import Fragment.MyMenuFragmet;
import Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private SearchFragment searchFragment = new SearchFragment();
    private MyMenuFragmet myMenuFragmet = new MyMenuFragmet();
    private MainFragment mainFragment = new MainFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager.beginTransaction().replace(R.id.frame_layout, mainFragment).commit(); //시작 화면을 mainFragment로

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //툴바 설정

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.menu_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {//사이드 바 메뉴 클릭 이벤트
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int id = item.getItemId();
               if(id == R.id.account){

               }
               else if(id == R.id.logout){

               }
               return true;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_home); //처음 화면에 맞춰 처음엔 홈버튼이 선택되도록 설정
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() { //하단 메뉴 클릭 이벤트
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                int id = item.getItemId();
                if(id == R.id.menu_home){
                    transaction.replace(R.id.frame_layout, mainFragment).commit();
                }
                else if(id == R.id.menu_my){
                    transaction.replace(R.id.frame_layout, myMenuFragmet).commit();
                }
                else if(id == R.id.menu_search){
                    transaction.replace(R.id.frame_layout, searchFragment).commit();
                }
                return true;
            }
        });

    }

}