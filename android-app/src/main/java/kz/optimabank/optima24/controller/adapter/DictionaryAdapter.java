package kz.optimabank.optima24.controller.adapter;

import android.util.Log;
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
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
    ArrayList<Object> data;
    private final OnItemClickListener mOnItemClickListener;
    private static final int DICTIONARY = 1;
    private static final int PAYMENTREGION = 2;
    private static final int FOREIGN_BANK = 3;
    private static final int COUNTRY = 4;
    private boolean isForeignBank;

    public DictionaryAdapter(ArrayList<Object> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.mOnItemClickListener = onItemClickListener;
        Log.i("data_data","data = "+data);
    }

    public DictionaryAdapter(ArrayList<Object> data, OnItemClickListener onItemClickListener, boolean isForeignBank) {
        this.data = data;
        this.isForeignBank = isForeignBank;
        this.mOnItemClickListener = onItemClickListener;
        Log.i("data_data","data = "+data);
    }

    @Override
    public DictionaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DictionaryAdapter.ViewHolder holder, int position) {
        if(data!=null&&!data.isEmpty()) {
            int viewType  = holder.getItemViewType();
            switch (viewType) {
                case DICTIONARY:
                    configureDictionaryViewHolder(holder,position);
                    break;
                case PAYMENTREGION:
                    configurePaymentRegionViewHolder(holder,position);
                    break;
                case FOREIGN_BANK:
                    configureForeignBankViewHolder(holder, position);
                    break;
                case COUNTRY:
                    configureCountryViewHolder(holder, position);
                    break;
            }

            /*if(item!=null) {
                holder.tvDictionaryDesc.setText(item.getDescription());
                if(item.getType() == 0) {
                    holder.tvDictionaryCode.setText(item.getCode() + " - ");
                } else {
                    holder.tvDictionaryCode.setVisibility(View.GONE);
                }
            }*/
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data != null && !data.isEmpty()) {
            if (data.get(position) instanceof Dictionary) {
                return DICTIONARY;
            } else if (data.get(position) instanceof PaymentRegions) {
                return PAYMENTREGION;
            } else if (isForeignBank) {
                return FOREIGN_BANK;
            } else if (data.get(position) instanceof String) {
                return COUNTRY;
            }
        }
        return -1000;
    }

    private void configureDictionaryViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        Dictionary dictionary = (Dictionary) data.get(position);
        if(dictionary!=null) {
                ViewHolder holder = (ViewHolder) mHolder;
                holder.tvDictionaryDesc.setText(dictionary.getDescription());
            Log.i("data_type","type = "+dictionary.getType());
            /*Log.i("data_code","Code = "+dictionary.getCode());
            Log.i("data_id","id = "+dictionary.getId());
            Log.i("data_code","getCustom_name = "+dictionary.getCustom_name());
            Log.i("data_description","description = "+dictionary.getDescription());*/

            if(dictionary.getType() == 0 || dictionary.getType() == 1 || dictionary.getType() == 3 || dictionary.getType() == 4 || dictionary.getType() == 7) {
                holder.tvDictionaryCode.setText(dictionary.getCode() + " - ");
            } else {
                holder.tvDictionaryCode.setVisibility(View.GONE);
            }
        }
    }

    private void configurePaymentRegionViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        PaymentRegions paymentRegions = (PaymentRegions) data.get(position);
        if(paymentRegions!=null) {
                ViewHolder holder = (ViewHolder) mHolder;
                holder.tvDictionaryDesc.setText(paymentRegions.getName());
                holder.tvDictionaryCode.setVisibility(View.GONE);
            //Log.i("payment.getExternalId()","paymentRegions.getExternalId() = "+paymentRegions.getExternalId());
            Log.i("paymentRegions.getId()","paymentRegions.getId() = "+paymentRegions.getId());
            //Log.i("getRegionID()","GeneralManager.getInstance().getRegionID() = "+GeneralManager.getInstance().getRegionID());
            if (paymentRegions.getId()== GeneralManager.getInstance().getRegionID()){
                Log.i("regionTrue","regionTrue"+paymentRegions.getId() + " = " + GeneralManager.getInstance().getRegionID());
                holder.imgIsSelected.setVisibility(View.VISIBLE);
            }else{
                holder.imgIsSelected.setVisibility(View.GONE);
            }
        }
    }

    private void configureForeignBankViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        ForeignBank foreignBank = (ForeignBank) data.get(position);
        if (foreignBank != null) {
            ViewHolder holder = (ViewHolder) mHolder;
            holder.tvDictionaryCode.setText(foreignBank.bic + " - ");
            holder.tvDictionaryDesc.setText(foreignBank.name);
        }
    }

    private void configureCountryViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        String country = (String) data.get(position);
        if (country != null) {
            ViewHolder holder = (ViewHolder) mHolder;
            holder.tvDictionaryCode.setText(country);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateList(ArrayList<Object> list){
        data = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDictionaryCode) TextView tvDictionaryCode;
        @BindView(R.id.tvDictionaryDesc) TextView tvDictionaryDesc;
        @BindView(R.id.imgIsSelected) ImageView imgIsSelected;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
