package kz.optimabank.optima24.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import kg.optima.mobile.R;
import kz.optimabank.optima24.app.NonSwipeableViewPager;
import kz.optimabank.optima24.controller.adapter.TabAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.MainFragment;
import kz.optimabank.optima24.fragment.news.FragmentNews;
import kz.optimabank.optima24.fragment.rate.RateFragment;
import kz.optimabank.optima24.fragment.service_point.ListAndMapContainer;
import kz.optimabank.optima24.model.manager.GeneralManager;

/**
 * Created by Timur on 06.03.2017.
 */

public class UnauthorizedTabActivity extends OptimaActivity {
    NavigationTabBar navigationTabBar;
    NonSwipeableViewPager viewPager;
    UnderlinePageIndicator indicator;
    private int selectedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.vp_horizontal_ntb);
        indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), getFragments(selectedPosition)));
        navigationTabBar.setModels(getModels());
        navigationTabBar.setViewPager(viewPager, selectedPosition);
        indicator.setViewPager(viewPager);
        indicator.setFades(false);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                if (selectedPosition != index) {
                    viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), getFragments(index)));
                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
                if (selectedPosition != index) {
                    viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), getFragments(index)));
                }
            }
        });
        viewPager.setOffscreenPageLimit(4);
        GeneralManager.getInstance().setAppOpen(true);
    }

    private ArrayList<ATFFragment> getFragments(int pos) {
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        selectedPosition = pos;
        switch (pos) {
            case 0:
                fragments.add(new MainFragment());
                break;
            case 1:
                fragments.add(new ListAndMapContainer());
                break;
            case 2:
                fragments.add(new RateFragment());
                break;
            case 3:
                fragments.add(new FragmentNews());
                break;
        }
        return fragments;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    private ArrayList<NavigationTabBar.Model> getModels() {
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final int bgColor = Color.parseColor("#00000000");
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_home),
                        bgColor)
                        .title(getString(R.string.main))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_map),//R.drawable.ic_location_ico
                        bgColor)
                        .title(getString(R.string.t_on_map))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_rates),//R.drawable.transfer_ico
                        bgColor)
                        .title(getString(R.string.exchange))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_news),//R.drawable.news
                        bgColor)
                        .title(getString(R.string.news))
                        .build()
        );
        return models;
    }


}
