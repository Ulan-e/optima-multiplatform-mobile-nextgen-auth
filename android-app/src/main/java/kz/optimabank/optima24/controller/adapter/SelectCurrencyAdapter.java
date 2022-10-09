package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import kg.optima.mobile.R;

/**
  Created by Timur on 10.05.2017.
 */

public class SelectCurrencyAdapter extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    Context context;
    ArrayList<String> data = new ArrayList<>();

    public SelectCurrencyAdapter(Context context, ArrayList<String> data) {
        super(context, 0, data);
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String currency = getItem(position);
        if (convertView==null) {
            ViewHolder holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.select_currency_item, parent, false);
            holder.tvCurrency = (TextView) convertView.findViewById(R.id.tvCurrency);
            holder.imgCurrency = (ImageView) convertView.findViewById(R.id.imgCurrency);
            convertView.setTag(holder);
        }
        if(currency!=null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.tvCurrency.setText(currency);
            switch (currency) {
                case "KZT":
                    holder.tvCurrency.setText(context.getResources().getString(R.string.kazakhstan_tenge));
                    holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.kazakhstan));
                    break;
                case "USD":
                    holder.tvCurrency.setText(context.getResources().getString(R.string.american_dollar));
                    holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.united_states_of_america));
                    break;
                case "KGS":
                    holder.tvCurrency.setText(context.getResources().getString(R.string.kyrgyz_som));
                    holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.kg_flag));
                    break;
                case "EUR":
                    holder.tvCurrency.setText(context.getResources().getString(R.string.euro_));
                    holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_european_union));
                    break;
                case "RUB":
                    holder.tvCurrency.setText(context.getResources().getString(R.string.russian_ruble));
                    holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.russia));
                    break;
                case "CNY":
                    holder.tvCurrency.setText(context.getResources().getString(R.string.china_yuan));
                    holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_china));
                    break;
                case "GBP":
                    holder.tvCurrency.setText(context.getResources().getString(R.string.pound_sterling));
                    holder.imgCurrency.setImageDrawable(context.getResources().getDrawable(R.drawable.united_kingdom));
                    break;
                default:
                    holder.imgCurrency.setVisibility(View.GONE);
                    break;
            }
        } else if (!data.isEmpty()){
            ViewHolder holder = new ViewHolder();
            holder.imgCurrency.setVisibility(View.GONE);
            holder.tvCurrency.setText(data.get(position));
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView tvCurrency;
        public ImageView imgCurrency;
    }
}
