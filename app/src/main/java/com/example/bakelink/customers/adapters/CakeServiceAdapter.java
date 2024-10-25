package com.example.bakelink.customers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CakeServiceAdapter extends RecyclerView.Adapter<CakeServiceAdapter.CakeServiceViewHolder>{

    private List<String> cakeServiceList;

    public CakeServiceAdapter(List<String> cakeServiceList) {
        this.cakeServiceList = cakeServiceList;
    }

    @NonNull
    @Override
    public CakeServiceAdapter.CakeServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new CakeServiceAdapter.CakeServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeServiceAdapter.CakeServiceViewHolder holder, int position) {
        holder.cakeSercviceName.setText(cakeServiceList.get(position));
    }

    @Override
    public int getItemCount() {
        return cakeServiceList.size();
    }

    public class CakeServiceViewHolder extends RecyclerView.ViewHolder {

        ImageView serviceCakeImg;
        TextView cakeSercviceName;
        public CakeServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceCakeImg = itemView.findViewById(R.id.serviceCakeImg);
            cakeSercviceName = itemView.findViewById(R.id.serviceText);
        }
    }
}
