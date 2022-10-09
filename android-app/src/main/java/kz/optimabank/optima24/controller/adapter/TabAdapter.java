package kz.optimabank.optima24.controller.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import kz.optimabank.optima24.fragment.ATFFragment;

/**
  Created by Timur on 22.03.2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<ATFFragment> fragments;

    public TabAdapter(FragmentManager fm, ArrayList<ATFFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public Fragment getRegisteredFragment(int position) {
        return fragments.get(position);
    }
}
