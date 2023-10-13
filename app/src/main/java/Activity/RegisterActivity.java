package Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Header;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Activity.PopupActivity.PopupInformationActivity;
import Fragment.RegisterFragment;
import Fragment.RegisterSecondFragment;
import Request.CheckIDRequest;
import Request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity{

    private ActivityRegisterBinding registerBinding;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = registerBinding.getRoot();
        setContentView(view);

        registerFragment = new RegisterFragment();
        fragmentManager.beginTransaction().replace(R.id.register_Frame_Layout, registerFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getFragments().get(0) == registerFragment){
            super.onBackPressed();
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.register_Frame_Layout, registerFragment).commit();
        }
    }
}