package kz.optimabank.optima24.controller.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import kz.optimabank.optima24.fragment.ATFFragment;

/**
  Created by Timur on 27.03.2017.
 */

public class ATMTabAdapter extends FragmentStatePagerAdapter  {
    private final ArrayList<ATFFragment> fragments;
    private final ArrayList<String> mFragmentTitle;

    public ATMTabAdapter(FragmentManager fm, ArrayList<ATFFragment> fragments, ArrayList<String> mFragmentTitle) {
        super(fm);
        this.fragments = fragments;
        this.mFragmentTitle = mFragmentTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mFragmentTitle!=null&&!mFragmentTitle.isEmpty()) {
            return mFragmentTitle.get(position);
        }
        return null;
    }
}
