package kz.optimabank.optima24.fragment.transfer;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.fragment.ATFFragment;

/**
  Created by Timur on 13.06.2017.
 */

public class InformationFragment extends ATFFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvInfo) TextView tvInfo;
    String info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.information_fragment, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(info!=null && !info.isEmpty()) {
            tvInfo.setText(info);
        }
    }

    public void initToolbar() {
        toolbar.setTitle("");
        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle() {
        if(getArguments()!=null) {
            info = getArguments().getString("infoText");
        }
    }
}
