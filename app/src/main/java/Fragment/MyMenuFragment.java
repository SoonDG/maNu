package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.databinding.FragmentMyMenuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import Request.EatFoodRequest;

public class MyMenuFragment extends Fragment {
    private FragmentMyMenuBinding fragmentMyMenuBinding;
    private double sum_kcal = 0, sum_carbs = 0, sum_protein = 0, sum_fat = 0, sum_sugars = 0, sum_sodium = 0, sum_CH = 0, sum_Sat_fat = 0, sum_trans_fat = 0;

    public MyMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMyMenuBinding = FragmentMyMenuBinding.inflate(inflater, container, false);
        View view = fragmentMyMenuBinding.getRoot();

        Long eat_date = System.currentTimeMillis(); //처음 프래그먼트를 띄울 때 오늘 날짜에 해당하는 정보를 출력시키기 위한 변수
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        set_date(format.format(eat_date));
        fragmentMyMenuBinding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String eat_date = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
                set_date(eat_date);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMyMenuBinding = null; //프래그먼트는 뷰보다 오래 지속되므로 결합 클래스 인스턴스 참조를 정리
    }

    public void set_date(String eat_date){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if(success == 0) {
                        sum_kcal = 0; sum_carbs = 0; sum_protein = 0; sum_fat = 0; sum_sugars = 0; sum_sodium = 0; sum_CH = 0; sum_Sat_fat = 0; sum_trans_fat = 0;
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int serving = jsonObject.getInt("serving");
                            double food_kcal = jsonObject.getDouble("food_kcal");
                            double food_carbs = jsonObject.getDouble("food_carbs");
                            double food_protein = jsonObject.getDouble("food_protein");
                            double food_fat = jsonObject.getDouble("food_fat");
                            double food_sugars = jsonObject.getDouble("food_sugars");
                            double food_sodium = jsonObject.getDouble("food_sodium");
                            double food_CH = jsonObject.getDouble("food_CH");
                            double food_Sat_fat = jsonObject.getDouble("food_Sat_fat");
                            double food_trans_fat = jsonObject.getDouble("food_trans_fat");
                            sum_kcal += food_kcal * serving;
                            sum_carbs += food_carbs * serving;
                            sum_protein += food_protein * serving;
                            sum_fat += food_fat * serving;
                            sum_sugars += food_sugars * serving;
                            sum_sodium += food_sodium * serving;
                            sum_CH += food_CH * serving;
                            sum_Sat_fat += food_Sat_fat * serving;
                            sum_trans_fat += food_trans_fat * serving;
                        }
                        set_My_Nu_Val(); //영양분 합을 아래 표에 반영
                    }
                    else if(success == 1){
                        Toast.makeText(getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                    }
                    else if(success == 2){
                        Toast.makeText(getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        EatFoodRequest eatfoodRequest = new EatFoodRequest(sharedPreferences.getString("ID", null), eat_date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(eatfoodRequest);
    }

    public void set_My_Nu_Val(){
        fragmentMyMenuBinding.monthMyKcalVal.setText(String.format("%.2f(kcal)", sum_kcal));
        fragmentMyMenuBinding.monthMyCarbsVal.setText(String.format("%.2f(g)", sum_carbs));
        fragmentMyMenuBinding.monthMyProteinVal.setText(String.format("%.2f(g)", sum_protein));
        fragmentMyMenuBinding.monthMyFatVal.setText(String.format("%.2f(g)", sum_fat));
        fragmentMyMenuBinding.monthMySugarsVal.setText(String.format("%.2f(g)", sum_sugars));
        fragmentMyMenuBinding.monthMySodiumVal.setText(String.format("%.2f(mg)", sum_sodium));
        fragmentMyMenuBinding.monthMyCHVal.setText(String.format("%.2f(mg)", sum_CH));
        fragmentMyMenuBinding.monthMySatFatVal.setText(String.format("%.2f(g)", sum_Sat_fat));
        fragmentMyMenuBinding.monthMyTransFatVal.setText(String.format("%.2f(g)", sum_trans_fat));
    }

}