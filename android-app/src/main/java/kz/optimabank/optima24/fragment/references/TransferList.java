package kz.optimabank.optima24.fragment.references;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.controller.adapter.TransferAndPaymentAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.TransferModel;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

/**
  Created by Timur on 16.06.2017.
 */

public class TransferList extends ATFFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.tvTitle) TextView tvTitle;

    UserAccounts account;
    TransferAndPaymentAdapter adapter;
    ArrayList<Object> objects = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transfer_list, container, false);
        ButterKnife.bind(this, view);
        //initToolbar();
        getBundle();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    private void initToolbar() {
        //((TransfersActivity)getActivity()).setSupportActionBar(toolbar);   //из за него появляется черная надпись
        tvTitle.setText(getString(R.string.transfers));
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle() {
        if(getArguments()!=null) {
            account = (UserAccounts) getArguments().getSerializable(ACCOUNT);
        }
    }

    private void setAdapter() {
        if(isAdded()) {
            if (account instanceof UserAccounts.CheckingAccounts)
                Log.i("ACCFCA","asd1q23");
                if (!account.currency.equals("KGS")){
                    Log.i("ACCFCA","account.currency = "+account.currency);
                    objects = GeneralManager.getInstance().getTransfersForInCurr(getActivity());
                }else {
                    Log.i("ACCFCA","account.currency = "+account.currency);
                    objects = GeneralManager.getInstance().getTransfers(getActivity());
                }
            adapter = new TransferAndPaymentAdapter(getActivity(), objects, setOnClick());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        initToolbar();
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {}
                    @Override
                    public void onAnimationEnd(View view) {
                        if(objects !=null && !objects.isEmpty()) {
                            Object object =  objects.get(position);
                            TransferModel transferModel = (TransferModel) object;
                            ATFFragment fragment = null;
                            if (transferModel.name.equals(getResources().getString(R.string.transfer_card))) {
                                fragment = new TransferReferencesAccountsFragment();
                            } else if (transferModel.name.equals(getResources().getString(R.string.transfer_swift_tenge))) {
                                fragment = new TransferReferencesInterbank();
                            } else if (transferModel.name.equals(getResources().getString(R.string.transfer_swift))) {
                                fragment = new TransferReferencesInterbankCurr();
                            }
                            if(fragment!=null) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ACCOUNT,account);
                                bundle.putString(TRANSFER_NAME, transferModel.name);
                                fragment.setArguments(bundle);
                                ((TransfersActivity) getActivity()).navigateToPage(fragment);
                            }
                        }
                    }
                    @Override
                    public void onAnimationCancel(View view) {}
                };
                clickAnimation(view,animatorListener);
            }
        };
    }
}
