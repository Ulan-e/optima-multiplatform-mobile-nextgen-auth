package kz.optimabank.optima24.fragment.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.PushResponse;
import kz.optimabank.optima24.model.interfaces.PushInterface;
import kz.optimabank.optima24.model.service.PushImpl;

/**
 * Created by Max on 29.08.2017.
 */

public class PushNotificationFragment extends ATFFragment implements PushImpl.Callback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btnSave) Button btnSave;
    //@BindView(R.id.swAutoPay) Switch swAutoPay;
    @BindView(R.id.swSuccessAutoPay) Switch swSuccessAutoPay;
    @BindView(R.id.swCarryAutoPayment24Hours) Switch swCarryAutoPayment24Hours;
    @BindView(R.id.swErrorAutoPay) Switch swErrorAutoPay;
    //@BindView(R.id.swWidgetInfo) Switch swWidgetInfo;
//    @BindView(R.id.swNewReceipts) Switch swNewReceipts;
//    @BindView(R.id.swNewFines) Switch swNewFines;
    @BindView(R.id.swNews) Switch swNews;
    //@BindView(R.id.swNeedReplenishSavingsAcc) Switch swNeedReplenishSavingsAcc;

    ArrayMap<Switch,Integer> hashSw = new ArrayMap<>();

    PushInterface pushInterface;
    JSONObject body;
    JSONArray array;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_push_notification, container, false);
        ButterKnife.bind(this, view);
        initToolbar();

        hashSw.clear();
        //hashSw.put(swAutoPay,-1011);
        hashSw.put(swSuccessAutoPay,-1013);
        hashSw.put(swCarryAutoPayment24Hours,-1014);
        hashSw.put(swErrorAutoPay,-1015);
        //hashSw.put(swWidgetInfo,-1016);
//        hashSw.put(swNewReceipts,-1017);
//        hashSw.put(swNewFines,-1018);
        hashSw.put(swNews,-1019);
        //hashSw.put(swNeedReplenishSavingsAcc,-1021);

        if (pushInterface==null){
            pushInterface = new PushImpl();
            pushInterface.registerPushSettingsCallback(this);
        }
        pushInterface.getPushSettings(getActivity());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array = new JSONArray();
                for (Map.Entry map : hashSw.entrySet()){
                    body = new JSONObject();
                    try {
                        body.put("PushType",map.getValue());
                        body.put("Enabled",((Switch) map.getKey()).isChecked());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(body);
                }

                pushInterface.setPushSettings(getActivity(),array);
            }
        });

        return view;
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
    public void jsonGetPushSettingsResponse(int statusCode, String errorMessage, ArrayList<PushResponse.PushSettings> response) {
        if (statusCode==0){
            if (response!=null)
            for (PushResponse.PushSettings pushSettings : response){
                switch (pushSettings.pushType){
                    //case -1011:
                    //     swAutoPay.setChecked(pushSettings.enabled);
                    //    break;
                    case -1013:
                        swSuccessAutoPay.setChecked(pushSettings.enabled);
                        break;
                    case -1014:
                        swCarryAutoPayment24Hours.setChecked(pushSettings.enabled);
                        break;
                    case -1015:
                        swErrorAutoPay.setChecked(pushSettings.enabled);
                        break;
                    //case -1016:
                    //    swWidgetInfo.setChecked(pushSettings.enabled);
                    //    break;
//                    case -1017:
//                        swNewReceipts.setChecked(pushSettings.enabled);
//                        break;
//                    case -1018:
//                        swNewFines.setChecked(pushSettings.enabled);
//                        break;
                    case -1019:
                        swNews.setChecked(pushSettings.enabled);
                        break;
                    //case -1021:
                    //    swNeedReplenishSavingsAcc.setChecked(pushSettings.enabled);
                    //    break;
                }
            }
        }else{
            Toast.makeText(getActivity(),R.string.exception,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void jsonSetPushSettingsResponse(int statusCode, String errorMessage) {
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
        }else{
            Toast.makeText(getActivity(),R.string.test_message,Toast.LENGTH_LONG).show();
        }
    }
}
