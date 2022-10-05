package kz.optimabank.optima24.controller.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;

import kz.optimabank.optima24.fragment.account.CardDetailsImageFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;

public class CardsViewPagerAdapter extends BaseCircularViewPagerAdapter<UserAccounts.Cards> {
    private final Context mContext;

    public CardsViewPagerAdapter(Context context, FragmentManager fm, ArrayList<UserAccounts.Cards> items) {
        super(fm, items);
        mContext = context;
        Log.i("CardsViewPagerAdapter","CardsViewPagerAdapter");
    }

    @Override
    protected Fragment getFragmentForItem(final UserAccounts.Cards card) {
        return CardDetailsImageFragment.instantiateWithArgs(mContext, card);
    }
}
