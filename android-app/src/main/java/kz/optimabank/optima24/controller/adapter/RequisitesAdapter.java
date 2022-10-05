package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;

public class RequisitesAdapter extends RecyclerView.Adapter<RequisitesAdapter.VHItem> {
    ArrayList<String> lists;
    Context context;

    @NonNull
    @Override
    public VHItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_requisites, parent, false);
        return new VHItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHItem holder, int position) {
        holder.bind(lists.get(position));
    }

    public RequisitesAdapter(ArrayList<String> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOne)
        TextView textView;

        private VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(String s1) {
            textView.setText(s1);
        }
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

}
