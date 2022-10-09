package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.Chart;
import kz.optimabank.optima24.utility.Constants;

/**
 * Created by Max on 27.11.2017.
 */

public class ChartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_SUB = 1;
    private static final int HEADER_SPACING = 2;
    private static final int TYPE_FOOTER = 3;

    Context context;
    ArrayList<Chart> list;
    ArrayList<Integer> colors;
    int colorCount = 0;

    public ChartAdapter(Context context, ArrayList<Chart> list, ArrayList<Integer> colors){
        this.context = context;
        this.list = list;
        this.colors = colors;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType==TYPE_PARENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chart_item, parent, false);
            viewHolder = new VHolder(view);
        } else
        if (viewType==HEADER_SPACING){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_spacing, parent, false);
            viewHolder = new VHSpacing(view);
        } else
        if (viewType==TYPE_SUB){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chart_item_sub, parent, false);
            viewHolder = new VHolderSub(view);
        } else
        if (viewType==TYPE_FOOTER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chart_footer, parent, false);
            viewHolder = new VHFooter(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chart chart = null;
        if (list!=null&&!list.isEmpty()){
            chart = list.get(position);
        }
        if (chart!=null&&holder!=null){
            if (holder instanceof VHolder) {
                VHolder vholder = (VHolder) holder;
                if (list.get(position).viewId == 1) {
                    vholder.imType.setVisibility(View.VISIBLE);
                    //vholder.imType.setBackgroundColor(colors.get(colorCount));
                    vholder.tvName.setText(list.get(position).name);
                    try {
                        switch (list.get(position).category.ID) {
                            case 1:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_car));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_1);
                                break;
                            case 2:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_bank_operations));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_2);
                                break;
                            case 3:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_home));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_3);
                                break;
                            case 4:
                            case 26:
                                vholder.tvName.setText(context.getString(R.string.animals));
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_animals));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_4);
                                break;
                            case 5:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_health));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_5);
                                break;
                            case 6:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_restaurants));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_6);
                                break;
                            case 7:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_shop));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_7);
                                break;
                            case 8:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_edu));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_8);
                                break;
                            case 9:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_clothes));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_9);
                                break;
                            case 10:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_transfer_from_card));
                                vholder.imType.setImageResource(R.drawable.ic_card);
                                break;
                            case 11:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_air));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_11);
                                break;
                            case 12:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_tv));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_12);
                                break;
                            case 13:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_tv));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_13);
                                break;
                            case 14:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_portf));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_14);
                                break;
                            case 15:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_communal));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_15);
                                break;
                            case 16:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_boots));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_16);
                                break;
                            case 17:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_members));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_17);
                                break;
                            case 22:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_decoration));
                                vholder.imType.setImageResource(R.drawable.ic_image_white_category_22);
                                break;

                            default:
                                vholder.imType.setBackground(context.getResources().getDrawable(R.drawable.radius_star));
                                vholder.imType.setImageResource(R.drawable.ic_stars2);
                                break;
                        }
                    }catch (Exception e){
                        Log.i("ChartAdapter","ChartAdapter Exception e = " + e);
                    }
                    colorCount++;
                    Log.i("COLOR","colorCount = "+colorCount);
                    vholder.tvAmount.setText(getFormattedBalance(list.get(position).amount) + " " + context.getString(R.string.KGS));//list.get(position).curr
                    //vholder.tvAmount.setText(String.format("%.2f", list.get(position).amount) + " " + context.getString(R.string.kzt));//list.get(position).curr
                    vholder.tvPercent.setText(String.format("%.2f", list.get(position).percent) + " " + context.getString(R.string.percent_symbol));// Math.round(list.get(position).percent)
                }
            }else if (holder instanceof VHolderSub) {
                VHolderSub vholder = (VHolderSub) holder;
                    vholder.linSub.setVisibility(View.VISIBLE);
                if (position!=list.size()-1) {
                    if (list.get(position+1).viewId==list.get(position).viewId) {
                        vholder.sub1.setBackgroundColor(colors.get(position-1));
                        vholder.sub2.setBackgroundColor(colors.get(position-1));
                        vholder.sub3.setBackgroundColor(colors.get(position-1));
                        vholder.sub3.setVisibility(View.VISIBLE);
                    }else{
                        vholder.sub1.setBackgroundColor(colors.get(position-1));
                        vholder.sub2.setBackgroundColor(colors.get(position-1));
                        vholder.sub3.setVisibility(View.INVISIBLE);
                    }
                }else{
                    vholder.sub1.setBackgroundColor(colors.get(position-1));
                    vholder.sub2.setBackgroundColor(colors.get(position-1));
                    vholder.sub3.setVisibility(View.INVISIBLE);
                }
                    vholder.tvName.setText(list.get(position).name);
                    //vholder.tvAmount.setText(String.format("%.2f",list.get(position).amount)+" "+context.getString(R.string.kzt));
                    vholder.tvAmount.setText(getFormattedBalance(list.get(position).amount)+" "+context.getString(R.string.KGS));
                    //vholder.tvPercent.setText(String.valueOf(list.get(position).percent));
                    vholder.tvPercent.setText(String.format("%.2f", list.get(position).percent) +" "+context.getString(R.string.percent_symbol));
            }
        }
    }

    public String getFormattedBalance(Float balance) {
        BigDecimal bd = new BigDecimal(balance);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,##0.00", symbols);
//        return String.format("%.2f %s", balance, Currency);
        return formatter.format(bd.doubleValue());
    }

    @Override
    public int getItemViewType(int position) {
        if (list!=null&&!list.isEmpty()){
            if (list.get(position).viewId==1) {
                return TYPE_PARENT;
            }else if(list.get(position).viewId == Constants.HEADER_SPACING) {
                return HEADER_SPACING;
            }else if(list.get(position).viewId == 2) {
                return TYPE_SUB;
            }else if(list.get(position).viewId == 3) {
                return TYPE_FOOTER;
            }
        }
        return TYPE_PARENT;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class VHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imType) ImageView imType;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvAmount) TextView tvAmount;
        @BindView(R.id.tvPercent) TextView tvPercent;

        private VHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public class VHolderSub extends RecyclerView.ViewHolder{
        @BindView(R.id.sub1) View sub1;
        @BindView(R.id.sub2) View sub2;
        @BindView(R.id.sub3) View sub3;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvAmount) TextView tvAmount;
        @BindView(R.id.tvPercent) TextView tvPercent;
        @BindView(R.id.linSub) RelativeLayout linSub;

        private VHolderSub(View view){
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public class VHSpacing extends RecyclerView.ViewHolder {
        @BindView(R.id.view) View view;
        private VHSpacing(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) context.getResources().getDimension(R.dimen.app_bar_height);
            params.width = RecyclerView.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);
        }
    }

    public class VHFooter extends RecyclerView.ViewHolder {
        @BindView(R.id.view) View view;
        private VHFooter(View itemView) {
            super(itemView);
        }
    }
}
