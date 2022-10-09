package kz.optimabank.optima24.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.Rate;
import kz.optimabank.optima24.utility.Constants;

/**
  Created by Timur on 03.04.2017.
 */

public class RateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_REGULAR = 1;
    private static final int TYPE_HEADER = 2;
    Context context;
    Calendar c = Calendar.getInstance();
    ArrayList<Rate> data;

    public RateAdapter(Context context,ArrayList<Rate> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_header, parent, false);
            viewHolder = new VHHEADER(view);
        } else if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_footer, parent, false);
            viewHolder = new VHFooter(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_item, parent, false);
            viewHolder = new VHItem(view);
        }
        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        Rate rate = data.get(position);
        if(rate!=null) {
            if(mHolder instanceof VHItem) {
                VHItem holder = (VHItem) mHolder;
                holder.tvCurrency.setText(rate.ForeignCurrency);
                float flBuyRate = Float.parseFloat(rate.getBuyRate().replace(",", "."));
                float flSaleRate = Float.parseFloat(rate.getSellRate().replace(",", "."));
                holder.tvBuyRate.setText(String.format("%.4f", flBuyRate));
                holder.tvSaleRate.setText(String.format("%.4f", flSaleRate));
                if(data.size()==(position+2)){
                    holder.separator.setVisibility(View.GONE);
                }

                switch (rate.ForeignCurrency) {
                    case "USD":
                        holder.imgRate.setImageDrawable(context.getResources().getDrawable(R.drawable.united_states_of_america));
                        break;
                    case "KGS":
                        holder.imgRate.setImageDrawable(context.getResources().getDrawable(R.drawable.kg_flag));
                        break;
                    case "KZT":
                        holder.imgRate.setImageDrawable(context.getResources().getDrawable(R.drawable.kazakhstan));
                        break;
                    case "EUR":
                        holder.imgRate.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_european_union));
                        break;
                    case "RUB":
                        holder.imgRate.setImageDrawable(context.getResources().getDrawable(R.drawable.russia));
                        break;
                    case "CNY":
                        holder.imgRate.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_china));
                        break;
                    case "GBP":
                        holder.imgRate.setImageDrawable(context.getResources().getDrawable(R.drawable.united_kingdom));
                        break;
                }
            } else if (mHolder instanceof VHHEADER){
                VHHEADER holder = (VHHEADER)mHolder;
                if(data.get(position).type == 0){
                    holder.tvRateHeader.setText(context.getResources().getString(R.string.pc_courses));
                } else if (data.get(position).type == 1){
                    holder.tvRateHeader.setText(context.getResources().getString(R.string.cash_courses) );
                } else if (data.get(position).type == 2){
                    holder.tvRateHeader.setText(context.getResources().getString(R.string.cashless_courses) );
                } else if (data.get(position).type == 3){
                    holder.tvRateHeader.setText(context.getResources().getString(R.string.nbrk_courses) );
                }
            }
            else {
                VHFooter holder = (VHFooter) mHolder;
                holder.tvRateFooter.setText(context.getResources().getString(R.string.rate_update_date) + "  " + data.get(1).getDate() + " "+ new SimpleDateFormat("HH:mm:ss").format(c.getTime()));//Utilities.getCurrentDate("hh:MM:ss")
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data!=null && !data.isEmpty() && data.get(position).code == Constants.FOOTER_ID) {
            return TYPE_FOOTER;
        }

        if (data!=null && !data.isEmpty() && data.get(position).code == Constants.HEADER_ID) {
            return TYPE_HEADER;
        }

        return TYPE_REGULAR;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.currency) TextView tvCurrency;
        @BindView(R.id.saleRate) TextView tvSaleRate;
        @BindView(R.id.buyRate) TextView tvBuyRate;
        @BindView(R.id.img_rate) ImageView imgRate;
        @BindView(R.id.separator) View separator;

        public VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHFooter extends RecyclerView.ViewHolder {
        @BindView(R.id.footer) TextView tvRateFooter;

        public VHFooter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class VHHEADER extends RecyclerView.ViewHolder {
        @BindView(R.id.rate_type) TextView tvRateHeader;

        public VHHEADER(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
