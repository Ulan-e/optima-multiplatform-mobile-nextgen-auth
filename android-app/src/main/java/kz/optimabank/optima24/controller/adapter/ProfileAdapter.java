package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;

/**
  Created by Timur on 03.07.2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    ArrayList<String> data;
    Context context;
    private final OnItemClickListener mOnItemClickListener;

    public ProfileAdapter(Context context, ArrayList<String> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(data!=null&&!data.isEmpty()) {
            String item = data.get(position);
            holder.tvTitle.setText(item);

            if(item.equals(context.getString(R.string.personal_data))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user));
            } else if(item.equals(context.getString(R.string.notice))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_notifications));
            } else if(item.equals(context.getString(R.string.applications))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_request));
            } else if(item.equals(context.getString(R.string.settings_title))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_settings));
            } else if(item.equals(context.getString(R.string.contact_bank))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_phonecall));
            } else if(item.equals(context.getString(R.string.exiting_application))) {
                holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_exit));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.imgItem) ImageView imgItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view,getAdapterPosition());
                }
            });
        }
    }
}
