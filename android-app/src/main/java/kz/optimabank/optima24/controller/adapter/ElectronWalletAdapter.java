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

public class ElectronWalletAdapter extends RecyclerView.Adapter<ElectronWalletAdapter.ElectronHolder> {
    ArrayList<InterfaceView> dataArrayList;

    @NonNull
    @Override
    public ElectronHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false);
        return new ElectronHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectronHolder holder, int position) {
        holder.onBind(dataArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }
    public void setData(InterfaceView data){
        dataArrayList.add(data);
    }

    public class ElectronHolder extends RecyclerView.ViewHolder {
        TextView wallet_bonuses_bank_tv, wallet_numbers_tv, wallet_bank_amount_tv, wallet_scores_amount_tv, wallet_header;
        ImageView wallet_card_image;

        public ElectronHolder(@NonNull View itemView) {
            super(itemView);
            wallet_header = itemView.findViewById(R.id.loyalty_header);
            wallet_bonuses_bank_tv = itemView.findViewById(R.id.bonuses_bank_tv);
            wallet_numbers_tv = itemView.findViewById(R.id.numbers_tv);
            wallet_bank_amount_tv = itemView.findViewById(R.id.bonus_amount_tv);
            wallet_scores_amount_tv = itemView.findViewById(R.id.scores_amount_tv);
            wallet_card_image = itemView.findViewById(R.id.card_image);
        }

        public void onBind(InterfaceView data) {
            wallet_header.setText(data.getCategoryName());
        }
    }
}
