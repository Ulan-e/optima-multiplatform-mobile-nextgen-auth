package kz.optimabank.optima24.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kg.optima.mobile.R;
import kz.optimabank.optima24.model.TransferServiceModel;

public class TransferServiceAdapter extends RecyclerView.Adapter<TransferServiceAdapter.TransferServiceHolder> {
    ArrayList<TransferServiceModel> serviceModels;

    @NonNull
    @Override
    public TransferServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransferServiceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransferServiceHolder holder, int position) {
        holder.onBind(serviceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    public void setServiceData(ArrayList<TransferServiceModel> serviceModels) {
        this.serviceModels = serviceModels;
        notifyDataSetChanged();
    }

    public class TransferServiceHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public TransferServiceHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.service_image);
            title = itemView.findViewById(R.id.service_title);
        }

        public void onBind(TransferServiceModel serviceModel) {
            title.setText(serviceModel.getTitle());
            Picasso.with(image.getContext()).load(serviceModel.getImage()).into(image);
        }
    }
}
