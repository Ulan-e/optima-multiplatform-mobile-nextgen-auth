package kz.optimabank.optima24.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.CustomListAdapter;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;

import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

public class CustomListFragment extends ATFFragment {

    public static final String CUSTOM_LIST_EXTRA = "custom_list";
    public static final String SELECTED_ITEM_EXTRA = "selected_item";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Object> mObjectList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mObjectList = (ArrayList<Object>) getArguments().getSerializable(CUSTOM_LIST_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);
        ButterKnife.bind(this, view);
        initToolar();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(parentActivity, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new CustomListAdapter(mObjectList, setOnClick()));
        return view;
    }

    private void initToolar() {
        ((NavigationActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((NavigationActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
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
                        Intent intent = new Intent();
                        Object object = mObjectList.get(position);
                        intent.putExtra(SELECTED_ITEM_EXTRA, (Serializable) object);
                        parentActivity.setResult(CommonStatusCodes.SUCCESS, intent);
                        parentActivity.finish();
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
