package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.graphics.Color;
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
import kz.optimabank.optima24.model.base.HistoryApplications;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.utility.Constants;

public class HistoryApplicationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	Context context;
	ArrayList<HistoryApplications> data = new ArrayList<>();
	private final OnItemClickListener mOnItemClickListener;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_REGULAR = 1;

	public HistoryApplicationsAdapter(Context context, ArrayList<HistoryApplications> data, OnItemClickListener onItemClickListener) {
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
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_applic_item, parent, false);
			viewHolder = new VHItem(view);
		}
		return viewHolder;
	}

    public void updateList(ArrayList<HistoryApplications> list){
        data = list;
        notifyDataSetChanged();
    }

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder mHolder, final int position) {
		HistoryApplications historyApplications = null;
		if(data!=null && !data.isEmpty()) {
			historyApplications = data.get(position);
		}
		if(historyApplications != null) {
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

				//holder.ivCalendar.setVisibility(View.GONE);
				/*if(historyApplications.Date.equals(context.getString(R.string.not_data))){
					holder.tvName.setGravity(Gravity.CENTER);
					holder.tvName.setPadding(70,15,15,15);
				}*/
				holder.tvName.setText(historyApplications.DisplayName);

			} else if(mHolder instanceof VHItem) {
				configureAppHistiry(mHolder,historyApplications);
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

	private void configureAppHistiry(RecyclerView.ViewHolder mHolder, HistoryApplications historyApplications) {
		VHItem holder = (VHItem) mHolder;
		holder.tvName.setText(historyApplications.DisplayName);
		holder.tvNumber.setText(historyApplications.id +" /");
		holder.tvStatus.setText(historyApplications.StatusDisplayName);
		holder.tvStatus.setTextColor(Color.parseColor(historyApplications.StatusStyleType));
		/*switch (historyApplications.StatusStyleType){
			case 0:
				holder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.green_19_136_52));
				break;
			case 1:
				holder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.red_attention));
				break;
			case 2:
				holder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.blue_6_83_161));
				break;
			case 3:
				holder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.orange_246_121_37));
				break;
			case 4:
				holder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.black));
				break;
			case 5:
				holder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.gray_atf_));
				break;
		}*/

	}

	public class VHItem extends RecyclerView.ViewHolder {
		@BindView(R.id.tvName) TextView tvName;
		@BindView(R.id.tvNumber) TextView tvNumber;
		@BindView(R.id.tvStatus) TextView tvStatus;

		private VHItem(View itemView) {
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

	public class VHHeader extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_header) TextView tvName;
		@BindView(R.id.ivCalendar) ImageView ivCalendar;

		private VHHeader(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
