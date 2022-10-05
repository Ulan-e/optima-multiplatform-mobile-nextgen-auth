package kz.optimabank.optima24.controller.adapter;

import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.setPaymentImage;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.model.base.HistoryItem;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	Context context;
	ArrayList<HistoryItem> data;
    private final PaymentContextController paymentController = PaymentContextController.getController();
	private final OnItemClickListener mOnItemClickListener;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_REGULAR = 1;

	public HistoryAdapter(Context context, ArrayList<HistoryItem> data,OnItemClickListener onItemClickListener) {
		this.data = data;
		this.context = context;
		this.mOnItemClickListener = onItemClickListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder viewHolder;
		if(viewType == TYPE_HEADER) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
			viewHolder = new VHHeader(view);
		} else {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_item, parent, false);
			viewHolder = new VHItem(view);
		}
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder mHolder, final int position) {
		HistoryItem historyItem = null;
		if(data!=null && !data.isEmpty()) {
			historyItem = data.get(position);
		}
		if(historyItem != null) {
			if (mHolder instanceof VHHeader) {
				VHHeader holder = (VHHeader) mHolder;
				if(position == 0) {
					holder.ivCalendar.setVisibility(View.VISIBLE);
				} else {
					holder.ivCalendar.setVisibility(View.GONE);
				}
				holder.ivCalendar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mOnItemClickListener.onItemClick(view, mHolder.getAdapterPosition());
					}
				});
				if(historyItem.name.equals(context.getString(R.string.not_data))){
					holder.tvName.setGravity(Gravity.CENTER);
					holder.tvName.setPadding(70,15,15,15);
				}
				holder.tvName.setText(historyItem.name);
			} else if(mHolder instanceof VHItem) {
				if(historyItem instanceof HistoryItem.PaymentHistoryItem) {
					configurePaymentHistory(mHolder, (HistoryItem.PaymentHistoryItem) historyItem);
				} else if (historyItem instanceof HistoryItem.TransferHistoryItem) {
					configureTransferHistory(mHolder, (HistoryItem.TransferHistoryItem) historyItem);
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
		if (data!=null && !data.isEmpty() && data.get(position).id == Constants.HEADER_ID) {
			return TYPE_HEADER;
		} else {
			return TYPE_REGULAR;
		}
	}

	@Override
	public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		Log.d("TAG","onDetachedFromRecyclerView");
		super.onDetachedFromRecyclerView(recyclerView);
	}

	@Override
	public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
		Log.d("TAG","onViewDetachedFromWindow");
		super.onViewDetachedFromWindow(holder);
	}

	public void closeBD() {
		if(paymentController!=null) {
			paymentController.close();
		}
	}

	private void configurePaymentHistory(RecyclerView.ViewHolder mHolder, HistoryItem.PaymentHistoryItem historyItem) {
		VHItem holder = (VHItem) mHolder;
//        holder.tvInfoReference.setVisibility(View.VISIBLE);
//        holder.tvReference.setVisibility(View.VISIBLE);

        PaymentCategory paymentCategory = null;
        holder.ivType.setVisibility(View.VISIBLE);
        PaymentService paymentService = paymentController.getPaymentServiceById(historyItem.ServiceId);
        if(paymentService!=null) {
            paymentCategory = paymentController.getPaymentCategoryByServiceId(paymentService.paymentCategoryId);
        }
        if (paymentCategory!=null && paymentCategory.getExternalId() != null) {
        	Log.i("PAYALIAS","paymentCategory.alias = "+paymentCategory.alias);
        	Log.i("PAYALIAS","paymentCategory.ID = "+paymentCategory.getId());
			setPaymentImage(context,holder.ivType,paymentCategory.alias);
        }
        if(!historyItem.success) {
			holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(historyItem.status);
            holder.ivStatusNotDone.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setVisibility(View.GONE);
            holder.ivStatusNotDone.setVisibility(View.GONE);
        }
        holder.tvComm.setText(historyItem.getFee() + " " + historyItem.getCurrency());
        holder.tvName.setText(historyItem.getServiceName());
        holder.tvBalance.setText(getFormattedBalance(/*context,*/historyItem.getAmount(),
                historyItem.getCurrency()));
        holder.tvDate.setText(historyItem.getOperationTime());
    }

	private void configureTransferHistory(RecyclerView.ViewHolder mHolder, HistoryItem.TransferHistoryItem historyItem) {
		VHItem holder = (VHItem) mHolder;

        holder.ivType.setVisibility(View.VISIBLE);
        Log.i("HGT","historyItem.getType() ="+historyItem.getType());
		if(historyItem.getType() == 100) {
			holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_local));
		} else if (historyItem.getType() == 110){
			holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_swift));
		} else{
			holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_dark_transfers_internal));
		}
        //convert size to px
        int sizeImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(sizeImage, sizeImage);
        lp.topMargin = 10;
        lp.setMarginEnd(15);
        holder.ivType.setLayoutParams(lp);

        holder.tvComm.setText(historyItem.getFee() + " " + historyItem.getCurrency());
        holder.tvName.setText(historyItem.getDescription());
        holder.tvBalance.setText(getFormattedBalance(historyItem.getAmount(),
                historyItem.getCurrency()));

        holder.tvDate.setText(historyItem.getOperationTime());

        switch (historyItem.getState()) {
            case 10:
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText(R.string.not_done);
                holder.ivStatusNotDone.setVisibility(View.VISIBLE);
                break;
			default:
				holder.tvStatus.setVisibility(View.GONE);
				holder.ivStatusNotDone.setVisibility(View.GONE);
				break;
		}
	}

	public class VHItem extends RecyclerView.ViewHolder {
		@BindView(R.id.tvName) TextView tvName;
		@BindView(R.id.tvBalance) TextView tvBalance;
		@BindView(R.id.tvDate) TextView tvDate;
		@BindView(R.id.tvStatus) TextView tvStatus;
		@BindView(R.id.tvComm) TextView tvComm;
		@BindView(R.id.iv_type) ImageView ivType;
		@BindView(R.id.ivStatusNotDone) ImageView ivStatusNotDone;

		private VHItem(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			Utilities.setRobotoTypeFaceToTextView(itemView.getContext(), tvBalance);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mOnItemClickListener.onItemClick(view, getAdapterPosition());
				}
			});
		}
	}

	public class VHHeader extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_header) TextView tvName;
		@BindView(R.id.ivCalendar) ImageView ivCalendar;

		private VHHeader(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
