package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.Terminal;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;

public class ATMListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<Terminal> data;
    Context context;
    private final OnItemClickListener mOnItemClickListener;
    boolean M500 = true,Mm1000 = true,M1000 = true;

    public ATMListAdapter(Context cxt, ArrayList<Terminal> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.context = cxt;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.atm_item, parent, false);
        return new VHItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        Terminal terminal = data.get(position);
        if(terminal!=null) {
            VHItem holder = (VHItem) mHolder;
            Log.i("getDistanceD","getDistanceD = "+terminal.getDistanceD());
            if (terminal.getDistanceD()==500123321){
                holder.tvRep.setVisibility(View.VISIBLE);
                holder.tvRep.setText(R.string.M500);
                holder.linATM.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(null);
                holder.tvName.setVisibility(View.GONE);
                holder.tvAddress.setVisibility(View.GONE);
                holder.tvDistance.setVisibility(View.GONE);
            }else if (terminal.getDistanceD()==1100123321){
                holder.tvRep.setVisibility(View.VISIBLE);
                holder.tvRep.setText(R.string.Mm1000);
                holder.itemView.setOnClickListener(null);
                holder.tvName.setVisibility(View.GONE);
                holder.linATM.setVisibility(View.GONE);
                holder.tvAddress.setVisibility(View.GONE);
                holder.tvDistance.setVisibility(View.GONE);
            }else if (terminal.getDistanceD()==1000123321){
                holder.tvRep.setVisibility(View.VISIBLE);
                holder.tvRep.setText(R.string.M1000);
                holder.itemView.setOnClickListener(null);
                holder.tvName.setVisibility(View.GONE);
                holder.linATM.setVisibility(View.GONE);
                holder.tvAddress.setVisibility(View.GONE);
                holder.tvDistance.setVisibility(View.GONE);
            }else {
                holder.tvRep.setVisibility(View.GONE);
                holder.tvName.setVisibility(View.VISIBLE);
                holder.linATM.setVisibility(View.VISIBLE);
                holder.tvAddress.setVisibility(View.VISIBLE);
                holder.tvDistance.setVisibility(View.VISIBLE);
                if(terminal.getPointType() == 0)
                    holder.tvName.setText(context.getResources().getString(R.string.branch));
                else if(terminal.getPointType() == 2)
                    holder.tvName.setText(context.getResources().getString(R.string.atm));
                else if(terminal.getPointType() == 3)
                    holder.tvName.setText(context.getResources().getString(R.string.terminal));
                holder.tvAddress.setText(terminal.getAddress());
                holder.tvWorkingTime.setText(terminal.getWorkTime());
                int distance = terminal.getDistance();
                if (distance != 0) {
                    holder.tvDistance.setText(distance + " " + context.getString(R.string.meters) + "");
                } else {
                    holder.tvDistance.setVisibility(View.GONE);
                }
            }
        }
    }

    public void updateList(ArrayList<Terminal> list) {
        data = list;
        notifyDataSetChanged();
        Log.i("hren","hren' = ");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvAddress) TextView tvAddress;
        @BindView(R.id.tvWorkingTime) TextView tvWorkingTime;
        @BindView(R.id.tvDistance) TextView tvDistance;
        @BindView(R.id.tvRep) TextView tvRep;
        @BindView(R.id.linATM) LinearLayout linATM;

        public VHItem(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }
}
