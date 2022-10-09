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
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

/**
  Created by Timur on 15.06.2017.
 */

public class SelectCurrencyForConvertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public ArrayList<UserAccounts.Cards.MultiBalanceList> data;
    private final OnItemClickListener onItemClickListener;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REGULAR = 1;

    public SelectCurrencyForConvertAdapter(Context context, ArrayList<UserAccounts.Cards.MultiBalanceList> data,
                                           OnItemClickListener onItemClickListener) {
        this.data = data;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
            viewHolder = new SelectCurrencyForConvertAdapter.VHHeader(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_currency_item, parent, false);
            viewHolder = new SelectCurrencyForConvertAdapter.VHItem(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        if(data!=null && !data.isEmpty()) {
            UserAccounts.Cards.MultiBalanceList balance = data.get(position);
            if(balance!= null) {
                if(mHolder instanceof SelectCurrencyForConvertAdapter.VHItem) {
                    VHItem holder = (VHItem) mHolder;
                    Utilities.setRobotoTypeFaceToTextView(context, holder.tvAmount);
                    holder.tvAmount.setText(balance.getFormattedAmount(context));
                    switch (balance.currency) {
                        case "KZT":
                            holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.kazakhstan));
                            break;
                        case "USD":
                            holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.united_states_of_america));
                            break;
                        case "KGS":
                            holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.kg_flag));
                            break;
                        case "EUR":
                            holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_european_union));
                            break;
                        case "RUB":
                            holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.russia));
                            break;
                        case "CNY":
                            holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_china));
                            break;
                        case "GBP":
                            holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.united_kingdom));
                            break;
                        default:
                            holder.imgCurrency.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    VHHeader holder = (VHHeader) mHolder;
                    holder.tvHeader.setText(balance.name);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data!=null && !data.isEmpty() && data.get(position).code == Constants.FOOTER_ID) {
            return TYPE_HEADER;
        }
        return TYPE_REGULAR;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCurrency) TextView tvAmount;
        @BindView(R.id.imgCurrency) ImageView imgCurrency;

        public VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header) TextView tvHeader;

        public VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
