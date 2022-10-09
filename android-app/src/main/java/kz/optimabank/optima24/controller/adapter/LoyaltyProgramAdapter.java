package kz.optimabank.optima24.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kg.optima.mobile.R;
import kz.optimabank.optima24.model.gson.response.InterfaceView;

public class LoyaltyProgramAdapter extends RecyclerView.Adapter<LoyaltyProgramAdapter.LoyaltyHolder> {
    ArrayList<InterfaceView> dataArrayList;

    @NonNull
    @Override
    public LoyaltyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loyalty, parent, false);
        return new LoyaltyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoyaltyHolder holder, int position) {
        holder.onBind(dataArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }
    public void setData(InterfaceView data){
        dataArrayList.add(data);
    }

    public class LoyaltyHolder extends RecyclerView.ViewHolder {
        TextView bonuses_bank_tv, numbers_tv, bank_amount_tv, scores_amount_tv,header;
        ImageView card_image;

        public LoyaltyHolder(@NonNull View itemView) {
            super(itemView);
            header=itemView.findViewById(R.id.loyalty_header);
            bonuses_bank_tv = itemView.findViewById(R.id.bonuses_bank_tv);
            numbers_tv = itemView.findViewById(R.id.numbers_tv);
            bank_amount_tv = itemView.findViewById(R.id.bonus_amount_tv);
            scores_amount_tv = itemView.findViewById(R.id.scores_amount_tv);
            card_image = itemView.findViewById(R.id.card_image);
        }

        public void onBind(InterfaceView data) {
            header.setText(data.getCategoryName());
        }
    }
}
