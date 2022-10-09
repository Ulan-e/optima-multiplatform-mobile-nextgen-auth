package kz.optimabank.optima24.controller.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;

/**
  Created by Timur on 24.04.2017.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {
    ArrayList<PaymentService> data;
    private final OnItemClickListener mOnItemClickListener;

    public ServiceListAdapter(ArrayList<PaymentService> data,OnItemClickListener onItemClickListener) {
        this.data = data;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_service_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(data!=null&&!data.isEmpty()) {
            PaymentService paymentService = data.get(position);
            if(paymentService!=null) {
                holder.tvTitle.setText(paymentService.name);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateList(ArrayList<PaymentService> list){
        data = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
            Drawable leftDrawable = AppCompatResources.getDrawable(itemView.getContext(), R.drawable.chevron_right);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
        }
    }
}
