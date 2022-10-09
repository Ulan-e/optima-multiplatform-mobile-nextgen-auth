package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kg.optima.mobile.R;
import kg.optima.mobile.databinding.LayoutItemSelectCardLimitBinding;
import kz.optimabank.optima24.model.base.Limit;

public class SelectCardLimitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutItemSelectCardLimitBinding binding;
    private List<Limit> listOfLimits;
    private List<String> listOfLimitsString = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private Context context;

    private static final int ATM_LIMIT_CASH_LIMIT = 0;
    private static final int RETAIL_CARD_PRESENT = 2;
    private static final int CARD_NOT_PRESENT = 3;

    public SelectCardLimitAdapter(Context context, List<Limit> listOfLimits, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listOfLimits = listOfLimits;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = LayoutItemSelectCardLimitBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LimitViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (listOfLimits != null && !listOfLimits.isEmpty()) {
        String atm_limit = context.getResources().getString(R.string.ATM_LIMIT_CASH_LIMIT);
        String retail_card = context.getResources().getString(R.string.RETAIL_CARD_PRESENT);
        String card_not_present = context.getResources().getString(R.string.CARD_NOT_PRESENT);

            for (Limit limit : listOfLimits) {
                if (limit.Type == ATM_LIMIT_CASH_LIMIT && !listOfLimitsString.contains(atm_limit)) {
                    listOfLimitsString.add(atm_limit);
                } else if (limit.Type == RETAIL_CARD_PRESENT && !listOfLimitsString.contains(retail_card)) {
                    listOfLimitsString.add(retail_card);
                } else if (limit.Type == CARD_NOT_PRESENT && !listOfLimitsString.contains(card_not_present)) {
                    listOfLimitsString.add(card_not_present);
                }
            }

            binding.textViewDescription.setText(listOfLimitsString.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(listOfLimits.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listOfLimits.size();
    }

    class LimitViewHolder extends RecyclerView.ViewHolder {
        public LimitViewHolder(@NonNull LayoutItemSelectCardLimitBinding binding) {
            super(binding.getRoot());

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Limit limit, int position);
    }
}
