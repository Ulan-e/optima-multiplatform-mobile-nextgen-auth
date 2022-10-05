package kz.optimabank.optima24.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomViewHolder> {

    private final List<Object> mObjectList;
    private final OnItemClickListener mOnItemClickListener;

    public CustomListAdapter(List<Object> objects, OnItemClickListener onItemClickListener) {
        mObjectList = objects;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public CustomListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomListAdapter.CustomViewHolder viewHolder, int i) {
        Object object = mObjectList.get(i);
        viewHolder.tvTitle.setText(object.toString());
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;

        public CustomViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }
}
