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

import com.example.my_first_project.R;

import java.util.ArrayList;

import Model.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{
    private ArrayList<Food> arrayList;

    public FoodAdapter(ArrayList<Food> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
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

                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //데이터 베이스에 먹은 음식에 추가

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
       protected TextView food_name, food_kcal, food_size, food_carbs, food_protein, food_fat, food_sugars, food_sodium, food_CH, food_Sat_fat, food_trans_fat;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            this.food_name = (TextView) itemView.findViewById(R.id.food_name);
            this.food_kcal = (TextView) itemView.findViewById(R.id.food_kcal);
            this.food_size = (TextView) itemView.findViewById(R.id.food_size);
            this.food_carbs = (TextView) itemView.findViewById(R.id.food_carbs);
            this.food_protein = (TextView) itemView.findViewById(R.id.food_protein);
            this.food_fat = (TextView) itemView.findViewById(R.id.food_fat);
            this.food_sugars = (TextView) itemView.findViewById(R.id.food_sugars);
            this.food_sodium = (TextView) itemView.findViewById(R.id.food_sodium);
            this.food_CH = (TextView) itemView.findViewById(R.id.food_CH);
            this.food_Sat_fat = (TextView) itemView.findViewById(R.id.food_Sat_Fat);
            this.food_trans_fat = (TextView) itemView.findViewById(R.id.food_trans_fat);
        }
    }
}

