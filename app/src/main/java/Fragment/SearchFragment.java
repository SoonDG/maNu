package Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.FragmentSearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Activity.LoginActivity;
import Activity.MainActivity;
import Adapter.FoodAdapter;
import Model.Food;
import Request.FoodRequest;
import Request.LoginRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private FragmentSearchBinding fragmentSearchBinding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FoodAdapter foodAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<Food> arrayList;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = fragmentSearchBinding.getRoot();
        // Inflate the layout for this fragment

        fragmentSearchBinding.searchRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentSearchBinding.searchRecyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        foodAdapter = new FoodAdapter(arrayList);
        fragmentSearchBinding.searchRecyclerView.setAdapter(foodAdapter);

        fragmentSearchBinding.SearchFoodText.setOnEditorActionListener(new TextView.OnEditorActionListener() { //검색 버튼을 누를경우 해당 검색어로 검색된 식품만 출력
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String Search_String = textView.getText().toString();
                if(!Search_String.isEmpty()) set_Food_list(Search_String);
                else set_Food_list("");
                return true;
            }
        });

        set_Food_list("");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentSearchBinding = null; //프래그먼트는 뷰보다 오래 지속되므로 결합 클래스 인스턴스 참조를 정리
    }

    public void set_Food_list(String Search_String){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                        arrayList.add(new Food(food_name, food_kcal, food_size, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat));
                    }
                    foodAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        FoodRequest foodRequest;
        if(!Search_String.isEmpty()) foodRequest = new FoodRequest(Search_String, responseListener); //검색어가 있다면 해당 검색어에 해당하는 목록만 가져오기
        else foodRequest = new FoodRequest((responseListener));
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(foodRequest);
    }
}