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
import kz.optimabank.optima24.model.ServiceModel;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder> {
    ArrayList<ServiceModel> serviceModels;

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
        holder.onBind(serviceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    public void setServiceData(ArrayList<ServiceModel> serviceModels) {
        this.serviceModels = serviceModels;
        notifyDataSetChanged();
    }

    public class ServiceHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.service_image);
            title = itemView.findViewById(R.id.service_title);
        }

        public void onBind(ServiceModel serviceModel) {
            title.setText(serviceModel.getTitle());
            Picasso.with(image.getContext()).load(serviceModel.getImage()).into(image);
        }
    }
}
