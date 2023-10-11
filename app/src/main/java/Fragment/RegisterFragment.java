package Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.FragmentRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Activity.PopupActivity.PopupInformationActivity;
import Request.CheckIDRequest;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding fragmentRegisterBinding;

    private boolean checkID = false;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentRegisterBinding = FragmentRegisterBinding.inflate(getLayoutInflater());
        View view = fragmentRegisterBinding.getRoot();

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            fragmentRegisterBinding.registerTitle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_textview_style2));

            fragmentRegisterBinding.registerIDTextLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));
            fragmentRegisterBinding.registerIDTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));

            fragmentRegisterBinding.checkIDBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_button_style2));

            fragmentRegisterBinding.registerPasswordTextLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));
            fragmentRegisterBinding.registerPasswordTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));
            fragmentRegisterBinding.registerPasswordTextLayout.setEndIconTintList(ContextCompat.getColorStateList(getContext(), R.color.MyNuWhite));

            fragmentRegisterBinding.registerRepeatPasswordTextLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));
            fragmentRegisterBinding.registerRepeatPasswordTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));
            fragmentRegisterBinding.registerRepeatPasswordTextLayout.setEndIconTintList(ContextCompat.getColorStateList(getContext(), R.color.MyNuWhite));

            fragmentRegisterBinding.nextRegisterBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_button_style2));
            fragmentRegisterBinding.cancleRegisterBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_button_style));
            fragmentRegisterBinding.cancleRegisterBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.MyNuWhite));
        }

        fragmentRegisterBinding.checkIDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = fragmentRegisterBinding.registerIDText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");

                            Intent intent = new Intent(getContext(), PopupInformationActivity.class);
                            if (success == 0) {
                                checkID = true; //중복 확인 표시
                                fragmentRegisterBinding.registerIDTextLayout.setError(null); //중복 확인 요청 에러 제거.
                                intent.putExtra("Contents", "사용 가능한 아이디 입니다.");
                                startActivity(intent);
                            } else if (success == 1) {
                                intent.putExtra("Contents", "데이터 전송 실패");
                                startActivity(intent);
                            } else if (success == 2) {
                                intent.putExtra("Contents", "sql문 실행 실패");
                                startActivity(intent);
                            }
                            else if(success == -1){
                                intent.putExtra("Contents", "이미 사용중인 아이디 입니다. 다른 아이디를 입력해 주세요");
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                };

                CheckIDRequest checkIDRequest = new CheckIDRequest(ID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(checkIDRequest);
            }
        });

        ActivityResultLauncher<Intent> secondRegisterResultLauncher = registerForActivityResult( //패스워드 검사를 통과 했다면 MyAccount액티비티를 호출함
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            getActivity().finish(); //두번쩨 회원가입 창에서 모든 과정을 완료하고 회원가입이 끝났다면 첫번째 회원가입 창도 같이 닫음.
                        }
                    }
                });

        fragmentRegisterBinding.nextRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragmentRegisterBinding.registerIDText.getText().toString().length() == 0){
                    fragmentRegisterBinding.registerIDTextLayout.setError("값을 입력해 주세요.");
                }
                else if(!checkID){
                    fragmentRegisterBinding.registerIDTextLayout.setError("아이디 중복 확인을 해주세요.");
                }
                else if(fragmentRegisterBinding.registerPasswordText.getText().toString().length() == 0){
                    fragmentRegisterBinding.registerPasswordTextLayout.setError("값을 입력해 주세요.");
                }
                else if(fragmentRegisterBinding.registerIDText.getText().toString().length() > 20 || fragmentRegisterBinding.registerPasswordText.getText().toString().length() > 20){
                    Intent intent = new Intent(getContext(), PopupInformationActivity.class);
                    intent.putExtra("Contents", "아이디 또는 비밀번호의 길이를 20자 내로 해주세요.");
                    startActivity(intent);
                }
                else if(!fragmentRegisterBinding.registerPasswordText.getText().toString().equals(fragmentRegisterBinding.registerRepeatPasswordText.getText().toString())){
                    fragmentRegisterBinding.registerRepeatPasswordTextLayout.setError("비밀번호가 일치하지 않습니다.");
                }
                else {
                    //registerSecondeFragment로 화면 전환
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    RegisterSecondFragment registerSecondFragment = new RegisterSecondFragment(fragmentRegisterBinding.registerIDText.getText().toString(), fragmentRegisterBinding.registerPasswordText.getText().toString());
                    fragmentManager.beginTransaction().replace(R.id.register_Frame_Layout, registerSecondFragment).commit();
                }
            }
        });
        fragmentRegisterBinding.cancleRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        fragmentRegisterBinding.registerIDText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkID = false; //중복 확인 후 아이디 값을 변경 시 변경된 값으로 다시 중복 확인 하도록 함.
                fragmentRegisterBinding.registerIDTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fragmentRegisterBinding.registerIDText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b && !checkID){ //ID텍스트 창에서 포커스 뺏겼을 때 && 중복 확인을 하지 않았을 때 중복확인을 요청 함.
                    fragmentRegisterBinding.registerIDTextLayout.setError("아이디 중복 확인을 해 주세요.");
                }
            }
        });

        fragmentRegisterBinding.registerPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentRegisterBinding.registerPasswordTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fragmentRegisterBinding.registerRepeatPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentRegisterBinding.registerRepeatPasswordTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentRegisterBinding = null;
    }
}