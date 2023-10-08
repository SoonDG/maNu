package Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;

import com.example.my_first_project.R;
import com.example.my_first_project.databinding.NuRecyclerviewItemBinding;
import com.example.my_first_project.databinding.RecyclerviewItemBinding;

import java.util.ArrayList;

import Model.Nutrients;

public class NuAdapter extends RecyclerView.Adapter<NuAdapter.ViewHolder>{
    private ArrayList<Nutrients> arrayList;
    private int [] itemViewBackgroundColors;
    public NuAdapter(ArrayList<Nutrients> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        itemViewBackgroundColors = new int[]{ContextCompat.getColor(context, R.color.MyNuRed), ContextCompat.getColor(context, R.color.MyNuOrange), ContextCompat.getColor(context, R.color.MyNuYellow), ContextCompat.getColor(context, R.color.MyNuPeagreen), ContextCompat.getColor(context, R.color.MyNuGreen), ContextCompat.getColor(context, R.color.MyNuSky), ContextCompat.getColor(context, R.color.MyNuBlue), ContextCompat.getColor(context, R.color.MyNuIndigo), ContextCompat.getColor(context, R.color.MyNuPurple)};
        View view = LayoutInflater.from(context).inflate(R.layout.nu_recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NuAdapter.ViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(itemViewBackgroundColors[position]);
        holder.nu_Title.setText(arrayList.get(position).getName());
        if(arrayList.get(position).getRec_Min_Amount() == 0.0) { //권장 최소치가 없다면 권장 최소치를 표시하지 않음.
            holder.nu_Amount.setText(String.format("%.2f", arrayList.get(position).getAmount()) + " / " + String.format("%.2f", arrayList.get(position).getRec_Max_Amount()) + arrayList.get(position).getUnit());
            double percentage = arrayList.get(position).getAmount() / arrayList.get(position).getRec_Max_Amount() * 100;
            holder.nu_ProgressBar.setProgress((int)percentage);
            if(percentage > 100){
                holder.nu_Notice.setText("※권장 최대치 초과");
            }
            else {
                holder.nu_Notice.setText("※권장량 섭취 중");
            }
        }
        else {
            holder.nu_Amount.setText(String.format("%.2f", arrayList.get(position).getRec_Min_Amount()) + " / " + String.format("%.2f", arrayList.get(position).getAmount()) + " / " + String.format("%.2f", arrayList.get(position).getRec_Max_Amount()) + arrayList.get(position).getUnit());
            double MaxPercentage = arrayList.get(position).getAmount() / arrayList.get(position).getRec_Max_Amount() * 100;
            double MinPercentage = arrayList.get(position).getRec_Min_Amount() / arrayList.get(position).getRec_Max_Amount() * 100;
            holder.nu_ProgressBar.setProgress((int)MaxPercentage); //권장 최대치에 대한 현재 먹은 영양분 백분율 값을 프로그레스 바에 표시
            holder.nu_ProgressBar.setSecondaryProgress((int)MinPercentage); //권장 최소치를 프로그레스 바에 표시
            if(MaxPercentage > 100){ //권장 최대치를 넘었을 경우
                holder.nu_Notice.setText("※권장 최대치 초과");
            }
            else if(MinPercentage > MaxPercentage){ //권장 최소치를 못 넘었을 경우
                holder.nu_Notice.setText("※권장 최소치 미달");
            }
            else {
                holder.nu_Notice.setText("※권장량 섭취 중");
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView nu_Title, nu_Notice, nu_Amount;
        protected ProgressBar nu_ProgressBar;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            NuRecyclerviewItemBinding itemBinding = NuRecyclerviewItemBinding.bind(itemView);
            nu_Title = itemBinding.nuTitle;
            nu_Notice = itemBinding.nuNotice;
            nu_Amount = itemBinding.nuAmount;
            nu_ProgressBar = itemBinding.nuProgressBar;
        }
    }

}
