package Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.FragmentRegisterSecondBinding;

import org.json.JSONException;
import org.json.JSONObject;

import Activity.PopupActivity.PopupInformationActivity;
import Request.RegisterRequest;

public class RegisterSecondFragment extends Fragment {
    private FragmentRegisterSecondBinding fragmentRegisterSecondBinding;
    private String ID, Password;
    public RegisterSecondFragment(String ID, String Password) {
        this.ID = ID;
        this.Password = Password;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentRegisterSecondBinding = FragmentRegisterSecondBinding.inflate(getLayoutInflater());
        View view = fragmentRegisterSecondBinding.getRoot();

        String [] age_Data = getResources().getStringArray(R.array.age);
        String [] gen_Data = getResources().getStringArray(R.array.gender);
        String [] act_Data = getResources().getStringArray(R.array.Activity);
        ArrayAdapter ageAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, age_Data) {
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) return false;
                else return true;
            }
        };
        ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, gen_Data){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) return false;
                else return true;
            }
        };;
        ArrayAdapter activityAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, act_Data){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) return false;
                else return true;
            }
        };;

        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) { //나이트 모드라면
            fragmentRegisterSecondBinding.registerSecondTitle.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_textview_style2));

            fragmentRegisterSecondBinding.ageSpinner.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_spinner_style));
            fragmentRegisterSecondBinding.genderSpinner.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_spinner_style));

            fragmentRegisterSecondBinding.registerHeightTextLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));
            fragmentRegisterSecondBinding.registerHeightTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));

            fragmentRegisterSecondBinding.registerWeightTextLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));
            fragmentRegisterSecondBinding.registerWeightTextLayout.setBoxStrokeColorStateList(ContextCompat.getColorStateList(getContext(), R.color.night_textinputlayout_color));

            fragmentRegisterSecondBinding.registerBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_button_style2));

            ageAdapter = new ArrayAdapter(getContext(), R.layout.night_spinner_item, age_Data){
                @Override
                public boolean isEnabled(int position) {
                    if(position == 0) return false;
                    else return true;
                }
            };
            genderAdapter = new ArrayAdapter(getContext(), R.layout.night_spinner_item, gen_Data){
                @Override
                public boolean isEnabled(int position) {
                    if(position == 0) return false;
                    else return true;
                }
            };
            activityAdapter = new ArrayAdapter(getContext(), R.layout.night_spinner_item, act_Data){
                @Override
                public boolean isEnabled(int position) {
                    if(position == 0) return false;
                    else return true;
                }
            };
        }

        fragmentRegisterSecondBinding.ageSpinner.setAdapter(ageAdapter);
        fragmentRegisterSecondBinding.genderSpinner.setAdapter(genderAdapter);
        fragmentRegisterSecondBinding.activitySpinner.setAdapter(activityAdapter);

        fragmentRegisterSecondBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentRegisterSecondBinding.ageSpinner.getSelectedItemPosition() == 0){
                    Intent intent1 = new Intent(getContext(), PopupInformationActivity.class);
                    intent1.putExtra("Contents", "정확한 나이를 선택 해 주세요.");
                    startActivity(intent1);
                }
                else if(fragmentRegisterSecondBinding.genderSpinner.getSelectedItemPosition() == 0){
                    Intent intent1 = new Intent(getContext(), PopupInformationActivity.class);
                    intent1.putExtra("Contents", "정확한 성별을 선택 해 주세요.");
                    startActivity(intent1);
                }
                else if(fragmentRegisterSecondBinding.activitySpinner.getSelectedItemPosition() == 0){
                    Intent intent1 = new Intent(getContext(), PopupInformationActivity.class);
                    intent1.putExtra("Contents", "정확한 활동량을 선택 해 주세요.");
                    startActivity(intent1);
                }
                else if(fragmentRegisterSecondBinding.registerHeightText.getText().toString().length() == 0){
                    fragmentRegisterSecondBinding.registerHeightTextLayout.setError("값을 입력해 주세요.");
                }
                else if(fragmentRegisterSecondBinding.registerHeightText.getText().toString().equals(".")){
                    fragmentRegisterSecondBinding.registerHeightTextLayout.setError("유효한 값을 입력해 주세요.");
                }
                else if(fragmentRegisterSecondBinding.registerWeightText.getText().toString().length() == 0l){
                    fragmentRegisterSecondBinding.registerWeightTextLayout.setError("값을 입력해 주세요.");
                }
                else if(fragmentRegisterSecondBinding.registerWeightText.getText().toString().equals(".")){
                    fragmentRegisterSecondBinding.registerWeightTextLayout.setError("유효한 값을 입력해 주세요.");
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");

                                Intent intent = new Intent(getContext(), PopupInformationActivity.class);
                                if (success == 0) {
                                    intent.putExtra("Contents", "회원가입 성공");
                                    startActivity(intent);
                                    getActivity().finish(); //회원가입 창 닫고 로그인 창으로 이동
                                } else if (success == 1) {
                                    intent.putExtra("Contents", "데이터 전송 실패");
                                    startActivity(intent);
                                } else if (success == 2) {
                                    intent.putExtra("Contents", "sql문 실행 실패");
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    int Age = Integer.parseInt(fragmentRegisterSecondBinding.ageSpinner.getSelectedItem().toString());
                    String Gender = fragmentRegisterSecondBinding.genderSpinner.getSelectedItem().toString();
                    double Height = Double.parseDouble(fragmentRegisterSecondBinding.registerHeightText.getText().toString());
                    double Weight = Double.parseDouble(fragmentRegisterSecondBinding.registerWeightText.getText().toString());
                    int Activity = fragmentRegisterSecondBinding.activitySpinner.getSelectedItemPosition();

                    RegisterRequest registerRequest = new RegisterRequest(ID, Password, Age, Gender, Height, Weight, Activity, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(registerRequest);
                }
            }
        });

        fragmentRegisterSecondBinding.registerHeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentRegisterSecondBinding.registerHeightTextLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        fragmentRegisterSecondBinding.registerWeightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragmentRegisterSecondBinding.registerWeightTextLayout.setError(null);
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
        fragmentRegisterSecondBinding = null;
    }
}