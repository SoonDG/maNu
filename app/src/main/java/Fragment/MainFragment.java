package Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.databinding.FragmentMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Adapter.EatFoodAdapter;
import Interface.EatFoodDelete;
import Model.Food;
import Request.EatFoodRequest;

public class MainFragment extends Fragment implements EatFoodDelete {

    private FragmentMainBinding fragmentMainBinding;

    private EatFoodAdapter eatFoodAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Food> arrayList;

    //오늘 유저가 먹은 음식의 영양분의 합을 저장할 변수들
    private double sum_kcal = 0, sum_carbs = 0, sum_protein = 0, sum_fat = 0, sum_sugars = 0, sum_sodium = 0, sum_CH = 0, sum_Sat_fat = 0, sum_trans_fat = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        View view = fragmentMainBinding.getRoot();

        fragmentMainBinding.mainRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentMainBinding.mainRecyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>(); //데이터 베이스에서 먹은 음식 테이블로 부터 유저id, 날짜를 통해 오늘 먹은 음식을 가져와 담음
        eatFoodAdapter = new EatFoodAdapter(arrayList, this);
        fragmentMainBinding.mainRecyclerView.setAdapter(eatFoodAdapter);

        set_Food_list();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMainBinding = null;
    }

    public void set_Food_list(){ //해당 부분 수정 필요 -> 먹은 음식 테이블에서 가져오도록
        sum_kcal = 0; sum_carbs = 0; sum_protein = 0; sum_fat = 0; sum_sugars = 0; sum_sodium = 0; sum_CH = 0; sum_Sat_fat = 0; sum_trans_fat = 0;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int serving = jsonObject.getInt("serving");
                        String food_code = jsonObject.getString("food_code");
                        String food_name = jsonObject.getString("food_name");
                        double food_kcal = jsonObject.getDouble("food_kcal");
                        int food_size = jsonObject.getInt("food_size");
                        double food_carbs = jsonObject.getDouble("food_carbs");
                        double food_protein = jsonObject.getDouble("food_protein");
                        double food_fat = jsonObject.getDouble("food_fat");
                        double food_sugars = jsonObject.getDouble("food_sugars");
                        double food_sodium = jsonObject.getDouble("food_sodium");
                        double food_CH = jsonObject.getDouble("food_CH");
                        double food_Sat_fat = jsonObject.getDouble("food_Sat_fat");
                        double food_trans_fat = jsonObject.getDouble("food_trans_fat");
                        arrayList.add(new Food(serving, food_code, food_name, food_kcal, food_size, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat));
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

                    eatFoodAdapter.notifyDataSetChanged(); //리스트의 변경 내용을 리스트 뷰에 반영
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Long eat_date = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        EatFoodRequest eatfoodRequest = new EatFoodRequest("test", format.format(eat_date), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(eatfoodRequest);
    }

    @Override
    public void EatFoodDelete(int serving, double food_kcal, double food_carbs, double food_protein, double food_fat, double food_sugars, double food_sodium, double food_CH, double food_Sat_fat, double food_trans_fat) {
        sum_kcal -= (food_kcal * serving);
        sum_carbs -= (food_carbs * serving);
        sum_protein -= (food_protein * serving);
        sum_fat -= (food_fat * serving);
        sum_sugars -= (food_sugars * serving);
        sum_sodium -= (food_sodium * serving);
        sum_CH -= (food_CH * serving);
        sum_Sat_fat -= (food_Sat_fat * serving);
        sum_trans_fat -= (food_trans_fat * serving);
        if(sum_kcal < 0) sum_kcal = 0;
        if(sum_carbs < 0) sum_carbs = 0;
        if(sum_protein < 0) sum_protein = 0;
        if(sum_fat < 0) sum_fat = 0;
        if(sum_sugars < 0) sum_sugars = 0;
        if(sum_sodium < 0) sum_sodium = 0;
        if(sum_CH < 0) sum_CH = 0;
        if(sum_Sat_fat < 0) sum_Sat_fat = 0;
        if(sum_trans_fat < 0) sum_trans_fat = 0;
        set_My_Nu_Val(); //삭제된 음식의 영양분 정보를 표에 반영
    }

    public void set_My_Nu_Val(){
        fragmentMainBinding.myKcalVal.setText(String.format("%.2f(kcal)", sum_kcal));
        fragmentMainBinding.myCarbsVal.setText(String.format("%.2f(g)", sum_carbs));
        fragmentMainBinding.myProteinVal.setText(String.format("%.2f(g)", sum_protein));
        fragmentMainBinding.myFatVal.setText(String.format("%.2f(g)", sum_fat));
        fragmentMainBinding.mySugarsVal.setText(String.format("%.2f(g)", sum_sugars));
        fragmentMainBinding.mySodiumVal.setText(String.format("%.2f(mg)", sum_sodium));
        fragmentMainBinding.myCHVal.setText(String.format("%.2f(mg)", sum_CH));
        fragmentMainBinding.mySatFatVal.setText(String.format("%.2f(g)", sum_Sat_fat));
        fragmentMainBinding.myTransFatVal.setText(String.format("%.2f(g)", sum_trans_fat));
    }
}