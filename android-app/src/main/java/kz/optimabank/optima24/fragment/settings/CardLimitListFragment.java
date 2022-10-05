package kz.optimabank.optima24.fragment.settings;

import static kz.optimabank.optima24.utility.Constants.NOTIFICATION_ARG_ID;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.List;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.CardLimitActivity;
import kz.optimabank.optima24.controller.adapter.SelectCardLimitAdapter;
import kz.optimabank.optima24.databinding.FragmentCardLimitListBinding;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.interfaces.LimitInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.LimitInterfaceImpl;
import kz.optimabank.optima24.notifications.ui.notificationDetails.NotificationDetailsFragment;
import kz.optimabank.optima24.utility.Utilities;

public class CardLimitListFragment extends ATFFragment implements LimitInterfaceImpl.Callback {

    private FragmentCardLimitListBinding binding;
    private SelectCardLimitAdapter adapter;
    private List<Limit> listOfLimits = new ArrayList<>();
    private LimitInterface limitInterface;
    private int code;

    private static final int TYPE_DEFAULT = 4;
    private static final String TYPE = "type";
    private static final String CODE = "code";
    private static final String POSITION = "position";
    private static final int ZERO_VALUE = 0;
    private static final int VALUE_MINUS_ONE = -1;

    private static final int ATM_LIMIT_CASH_LIMIT = 0;
    private static final int RETAIL_CARD_PRESENT = 2;
    private static final int CARD_NOT_PRESENT = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCardLimitListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        doRequest();
    }

    @Override
    public void jsonLimitResponse(int statusCode, String errorMessage) {
        if (statusCode == ZERO_VALUE) {
            setAdapter(GeneralManager.getInstance().getLimit());
        }
    }

    private void initToolbar() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requireActivity().onBackPressed();
            }
        });
    }

    private void doRequest() {

        if (getArguments() != null) {
            code = getArguments().getInt(CODE);
        }
        if (code != ZERO_VALUE && code != VALUE_MINUS_ONE) {
            limitInterface = new LimitInterfaceImpl();
            limitInterface.registerCallBack(this);
            limitInterface.getLimit(getActivity(), code, true);
        } else {
            Utilities.showToast(requireActivity(),  getString(R.string.card_not_defined));
        }
    }

    private void setAdapter(List<Limit> limits) {
        listOfLimits.clear();
        listOfLimits.addAll(limits);

        adapter = new SelectCardLimitAdapter(requireActivity(), listOfLimits, new SelectCardLimitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Limit limit, int position) {
                navigateToCardLimitFragment(limit, position);
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void navigateToCardLimitFragment(Limit limit, int position) {
        setResultForIntent();

        ((CardLimitActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, newInstance(limit, position), null)
                .commit();
    }

    private void setResultForIntent() {
        Intent intent = new Intent();
        requireActivity().setResult(CommonStatusCodes.SUCCESS, intent);
    }

    public CardLimitFragment newInstance(Limit limit, int position) {
        CardLimitFragment fragment = new CardLimitFragment();
        fragment.setArguments(getBundle(limit, position));
        return fragment;
    }

    private Bundle getBundle(Limit limit, int position){

        Bundle bundle = new Bundle();
        bundle.putParcelable(STRING_KEY, limit);
        bundle.putInt(TYPE, TYPE_DEFAULT);
        bundle.putInt(CODE, getArguments().getInt(CODE));
        bundle.putInt(POSITION, position);

        return  bundle;
    }
}