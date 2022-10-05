package kz.optimabank.optima24.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.service_point.ServicePointsMap;
import kz.optimabank.optima24.model.base.Terminal;

/**
  Created by Timur on 26.06.2017.
 */

public class ServicePointDetails extends OptimaActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;

    Terminal terminal;
    ATFFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_point_details);
        ButterKnife.bind(this);
        initToolbar();

        terminal = (Terminal) getIntent().getSerializableExtra("terminal");
        setTitle();

        fragment = new ServicePointsMap();
        fragment.setArguments(setBundle());
        navigateToPage(fragment);
    }

    public void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void setTitle() {
        if(terminal!=null) {
            if(terminal.getPointType() == 0) {
                tvTitle.setText(getString(R.string.branch));
            } else if(terminal.getPointType() == 2) {
                tvTitle.setText(getString(R.string.atm));
            } else if(terminal.getPointType() == 3)
                tvTitle.setText(getString(R.string.terminal));
        }
    }

    public void navigateToPage(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.fragment_content, fragment);
        ft.commit();
    }

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isDetails",getIntent().getBooleanExtra("isDetails",false));
        bundle.putSerializable("terminal", getIntent().getSerializableExtra("terminal"));
        return bundle;
    }
}
