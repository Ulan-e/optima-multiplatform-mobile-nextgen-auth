package kz.optimabank.optima24.fragment.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.NewsItem;
import kz.optimabank.optima24.model.interfaces.News;
import kz.optimabank.optima24.model.service.NewsImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.LocaleUtils;

public class FragmentNewsItem extends ATFFragment implements NewsImpl.CallbackImage {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tvNewsDate) TextView tvNewsDate;
    @BindView(R.id.tvNewsTitle) TextView tvNewsTitle;
    @BindView(R.id.tvNewsBody) TextView tvNewsBody;
    @BindView(R.id.imageNews) ImageView imageNews;

    private NewsItem newsItem;
    private News newsImpl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_item, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsImpl = new NewsImpl();
        newsImpl.registerImageCallBack(this);
        getNewsParams();
    }

    private void getNewsParams() {
        NewsItem.PublicationItemRecord item;
        String lang = LocaleUtils.getLanguage(getActivity());
        Log.i("lang","lang = "+lang);
        if (lang.equalsIgnoreCase("ru")) {
            item = newsItem.getPublicationItemRecords().get(1);
        } else if (lang.equalsIgnoreCase("en")) {
            item = newsItem.getPublicationItemRecords().get(2);
        } else {
            item = newsItem.getPublicationItemRecords().get(0);
        }
        String body = item.getBody();
        body = body.replaceAll("(\r\n|\n|\r)", "<br />");

        tvNewsDate.setText(newsItem.getPublishDate());
        tvNewsTitle.setText(item.getTitle());

        Spanned spanned = Html.fromHtml(body, new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                Log.i("getDrawable","source = "+source);
                LevelListDrawable d = new LevelListDrawable();

                // пришлось делать такой костыль так как не удалось из готовой ссылки(source) сделать запрос(ретрофит заменял некоторые символы) -  находим в source name и category
                String name = source.substring(source.indexOf("=") + 1, source.indexOf("&"));
                String s = source.substring(source.indexOf("&"));
                String category = s.substring(s.indexOf("=") + 1);
                newsImpl.getNewsImage(FragmentNewsItem.this.getContext(), name, category);

                return d;
            }
        }, null);
        tvNewsBody.setText(spanned);
        tvNewsBody.setClickable(true);
        tvNewsBody.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void getBundle() {
        if(getArguments()!=null){
            newsItem = (NewsItem) getArguments().getSerializable("news");
        }
    }

    public void initToolbar() {
        toolbar.setTitle("");
        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((NavigationActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void jsonNewsImageResponse(int statusCode, String imageBytes) {
        if(statusCode == 0 && imageBytes != null){
            InputStream stream = new ByteArrayInputStream(Base64.decode(imageBytes.getBytes(), Base64.DEFAULT));
            imageNews.setImageBitmap(BitmapFactory.decodeStream(stream));
        }
    }
}