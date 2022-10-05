package kz.optimabank.optima24.fragment.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.interfaces.InternetInterface;
import kz.optimabank.optima24.model.interfaces.LimitInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.InternetImpl;
import kz.optimabank.optima24.model.service.LimitInterfaceImpl;

/**
 * Created by Max on 16.10.2017.
 */

public class LimitsInfoFragment extends ATFFragment implements View.OnClickListener , LimitInterfaceImpl.Callback, InternetImpl.callbackCheck{

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tvAmoutDay) TextView tvAmoutDay;
    @BindView(R.id.tvAmoutDayInfo) TextView tvAmoutDayInfo;
    @BindView(R.id.tvInternetPayments) TextView tvInternetPayments;
    @BindView(R.id.tvInternetPaymentsInfo) TextView tvInternetPaymentsInfo;
    @BindView(R.id.tvLimitRK) TextView tvLimitRK;
    @BindView(R.id.tvLimitRKInfo) TextView tvLimitRKInfo;
    @BindView(R.id.tvLimitOutsideRK) TextView tvLimitOutsideRK;
    @BindView(R.id.tvLimitOutsideRKInfo) TextView tvLimitOutsideRKInfo;
    @BindView(R.id.tvLimitUSACInfo) TextView tvLimitUSACInfo;
    @BindView(R.id.tvLimitUSAMInfo) TextView tvLimitUSAMInfo;
    @BindView(R.id.tvLimitUSARInfo) TextView tvLimitUSARInfo;
    @BindView(R.id.tvLimitAPCInfo) TextView tvLimitAPCInfo;
    @BindView(R.id.tvLimitAPMInfo) TextView tvLimitAPMInfo;
    @BindView(R.id.tvLimitAPRInfo) TextView tvLimitAPRInfo;
    @BindView(R.id.tvLimitCHINACInfo) TextView tvLimitCHINACInfo;
    @BindView(R.id.tvLimitCHINAMInfo) TextView tvLimitCHINAMInfo;
    @BindView(R.id.tvLimitCHINARInfo) TextView tvLimitCHINARInfo;
    @BindView(R.id.tvLimitTHAILANDCInfo) TextView tvLimitTHAILANDCInfo;
    @BindView(R.id.tvLimitTHAILANDMInfo) TextView tvLimitTHAILANDMInfo;
    @BindView(R.id.tvLimitTHAILANDRInfo) TextView tvLimitTHAILANDRInfo;

    int code;
    private final ArrayList<Limit> limitMass = new ArrayList<>();

    private InternetInterface internetInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_limits_info, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();
            if (code != 0 && code != -1) {
                LimitInterface limit = new LimitInterfaceImpl();
                limit.registerCallBack(this);
                limit.getLimit(getActivity(), code, true);
            }

            if (internetInterface==null){
                internetInterface = new InternetImpl();
            }
        internetInterface.registerCheckInternetCallBack(this);
        internetInterface.checkInternet(getActivity(),code, true);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public void initToolbar() {
        toolbar.setTitle("");
        ((SettingsActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((SettingsActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void jsonLimitResponse(int statusCode, String errorMessage) {
        Log.i("LIMIT","statusCode = "+statusCode);
        if (statusCode==0){
            limitMass.addAll(GeneralManager.getInstance().getLimit());
                //setLimits(limitMass);
        }
    }

    /*private void setLimits(ArrayList<Limit> list) {
        for (Limit limit : list){

            try {
                if (limit.getGroupName().equals("LIMDom")) {
                    Log.i("LIMIT", "LIMDom  isActive = " + limit.isActive());
                    if (limit.isActive()) {
                        tvLimitRKInfo.setText(String.valueOf(limit.getMaxAmount()) + " " + (limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                    } else {
                        tvLimitRKInfo.setText(getString(R.string.disabled2));
                    }
                }
            }catch (Exception ignored){}
            try {
            if (limit.getGroupName().equals("LIMVAcq")){
                if(limit.isActive()){
                    tvLimitOutsideRKInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitOutsideRKInfo.setText(getString(R.string.disabled2));
                }
            }
            }catch (Exception ignored){}
            try {
            if (limit.getCode().equals("USA_ATM_CHIP")){
                if(limit.isActive()){
                    tvLimitUSACInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitUSACInfo.setText(getString(R.string.disabled2));
                }
            }
            }catch (Exception ignored){}
            try {
            if (limit.getCode().equals("USA_ATM_MS")){
                if(limit.isActive()){
                    tvLimitUSAMInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitUSAMInfo.setText(getString(R.string.disabled2));
                }
            }
            }catch (Exception ignored){}
            try {
            if (limit.getCode().equals("USA_RETAIL")){
                if(limit.isActive()){
                    tvLimitUSARInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitUSARInfo.setText(getString(R.string.disabled2));
                }
            }
            }catch (Exception ignored){}
            try {
            if (limit.getCode().equals("AP_ATM_CHIP")){
                if(limit.isActive()){
                    tvLimitAPCInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitAPCInfo.setText(getString(R.string.disabled2));
                }
            }
            }catch (Exception ignored){}
            try {
            if (limit.getCode().equals("AP_ATM_MS")){
                if(limit.isActive()){
                    tvLimitAPMInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitAPMInfo.setText(getString(R.string.disabled2));
                }
            }}catch (Exception ignored){}
            try {
            if (limit.getCode().equals("AP_RETAIL")){
                if(limit.isActive()){
                    tvLimitAPRInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitAPRInfo.setText(getString(R.string.disabled2));
                }
            }}catch (Exception ignored){}
            try {
            if (limit.getCode().equals("CHINA_ATM_CHIP")){
                if(limit.isActive()){
                    tvLimitCHINACInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitCHINACInfo.setText(getString(R.string.disabled2));
                }
            }}catch (Exception ignored){}
            try {
            if (limit.getCode().equals("CHINA_ATM_MS")){
                if(limit.isActive()){
                    tvLimitCHINAMInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitCHINAMInfo.setText(getString(R.string.disabled2));
                }
            }}catch (Exception ignored){}
            try {
            if (limit.getCode().equals("CHINA_RETAIL")){
                if(limit.isActive()){
                    tvLimitCHINARInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitCHINARInfo.setText(getString(R.string.disabled2));
                }
            }}catch (Exception ignored){}
            try {
            if (limit.getCode().equals("THAILAND_ATM_CHIP")){
                if(limit.isActive()){
                    tvLimitTHAILANDCInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitTHAILANDCInfo.setText(getString(R.string.disabled2));
                }
            }}catch (Exception ignored){}
            try {
            if (limit.getCode().equals("THAILAND_ATM_MS")){
                if(limit.isActive()){
                    tvLimitTHAILANDMInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitTHAILANDMInfo.setText(getString(R.string.disabled2));
                }
            }}catch (Exception ignored){}
            try {
            if (limit.getCode().equals("THAILAND_RETAIL")){
                if(limit.isActive()){
                    tvLimitTHAILANDRInfo.setText(String.valueOf(limit.getMaxAmount())+" "+(limit.getMaxAmountCurrency().equals("KZT") ? getString(R.string.tenge_icon) : getString(R.string.USD)));
                }else{
                    tvLimitTHAILANDRInfo.setText(getString(R.string.disabled2));
                }
            }
            }catch (Exception ignored){}


        }
    }*/

    public void getBundle() {
        if (getArguments()!=null){
            code = getArguments().getInt("code");
        }
    }

    @Override
    public void jsonCheckInternetResponse(int statusCode, String errorMessage, Limit.Internet internet) {
        Log.i("internet","internet = " + internet);
        if (statusCode==0){
            if (internet.IsActive) {
                tvInternetPaymentsInfo.setText(R.string.enabled2);
            }else{
                tvInternetPaymentsInfo.setText(R.string.disabled2);
            }
        }
    }
}
