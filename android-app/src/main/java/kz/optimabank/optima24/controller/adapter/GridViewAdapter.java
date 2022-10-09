package kz.optimabank.optima24.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.InvoiceContainerItem;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;

/**
  Created by Timur on 27.04.2017.
 */

public class GridViewAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    private static final int PAYMENT_TEMPLATE = 1;
    private static final int PAYMENT_INVOICE = 2;
    ArrayList<Object> data;
    private final OnItemClickListener mOnItemClickListener;
    Context context;
    private int selectedPosition = 0;

    public GridViewAdapter(Context context, ArrayList<Object> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.mOnItemClickListener = onItemClickListener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == PAYMENT_TEMPLATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_grid_view_item, parent, false);
            viewHolder = new ViewHolder(view);
        } else if(viewType == PAYMENT_INVOICE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_text_item, parent, false);
            viewHolder = new InvoiceViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        int viewType = mHolder.getItemViewType();
        if(viewType == PAYMENT_TEMPLATE) {
            ViewHolder holder = (ViewHolder) mHolder;
            configurePaymentTemplateViewHolder(holder,position);
        } else if(viewType == PAYMENT_INVOICE) {
            InvoiceViewHolder holder = (InvoiceViewHolder) mHolder;
            configurePaymentInvoiceViewHolder(holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data!=null && !data.isEmpty()) {
            if(data.get(position) instanceof TemplatesPayment) {
                return PAYMENT_TEMPLATE;
            } else if(data.get(position) instanceof InvoiceContainerItem.BodyItem) {
                return PAYMENT_INVOICE;
            }
        }
        return -1000;
    }

    private void configurePaymentTemplateViewHolder(ViewHolder mHolder, int position) {
        if(data!=null&&!data.isEmpty()) {
            TemplatesPayment tempPayment = (TemplatesPayment) data.get(position);
            if (tempPayment != null) {
                mHolder.tvGridItem.setText(tempPayment.name);
            }
        }
    }

    private void configurePaymentInvoiceViewHolder(InvoiceViewHolder mHolder, int position) {
        if(data!=null&&!data.isEmpty()) {
            InvoiceContainerItem.BodyItem item = (InvoiceContainerItem.BodyItem) data.get(position);
            mHolder.tvGridItem.setText(item.getServiceName());
            if(selectedPosition == position) {
                mHolder.tvGridItem.setTextColor(context.getResources().getColor(R.color.black));
                mHolder.linMain.setPadding(0,0,11,0);
            } else {
                mHolder.tvGridItem.setTextColor(context.getResources().getColor(R.color.blue_0_93_186));
                mHolder.linMain.setPadding(0,0,11,11);
            }

            /*for (int i = 0;i<data.toArray().length;i++) {
                InvoiceContainerItem.BodyItem item1 = (InvoiceContainerItem.BodyItem) data.get(position);
        }*/
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvGridItem) TextView tvGridItem;

        @SuppressLint("ResourceAsColor")
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setBackgroundResource(R.drawable.rectangle_background);
            tvGridItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public class InvoiceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvGridItem) TextView tvGridItem;
        @BindView(R.id.linMain) LinearLayout linMain;

        InvoiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvGridItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!GeneralManager.getInstance().isInvoiceCheckedStatus()) {
                        notifyItemChanged(selectedPosition);
                        selectedPosition = getAdapterPosition();
                        notifyItemChanged(selectedPosition);

                        mOnItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }
}
