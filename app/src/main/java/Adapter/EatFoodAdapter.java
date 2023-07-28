package Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.RecyclerviewItemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Model.Food;
import Request.EatFoodRequest;

public class EatFoodAdapter extends RecyclerView.Adapter<EatFoodAdapter.ViewHolder>{
    private ArrayList<Food> arrayList;

    public EatFoodAdapter(ArrayList<Food> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public EatFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EatFoodAdapter.ViewHolder holder, int position) {
        holder.food_code = arrayList.get(position).getFood_code();
        holder.food_name.setText(arrayList.get(position).getFood_name());
        holder.food_kcal.setText(arrayList.get(position).getFood_kcal() + "(kcal)");
        holder.food_size.setText(arrayList.get(position).getFood_size() + "(g)");
        holder.food_carbs.setText(arrayList.get(position).getFood_carbs() + "(g)");
        holder.food_protein.setText(arrayList.get(position).getFood_protein() + "(g)");
        holder.food_fat.setText(arrayList.get(position).getFood_fat() + "(g)");
        holder.food_sugars.setText(arrayList.get(position).getFood_sugars() + "(g)");
        holder.food_sodium.setText(arrayList.get(position).getFood_sodium() + "(mg)");
        holder.food_CH.setText(arrayList.get(position).getFood_CH() + "(mg)");
        holder.food_Sat_fat.setText(arrayList.get(position).getFood_Sat_fat() + "(g)");
        holder.food_trans_fat.setText(arrayList.get(position).getFood_trans_fat() + "(g)");

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() { //클릭했을 때 먹은 음식 정보를 수정할 수 있는 기능 추가
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                ad.setMessage(holder.food_name.getText() + "정보를 수정하시겠습니까?");

                ad.setPositiveButton("삭제", new DialogInterface.OnClickListener() { //음식 먹은 갯수를 입력하도록 변경해야 함
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //데이터 베이스에 먹은 음식에 추가
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        Toast.makeText(view.getContext(), "먹은 음식에서 제거", Toast.LENGTH_SHORT);
                                        notifyDataSetChanged();
                                        return;
                                    }
                                    else {
                                        Toast.makeText(view.getContext(), "먹은 음식에서 제거 실패", Toast.LENGTH_SHORT);
                                        return;
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };

                        Long eat_date = System.currentTimeMillis();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        EatFoodRequest eatFoodRequest = new EatFoodRequest("test", format.format(eat_date), holder.food_code, 1, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(view.getContext());
                        queue.add(eatFoodRequest);

                        dialogInterface.dismiss();
                    }
                });

                ad.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                ad.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView food_name, food_kcal, food_size, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat;
        protected String food_code;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            RecyclerviewItemBinding itemBinding = RecyclerviewItemBinding.bind(itemView);
            this.food_name = itemBinding.foodName;
            this.food_kcal = itemBinding.foodKcal;
            this.food_size = itemBinding.foodSize;
            this.food_carbs = itemBinding.foodCarbs;
            this.food_protein = itemBinding.foodProtein;
            this.food_fat = itemBinding.foodFat;
            this.food_sugars = itemBinding.foodSugars;
            this.food_sodium = itemBinding.foodSodium;
            this.food_CH = itemBinding.foodCH;
            this.food_Sat_fat = itemBinding.foodSatFat;
            this.food_trans_fat = itemBinding.foodTransFat;
        }
    }
}

