package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.my_first_project.R;
import com.example.my_first_project.databinding.RecyclerviewItemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Model.Food;
import Request.EatFoodRequest;
import Request.FoodRequest;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{
    private ArrayList<Food> arrayList;
    private SharedPreferences sharedPreferences;
    private String user_ID;

    public FoodAdapter(ArrayList<Food> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        sharedPreferences = context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        user_ID = sharedPreferences.getString("ID", null);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        holder.serving.setText(arrayList.get(position).getServing() + " 인분");
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                ad.setMessage(holder.food_name.getText() + "를 오늘 먹은 식품에 추가 하시겠습니까?");

                final Spinner spinner = new Spinner(ad.getContext());
                String [] serving_Data = ad.getContext().getResources().getStringArray(R.array.serving);
                ArrayAdapter servingAdapter = new ArrayAdapter(ad.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, serving_Data);
                spinner.setAdapter(servingAdapter);
                ad.setView(spinner);

                ad.setPositiveButton("네", new DialogInterface.OnClickListener() { //음식 먹은 갯수를 입력하도록 변경해야 함
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //데이터 베이스에 먹은 음식에 추가
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    if(success == 0){
                                        Toast.makeText(view.getContext(), holder.food_name.getText() + " " + spinner.getSelectedItem().toString() + "인분을 먹은 음식에 담았습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(success == 1){
                                        Toast.makeText(view.getContext(), "데이터 전송 실패", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(success == 2){
                                        Toast.makeText(view.getContext(), "sql문 실행 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        Long eat_date = System.currentTimeMillis();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        EatFoodRequest eatFoodRequest = new EatFoodRequest(user_ID, format.format(eat_date), Integer.parseInt(spinner.getSelectedItem().toString()), holder.food_code, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(view.getContext());
                        queue.add(eatFoodRequest);

                        dialogInterface.dismiss();
                    }
                });

                ad.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
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
       protected TextView serving, food_name, food_kcal, food_size, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat;
       protected String food_code;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            RecyclerviewItemBinding itemBinding = RecyclerviewItemBinding.bind(itemView);
            this.serving = itemBinding.serving;
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

