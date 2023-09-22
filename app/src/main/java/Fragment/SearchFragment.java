package Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import com.example.my_first_project.R;
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
    private int index = 0;
    private String Search_String = "";
    private boolean update_RecyclerView = true;
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

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES: //나이트 모드라면
                fragmentSearchBinding.searchFoodText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.night_edit_text_style));
                fragmentSearchBinding.searchFoodText.setHintTextColor(Color.parseColor("#ffffff"));
                break;
            case Configuration.UI_MODE_NIGHT_NO: //나이트 모드가 아니라면
                fragmentSearchBinding.searchFoodText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style));
                fragmentSearchBinding.searchFoodText.setTextColor(Color.parseColor("#212121"));
                fragmentSearchBinding.searchFoodText.setHintTextColor(Color.parseColor("#A6A6A6"));
                break;
        }

        fragmentSearchBinding.searchFoodText.setOnEditorActionListener(new TextView.OnEditorActionListener() { //검색 버튼을 누를경우 해당 검색어로 검색된 식품만 출력
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                index = 0; //새로 검색 되므로 index는 0부터 다시 시작
                Search_String = textView.getText().toString(); //검색어를 가져옴
                update_RecyclerView = true;

                set_Food_list(Search_String, 0); //검색어를 통해 데이터베이스에서 데이터를 다시 받아와서 리스트를 채움

                fragmentSearchBinding.searchRecyclerView.scrollToPosition(0); //검색을 새로 하면 스크롤을 제일 위로 올려서 확인할 수 있도록 함
                return true;
            }
        });

        fragmentSearchBinding.searchRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if(!fragmentSearchBinding.searchRecyclerView.canScrollVertically(1)){ //RecyclerView의 스크롤을 끝까지 내렸다면
                    if(update_RecyclerView) { //업데이트 할 것이 남아 있다면
                        set_Food_list(Search_String, 1); //목록을 유지한 상태에서 새로 업데이트된 index부터 20개를 받아와 채워넣기
                    }
                }
            }
        });

        set_Food_list(Search_String, 0);

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

    public void set_Food_list(String Search_String, int code){ //음식 정보를 데이터베이스에서 가져와서 arrayList에 담고 RecyclerView에 반영시키는 함수
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fragmentSearchBinding.progressBar.setVisibility(View.VISIBLE); //progressBar를 띄워 아이템을 추가 중임을 알림

                int size = 0;
                if(code == 0) arrayList.clear(); //code == 0이면 목록 초기화후 새로 목록을 제작하는 코드, code == 1이면 목록 유지하고 목록에 새로운 음식 추가
                else if(code == 1) size = arrayList.size(); //code == 1이면 목록 유지하면서 추가, 음식이 추가 됐는지 확인하기 위한 변수

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
                            //arrayList에 검색된 음식 데이터를 저장
                        }

                        index += jsonArray.length() - 1; //맨 앞의 에러 코드를 제외한 추가된 아이템의 갯수만큼 index 증가
                        foodAdapter.notifyDataSetChanged(); //arrayList의 변경 내용을 RecyclerView에 반영
                        if(code == 1){
                            if(size == arrayList.size()){
                                update_RecyclerView = false; //추가된 음식이 없으므로 앞으로 추가하는 함수 호출하지 않도록 설정
                            }
                        }

                        fragmentSearchBinding.progressBar.setVisibility(View.INVISIBLE); //추가가 완료되었으므로, progressBar 숨김
                    }
                    else if(success == 2){
                        Toast.makeText(getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        GetFoodRequest getFoodRequest = new GetFoodRequest(Search_String, index, responseListener); //검색어가 있다면 해당 검색어에 해당하는 목록만 가져오기
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(getFoodRequest);
    }
}