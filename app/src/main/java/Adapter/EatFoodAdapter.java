package Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.RecyclerviewItemBinding;

import java.util.ArrayList;

import Interface.ListItemClickInterface;
import Model.Food;

public class EatFoodAdapter extends RecyclerView.Adapter<EatFoodAdapter.ViewHolder>{
    private ArrayList<Food> arrayList;
    private ListItemClickInterface Listener;
    public EatFoodAdapter(ArrayList<Food> arrayList, ListItemClickInterface Listener){
        this.arrayList = arrayList; //RecyclerView와 연동될 ArrayList
        this.Listener = Listener; //클릭 이벤트 리스너
    }


    @NonNull
    @Override
    public EatFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EatFoodAdapter.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        if((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){ //나이트 모드라면
            holder.line.setImageResource(R.drawable.night_line);
        }

        holder.serving.setText(arrayList.get(position).getServing() + " 인분");
        holder.food_name.setText(arrayList.get(position).getFood_name());
        holder.food_kcal.setText(String.format("%.2f(kcal)", arrayList.get(position).getFood_kcal()));
        holder.food_size.setText(arrayList.get(position).getFood_size() + "(g)");
        holder.food_carbs.setText(String.format("%.2f(g)", arrayList.get(position).getFood_carbs()));
        holder.food_protein.setText(String.format("%.2f(g)", arrayList.get(position).getFood_protein()));
        holder.food_fat.setText(String.format("%.2f(g)", arrayList.get(position).getFood_fat()));

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() { //클릭했을 때 먹은 음식 정보를 수정할 수 있는 기능 추가
            @Override
            public void onClick(View view) {
                Listener.onItemClick(view, holder.getAdapterPosition()); //RecyclerView가 부착된 쪽의 Listener의 클릭이벤트 실행, 클릭 이벤트가 호출한 쪽에 있음
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView serving, food_name, food_kcal, food_size, food_carbs, food_protein, food_fat;
        protected ImageView line;
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
            this.line = itemBinding.line;
        }
    }
}

