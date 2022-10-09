package kz.optimabank.optima24.fragment.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.NewsAtfAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.NewsItem;
import kz.optimabank.optima24.model.interfaces.News;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.NewsImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

public class FragmentNewsInside extends ATFFragment implements  NewsImpl.Callback {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private ArrayList<NewsItem> newsList;
    NewsAtfAdapter adapter;
    News news;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_inside, container, false);
        ButterKnife.bind(this, view);
        inittoolbar();
        progress.setVisibility(View.VISIBLE);
        return view;
    }

    private void inittoolbar() {
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        request();
    }
    @Override
    public void jsonNewsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            newsList = GeneralManager.getInstance().getNews();
//            Collections.reverse(newsList);
            setAdapter(newsList);
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void request(){
        if(GeneralManager.getInstance().getNews().isEmpty()) {
            news = new NewsImpl();
            news.registerCallBack(this);
            news.getNews(getActivity());
        } else {
            newsList = GeneralManager.getInstance().getNews();
            //Collections.reverse(newsList);
            setAdapter(newsList);
        }
    }

    private void request2(){
        news = new NewsImpl();
        news.registerCallBack(this);
        news.getNews(getActivity());
    }

    private void setAdapter(ArrayList<NewsItem> newsList) {
        if(isAdded()) {
            progress.setVisibility(View.GONE);
            adapter = new NewsAtfAdapter(newsList, setOnClick(), getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }
                    @Override
                    public void onAnimationEnd(View view) {
                        NewsItem newsItem = newsList.get(position);
                        Intent intent = new Intent(getActivity(), NavigationActivity.class);
                        intent.putExtra("isNews",true);
                        intent.putExtra("news",newsItem);
                        Log.i("newsItem.getId()","newsItem.getId() = " + newsItem.getId());
                        int str = (newsItem.getId());
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                        editor.putInt(String.valueOf(str), 1);
                        editor.apply();
                        Log.i("editor","editor = " + editor);
                        startActivity(intent);
                        request2();
                        Log.i("anim","anim");
                    }
                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view,animatorListener);
            }
        };
    }
}