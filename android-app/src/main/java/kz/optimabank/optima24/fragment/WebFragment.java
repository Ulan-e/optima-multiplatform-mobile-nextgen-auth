package kz.optimabank.optima24.fragment;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;

public class WebFragment extends ATFFragment{
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;

    String url = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();
        initWebView();
        return view;
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setContentDescription("application/pdf");
        webView.loadUrl("http://docs.google.com/viewer?embedded=true&url=" + url);
    }

    private void initToolbar() {
        toolbar.setTitle(null);
//        if (!url.isEmpty()){
//            tvTitle.setText(R.string.atf_news);}

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle(){
        if (getArguments()!=null){
            Log.i("url","getBundle = " + getArguments().getString("url"));
            url = getArguments().getString("url");
        }
    }
}
