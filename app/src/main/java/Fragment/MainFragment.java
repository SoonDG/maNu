package Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.FragmentMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Adapter.EatFoodAdapter;
import Adapter.FoodAdapter;
import Model.Food;
import Request.EatFoodRequest;
import Request.FoodRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private FragmentMainBinding fragmentMainBinding;

    private EatFoodAdapter eatFoodAdapter;
    private LinearLayoutManager linearLayoutManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Food> arrayList;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        eatFoodAdapter = new EatFoodAdapter(arrayList);
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
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    double sum_kcal = 0, sum_carbs = 0, sum_protein = 0, sum_fat = 0, sum_sugars = 0, sum_sodium = 0, sum_CH = 0, sum_Sat_fat = 0, sum_trans_fat = 0;
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                        arrayList.add(new Food(food_code, food_name, food_kcal, food_size, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat));
                        sum_kcal += food_kcal;
                        sum_carbs += food_carbs;
                        sum_protein += food_protein;
                        sum_fat += food_fat;
                        sum_sugars += food_sugars;
                        sum_sodium += food_sodium;
                        sum_CH += food_CH;
                        sum_Sat_fat += food_Sat_fat;
                        sum_trans_fat += food_trans_fat;
                    }
                    fragmentMainBinding.myKcalVal.setText(String.format("%.2f", sum_kcal));
                    fragmentMainBinding.myCarbsVal.setText(String.format("%.2f",sum_carbs));
                    fragmentMainBinding.myProteinVal.setText(String.format("%.2f",sum_protein));
                    fragmentMainBinding.myFatVal.setText(String.format("%.2f",sum_fat));
                    fragmentMainBinding.mySugarsVal.setText(String.format("%.2f",sum_sugars));
                    fragmentMainBinding.mySodiumVal.setText(String.format("%.2f",sum_sodium));
                    fragmentMainBinding.myCHVal.setText(String.format("%.2f",sum_CH));
                    fragmentMainBinding.mySatFatVal.setText(String.format("%.2f",sum_Sat_fat));
                    fragmentMainBinding.myTransFatVal.setText(String.format("%.2f",sum_trans_fat));

                    eatFoodAdapter.notifyDataSetChanged();
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
}