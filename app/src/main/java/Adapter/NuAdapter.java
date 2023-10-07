package Adapter;

import android.content.Context;
import android.content.res.Configuration;
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
    public NuAdapter(ArrayList<Nutrients> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public NuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.nu_recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NuAdapter.ViewHolder holder, int position) {
        holder.nu_Title.setText(arrayList.get(position).getName());
        holder.nu_Amount.setText(arrayList.get(position).getAmount() + " / " + arrayList.get(position).getRec_Max_Amount());
        if(arrayList.get(position).getAmount() != 0) holder.nu_ProgressBar.setProgress((int) (arrayList.get(position).getRec_Max_Amount() / arrayList.get(position).getAmount()));
        else holder.nu_ProgressBar.setProgress(0);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView nu_Title, nu_Amount;
        protected ProgressBar nu_ProgressBar;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            NuRecyclerviewItemBinding itemBinding = NuRecyclerviewItemBinding.bind(itemView);
            nu_Title = itemBinding.nuTitle;
            nu_Amount = itemBinding.nuAmount;
            nu_ProgressBar = itemBinding.nuProgressBar;
        }
    }

}
