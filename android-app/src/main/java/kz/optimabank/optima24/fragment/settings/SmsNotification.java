package kz.optimabank.optima24.fragment.settings;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.interfaces.SmsInform;
import kz.optimabank.optima24.model.interfaces.SmsSend;
import kz.optimabank.optima24.model.service.SmsInformImpl;
import kz.optimabank.optima24.model.service.SmsSendImpl;

/**
 * Created by Max on 29.08.2017.
 */

public class SmsNotification extends ATFFragment implements View.OnClickListener, SmsInformImpl.Callback, SmsSendImpl.TransferCallback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btnConnSms) Button btnConnSms;
    @BindView(R.id.btnEn) Button btnEn;
    @BindView(R.id.btnDis) Button btnDis;

    private SmsInform smsInform;
    private SmsSend smsSend;
    int code;

    boolean isSms, isSmsChecked = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_notification, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();
        if (smsInform==null){
            smsInform = new SmsInformImpl();
        }
        if (smsSend==null){
            smsSend = new SmsSendImpl();
        }
        smsSend.registerSmsCallBackForTransfer(this);
        smsInform.registerCheckSmsCallBack(this);
        smsInform.registerSetSmsInformCallBack(this);
        smsInform.checkSmsInform(getActivity(),code, true);

        //btnEn.setBackgroundColor(Color.GRAY);
        btnEn.setOnClickListener(this);
        btnDis.setOnClickListener(this);
        btnConnSms.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEn :
                isSms = true;
                btnDis.setBackgroundColor(Color.GRAY);
                btnEn.setBackgroundColor(getResources().getColor(R.color.blue_0_93_186));
                break;
            case R.id.btnDis :
                isSms = false;
                btnEn.setBackgroundColor(Color.GRAY);
                btnDis.setBackgroundColor(getResources().getColor(R.color.blue_0_93_186));
                break;
            case R.id.btnConnSms :
                if (isSmsChecked) {
                    smsSend.sendSms(getActivity(),24);
                }else{
                    Toast.makeText(getActivity(), getString(R.string.status_sms_notifications_not_determined), Toast.LENGTH_LONG).show();
                }
                break;
        }
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

    public void getBundle() {
        if (getArguments()!=null){
            code = getArguments().getInt("code");
            Log.i("code"," code ==== "+code);
        }
    }

    @Override
    public void jsonSetSmsInformResponse(int statusCode, String errorMessage) {
        if (statusCode==200){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.operation_success);
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().onBackPressed();
                }
            });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void jsonCheckSmsInformResponse(int statusCode, String errorMessage, String response) {
        if (statusCode==0){
            Log.i("BOOL","response = " + response);
            if (response.equals("true")){
                isSms = true;
                isSmsChecked = true;
                btnDis.setBackgroundColor(Color.GRAY);
                btnEn.setBackgroundColor(getResources().getColor(R.color.blue_0_93_186));
            }else{
                isSms = false;
                isSmsChecked = true;
                btnEn.setBackgroundColor(Color.GRAY);
                btnDis.setBackgroundColor(getResources().getColor(R.color.blue_0_93_186));
            }
        }
    }

    @Override
    public void jsonSmsResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode==0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.sendSMS);
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.send_sms,null);
            builder.setView(view);
            final EditText ed = (EditText) view.findViewById(R.id.smsED);
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                            if (ed.getText().length() > 0) {
                                JSONObject body = new JSONObject();
                                body.put("IsActive", isSms);
                                body.put("SMS", ed.getText());
                                smsInform.setSmsInform(getActivity(), code, body,true);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.text_sms_code), Toast.LENGTH_LONG).show();
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create();
            builder.show();
        }else{
            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void checkMt100TransferResponse(int statusCode, CheckResponse response, String errorMessage) {

    }

}
