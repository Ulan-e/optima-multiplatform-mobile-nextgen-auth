package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.utility.Utilities.doubleFormatter;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.payment.invoice.FixedInvoiceFragment;
import kz.optimabank.optima24.model.base.InvoiceContainerItem;

/**
  Created by Timur on 27.07.2017.
 */

public class FixedInvoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<InvoiceContainerItem.BodyItem> data;
    private final FixedInvoiceFragment fragment;
    private boolean isCheckedCheckBox = false;

    public FixedInvoiceAdapter(Context context, ArrayList<InvoiceContainerItem.BodyItem> data, FixedInvoiceFragment fragment) {
        this.context = context;
        this.data = data;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fixed_invoice_item, parent, false);
        return new VHItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        if(data!=null && !data.isEmpty()) {
            final InvoiceContainerItem.BodyItem item = data.get(position);
            if(item!=null) {
                if(mHolder instanceof VHItem) {
                    final VHItem holder = (VHItem) mHolder;
                    holder.tvName.setText(item.getServiceName());
                    if(item.getMeasure()!=null) {
                        holder.tvMeasure.setText(String.format("%s %s", item.getMinTariffValue(), item.getMeasure()));
                    } else {
                        holder.tvMeasure.setVisibility(View.GONE);
                    }
                        holder.tvServiceAmount.setText(getFormattedBalance(Double.valueOf(item.getCalc()), "KGS"));
                    Log.i("item.getServiceName()", "item.getServiceName() = " + item.getServiceName());
                    Log.i("item.getFixSum()","item.getFixSum() = "+item.getFixSum());
                    Log.i("item.getDebtInfo()","item.getDebtInfo() = "+item.getDebtInfo());
                    Log.i("item.getServiceSum()","item.getServiceSum() = "+item.getServiceSum());

                    Log.i("isPay","holder isPay = "+item.isPay());

                    holder.isPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            item.isPay(isChecked);
                            fragment.updateTotalSum();
                            Log.i("isPayCHECK","isChecked = "+isChecked);
                            if (isChecked) {
                                holder.isPenalty.setClickable(true);
                                holder.cbDebt.setClickable(true);
                                Log.i("isPayCHECK","isChecked ==== true");
                                if (holder.cbDebt.isChecked()) {
                                    //if (item.getDebtInfo()>0){
                                    //    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc())-Math.abs(item.getDebtInfo())));
                                    //}else {
                                    //    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc())+Math.abs(item.getDebtInfo())));
                                    //}
                                    if (holder.isPenalty.isChecked()){
                                        holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo()) + item.getPenalty()));
                                        setServSum(item, doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo()) + item.getPenalty()));
                                        item.setUsePenalty(true);
                                        item.setNotUseDebtForFixInvoice(false);
                                    } else {
                                        holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo())));
                                        setServSum(item, doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo())));
                                        item.setNotUseDebtForFixInvoice(false);
                                    }
                                } else {
                                    if (holder.isPenalty.isChecked()){
                                        holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) + item.getPenalty()));
                                        setServSum(item, doubleFormatter(Double.valueOf(item.getCalc())  + item.getPenalty()));
                                        item.setUsePenalty(true);
                                        item.setNotUseDebtForFixInvoice(true);
                                    }
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc())));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc())));
                                    item.setNotUseDebtForFixInvoice(true);
                                }
                            }else{
                                holder.cbDebt.setClickable(false);
                                holder.isPenalty.setClickable(false);
                                holder.cbDebt.setChecked(false);
                                holder.isPenalty.setChecked(false);
                                Log.i("isPayCHECK","isChecked ==== false");
                                holder.edAmount.setText("0.00");
                                setServSum(item, "0");
                            }
                        }
                    });

                    if(item.getDebtInfo()!=0) {
                        holder.tvDebt.setText(getFormattedBalance(item.getDebtInfo(), "KGS"));
                        if(item.getDebtInfo() < 0) {
                            holder.cbDebt.setText(context.getString(R.string.debt));
                        } else if(item.getDebtInfo() > 0) {
                            holder.cbDebt.setText(context.getString(R.string.overpayment));
                        }
                    } else {
                        holder.linDebt.setVisibility(View.GONE);
                    }

                    if (Double.valueOf(item.getCalc())==0 && item.getDebtInfo()>0){
                        holder.cbDebt.setChecked(false);
                        holder.linDebt.setVisibility(View.GONE);
                    }
                    if (Double.valueOf(item.getCalc())-item.getDebtInfo()<0){
                        holder.edAmount.setText("0");
                        setServSum(item, "0");
                    }
                    holder.cbDebt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            isCheckedCheckBox = true;
                            item.setServiceSum(-1000);
                            if(isChecked) {
                                if (holder.isPenalty.isChecked()){
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo()) + item.getPenalty()));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo()) + item.getPenalty()));
                                    item.setNotUseDebtForFixInvoice(false);
                                } else {
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo())));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo())));
                                    item.setNotUseDebtForFixInvoice(false);
                                }
                            } else {
                                if (holder.isPenalty.isChecked()){
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) + item.getPenalty()));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc()) + item.getPenalty()));
                                    item.setNotUseDebtForFixInvoice(false);
                                } else {
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc())));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc())));
                                    item.setNotUseDebtForFixInvoice(true);
                                }
                            }
                            fragment.updateTotalSum();
                            isCheckedCheckBox = false;
                        }
                    });

                    holder.isPenalty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            isCheckedCheckBox = true;
                            item.setServiceSum(-1000);
                            if(isChecked) {
                                if (holder.cbDebt.isChecked()){
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo()) + item.getPenalty()));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo()) + item.getPenalty()));
                                    item.setNotUseDebtForFixInvoice(false);
                                }else {
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) + item.getPenalty()));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc())  + item.getPenalty()));
                                }
                                item.setUsePenalty(true);
                            } else {
                                if (holder.cbDebt.isChecked()){
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo())));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc()) - (item.getDebtInfo())));
                                    item.setNotUseDebtForFixInvoice(false);
                                }else {
                                    holder.edAmount.setText(doubleFormatter(Double.valueOf(item.getCalc())));
                                    setServSum(item, doubleFormatter(Double.valueOf(item.getCalc())));
                                }
                                item.setUsePenalty(false);
                            }
                            fragment.updateTotalSum();
                            isCheckedCheckBox = false;
                        }
                    });

                    holder.isPay.setChecked(item.isPay());
                    if (item.getPenalty()>0){
                        holder.linPenalty.setVisibility(View.VISIBLE);
                        holder.tvPenalty.setText(String.valueOf(item.getPenalty()));
                        holder.isPenalty.setChecked(true);
                    } else {
                        holder.linPenalty.setVisibility(View.GONE);
                        holder.isPenalty.setChecked(false);
                    }
                }
            }
        }
    }

    private void setServSum(InvoiceContainerItem.BodyItem item, String sum){
        item.setServiceSum(Double.parseDouble(sum.replace(" ", "")));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHItem extends RecyclerView.ViewHolder  {
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvMeasure) TextView tvMeasure;
        @BindView(R.id.tvServiceAmount) TextView tvServiceAmount;
        @BindView(R.id.edAmount) EditText edAmount;

        //debt
        @BindView(R.id.linDebt) LinearLayout linDebt;
        @BindView(R.id.linPenalty) LinearLayout linPenalty;
        @BindView(R.id.cbDebt) CheckBox cbDebt;
        @BindView(R.id.isPay) CheckBox isPay;
        @BindView(R.id.isPenalty) CheckBox isPenalty;
        @BindView(R.id.tvDebt) TextView tvDebt;
        @BindView(R.id.tvPenalty) TextView tvPenalty;

        public VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
