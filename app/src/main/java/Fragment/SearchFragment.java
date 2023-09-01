package Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.databinding.FragmentSearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Activity.PopupActivity.PopupFoodEatActivity;
import Adapter.FoodAdapter;
import Interface.ListItemClickInterface;
import Model.Food;
import Request.GetFoodRequest;

public class SearchFragment extends Fragment implements ListItemClickInterface {

    private FragmentSearchBinding fragmentSearchBinding;

    private FoodAdapter foodAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Food> arrayList;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        foodAdapter = new FoodAdapter(arrayList, this);
        fragmentSearchBinding.searchRecyclerView.setAdapter(foodAdapter);

        fragmentSearchBinding.searchFoodText.setOnEditorActionListener(new TextView.OnEditorActionListener() { //검색 버튼을 누를경우 해당 검색어로 검색된 식품만 출력
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

    @Override
    public void onItemClick(View v, int position) { //RecyclerView의 ItemView 클릭 이벤트
        Intent intent = new Intent(getContext(), PopupFoodEatActivity.class);
        intent.putExtra("food_code", arrayList.get(position).getFood_code());
        intent.putExtra("food_name", arrayList.get(position).getFood_name());
        startActivity(intent);
    }

    public void set_Food_list(String Search_String){ //음식 정보를 데이터베이스에서 가져와서 arrayList에 담고 RecyclerView에 반영시키는 함수
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int success = jsonArray.getJSONObject(0).getInt("success");
                    if(success == 0) {
                        for (int i = 1; i < jsonArray.length(); i++) {
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
                        }
                        foodAdapter.notifyDataSetChanged();
                    }
                    else if(success == 2){
                        Toast.makeText(getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        GetFoodRequest getFoodRequest = new GetFoodRequest(Search_String, responseListener); //검색어가 있다면 해당 검색어에 해당하는 목록만 가져오기
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getFoodRequest);
    }
}