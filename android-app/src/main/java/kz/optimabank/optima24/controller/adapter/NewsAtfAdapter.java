package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.NewsItem;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.utility.LocaleUtils;
import kz.optimabank.optima24.utility.Utilities;

/**
  Created by Timur on 30.03.2017.
 */

public class NewsAtfAdapter extends RecyclerView.Adapter<NewsAtfAdapter.ViewHolder> {
    ArrayList<NewsItem> data;
    Context context;
    private OnItemClickListener onItemClickListener;

    public NewsAtfAdapter(ArrayList<NewsItem> data, OnItemClickListener onItemClickListener, Context context) {
        //Log.i("NEWSITEM0","data.0 = "+data.get(0).getPublicationItemRecords().get(0).getBody());
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(data!=null&&!data.isEmpty()) {
            NewsItem newsItem = data.get(position);
            if (newsItem != null) {
                String languageApp = LocaleUtils.getLanguage(context);
                holder.tvPublishDate.setText(newsItem.getPublishDate());
                NewsItem.PublicationItemRecord item;
                switch (languageApp.toUpperCase()) {
                    case "RU":
                        item = newsItem.getPublicationItemRecords().get(1);
                        break;
                    case "EN":
                        item = newsItem.getPublicationItemRecords().get(2);
                        break;
                    default:
                        item = newsItem.getPublicationItemRecords().get(0);
                        break;
                }
                holder.tvNewsTitle.setText(item.getTitle());
                NewsItem newsItemForIm = data.get(position);
                String str =  String.valueOf(newsItemForIm.getId());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                int isViewed = prefs.getInt(str, -1);
                Log.i("str","str = " + str);
                Log.i("isViewed","isViewed = " + isViewed);
                Log.i("holder","holder = " + holder);
                Log.i("holder","holder = " + holder.tvNewsTitle.getTextColors());
                if (Utilities.getDateSevenDaysAgo(newsItem.getPublishDate())&&isViewed == -1){
                    holder.tvNewsTitle.setTextColor(Color.BLACK);
                }else{
                    holder.tvNewsTitle.setTextColor(context.getResources().getColor(R.color.gray_atf));
                }
                //if (isViewed == 1) {
                //    //newsItemForIm.getPublicationItemRecords().
                //    holder.tvNewsTitle.setTextColor(context.getResources().getColor(R.color.gray_atf_));
                //}else{
                //    holder.tvNewsTitle.setTextColor(Color.BLACK);
                //}
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvPublishDate) TextView tvPublishDate;
        @BindView(R.id.tvNewsTitle) TextView tvNewsTitle;

       public ViewHolder(View itemView) {
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
}
