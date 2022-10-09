package kz.optimabank.optima24.controller.adapter;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.LOGIN;
import static kz.optimabank.optima24.fragment.authorization.MLoginFragment.CODE;
import static kz.optimabank.optima24.fragment.authorization.MLoginFragment.SAVED_LOGIN;
import static kz.optimabank.optima24.fragment.authorization.MLoginFragment.SAVED_PASSWORD;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.base.SettingsModel;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.SmsInform;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.SmsInformImpl;
import kz.optimabank.optima24.utility.Constants;

/**
  Created by Timur on 04.07.2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SmsInformImpl.Callback{
    ArrayList<SettingsModel> data;
    private OnItemClickListener onItemClickListener;
    Context context;
    int code = 0;
    private boolean isSMS, isSMSFailure = false;
    private SmsInform smsInform = new SmsInformImpl();
    private SharedPreferences mSharedPreferences;
    ViewSMSHolder smsHolder;

    private static final String MY_PREF_KEY = "my_pref";
    private static final String FINGERPRINT_BOOLEAN_KEY = "fingerprint_bool_key";
    private static final String USING_TOUCH_ID_KEY = "using_touch_id_key";


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_REGULAR = 1;
    private static final int TYPE_SMSINFO = 2;
    private static final int TYPE_PUSH_NOTIFICATION = 3;

    public SettingsAdapter(Context context, ArrayList<SettingsModel> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public SettingsAdapter(Context context, ArrayList<SettingsModel> data, OnItemClickListener onItemClickListener, int code) {
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        this.code = code;
        mSharedPreferences = getPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_header, parent, false);
            viewHolder = new VHHeader(view);
        } else if (viewType == TYPE_SMSINFO){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_smsinfo_item, parent, false);
            viewHolder = new ViewSMSHolder(view);
            if(GeneralManager.isFirstCheckSms()){
                checkSMS();
            }
            if(GeneralManager.getHashSmsNotif(code)!=null){
                isSMS = Boolean.valueOf(GeneralManager.getHashSmsNotif(code));
            }
        } else if (viewType == TYPE_PUSH_NOTIFICATION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_smsinfo_item, parent, false);
            viewHolder = new ViewPushNotification(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_list_item, parent, false);
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    private void checkSMS() {
        smsInform.registerCheckSmsCallBack(this);
        smsInform.checkSmsInform(context,code,true);
    }

    private void setSMSInform(JSONObject body) {
        smsInform.registerSetSmsInformCallBack(this);
        smsInform.setSmsInform(context,code,body,true);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {
        if(data!=null&&!data.isEmpty()) {
            SettingsModel item = data.get(position);
            if(item!=null) {
                if(mHolder instanceof VHHeader){
                    VHHeader holder = (VHHeader)mHolder;
                    holder.tvName.setText(item.getName());
                } if (mHolder instanceof ViewSMSHolder){
                    smsHolder = (ViewSMSHolder)mHolder;
                    smsHolder.tvTitle.setText(item.getName());
                    smsHolder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_sms));
                    Log.i("onBindViewHolder"," = " + position);
                    if (!isSMSFailure){
                        smsHolder.swSmsNot.setVisibility(View.VISIBLE);
                        smsHolder.imgReload.setVisibility(View.GONE);
                        smsHolder.swSmsNot.setChecked(isSMS);
                        smsHolder.swSmsNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(!GeneralManager.isFirstCheckSms() ){
                                    isSMS = isChecked;
                                    JSONObject body = new JSONObject();
                                    try {
                                        body.put("IsActive", isSMS);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    setSMSInform(body);
                                }
                            }
                        });
                    }else{
                        smsHolder.swSmsNot.setVisibility(View.GONE);
                        smsHolder.imgReload.setVisibility(View.VISIBLE);
                        Toast.makeText(context, context.getString(R.string.status_sms_notifications_not_determined), Toast.LENGTH_LONG).show();
                    }
                } else if (mHolder instanceof ViewPushNotification) {
                    ViewPushNotification holder = (ViewPushNotification)mHolder;
                    holder.tvTitle.setText(item.getName());
                    holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_notifications));
                } else if (mHolder instanceof ViewHistoryOperations) {
                    ViewHistoryOperations holder = (ViewHistoryOperations)mHolder;
                    holder.tvTitle.setText(item.getName());
                    holder.swSmsNot.setChecked(mSharedPreferences.getBoolean("show_contactless_history", true));
                    holder.swSmsNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putBoolean("show_contactless_history", isChecked);
                            editor.apply();
                        }
                    });
                } else if(mHolder instanceof ViewHolder) {
                    ViewHolder holder = (ViewHolder)mHolder;
                    holder.tvTitle.setText(item.getName());
                    if(item.getName().equals(context.getString(R.string.visibility_account))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_eyeclosed));
                    } else if(item.getName().equals(context.getString(R.string.change_password))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_lock));
                    } else if(item.getName().equals(context.getString(R.string.SMS_alert))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_notifications));
                    } else if(item.getName().equals(context.getString(R.string.region))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_grey_topbar_map));
                    } else if(item.getName().equals(context.getString(R.string.do_not_use_fingerprint_more_switch))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_touch_id));
                        initQuickEntryItem(holder,item);
                    } else if(item.getName().equals(context.getString(R.string.do_not_use_code_more_switch))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_accesscode));
                        initQuickEntryItem(holder,item);
                    }else if(item.getName().equals(context.getString(R.string.InterfaceLanguage))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_language));
                    }else if(item.getName().equals(context.getString(R.string.alert_info))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_paymentcard));
                    }else if(item.getName().equals(context.getString(R.string.requisites))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_grey_topbar_attributes));
                    }else /*if(item.getName().equals(context.getString(R.string.SMS_alert))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.not));
                        initQuickEntryItem(holder,item);
                    }else*/ if(item.getName().equals(context.getString(R.string.limits_info))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_block));
                    }else if(item.getName().equals(context.getString(R.string.Cash_withdrawal))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_cash));
                    }else if(item.getName().equals(context.getString(R.string.Internet_payments))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_paymentcards));
                    }else if(item.getName().equals(context.getString(R.string.pos_terminal_pay))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_posterminal));
                    }else if(item.getName().equals(context.getString(R.string.Card_lock))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_lock));
                    }else if(item.getName().equals(context.getString(R.string.Limits))){
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_button_dark_common_limits));
                    }
                    /* else if (item.getName().equals(context.getString(R.string.activation_contactless))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.paywave));
                    } else if (item.getName().equals(context.getString(R.string.choose_card_for_payment))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.c_2_c));
                    } else if (item.getName().equals(context.getString(R.string.suspend_contactless))) {
                        holder.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_disable_contactless));
                    }*/
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data!=null && !data.isEmpty() && data.get(position).getCode() == Constants.HEADER_ID) {
            return TYPE_HEADER;
        } else if(data!=null && !data.isEmpty() && data.get(position).getCode() == Constants.CARD_SMSINFO) {
            return TYPE_SMSINFO;
        } else if(data!=null && !data.isEmpty() && data.get(position).getCode() == Constants.PUSH_NOTIFICATION) {
            return TYPE_PUSH_NOTIFICATION;
        } else {
            return TYPE_REGULAR;
        }
    }

    private void initQuickEntryItem(ViewHolder holder,SettingsModel item) {
        final SharedPreferences pref = context.getSharedPreferences(MY_PREF_KEY,Context.MODE_APPEND);
        boolean isUserEnableTouchId = pref.getBoolean(FINGERPRINT_BOOLEAN_KEY, false);

        holder.tvTitle.setVisibility(View.GONE);
        holder.swUseQuickEntry.setVisibility(View.VISIBLE);
        holder.swUseQuickEntry.setText(item.getName());
        holder.swUseQuickEntry.setChecked(isUserEnableTouchId);
    }

    private void showAlert(final Switch swUseQuickEntry, final int position) {
        final SettingsModel item = data.get(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        if(item.getName().equals(context.getString(R.string.do_not_use_fingerprint_more_switch))) {
            builder.setMessage(context.getString(R.string.do_not_use_fingerprint_more));
        } else {
            builder.setMessage(context.getString(R.string.do_not_use_code_more));
        }
        builder.setPositiveButton(context.getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final SharedPreferences pref = context.getSharedPreferences(MY_PREF_KEY,Context.MODE_APPEND);

                SharedPreferences.Editor editor = pref.edit();
                if(item.getName().equals(context.getString(R.string.do_not_use_fingerprint_more_switch))) {
                    editor.putBoolean(FINGERPRINT_BOOLEAN_KEY, false);
                    editor.putBoolean(USING_TOUCH_ID_KEY, false);

//                    editor.remove(context.getString(R.string.login_key));
//                    editor.remove(context.getString(R.string.password_key));
                } else {
                    if(!pref.getBoolean(LOGIN,false)) {
                        editor.remove(SAVED_LOGIN);
                    }
                    editor.remove(SAVED_PASSWORD);
                    editor.putBoolean(CODE, false);
                }
                editor.apply();
                dialog.cancel();
                Toast.makeText(context, context.getString(R.string.operation_success), Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(context.getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                swUseQuickEntry.setChecked(true);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void jsonSetSmsInformResponse(int statusCode, String errorMessage) {
        if (statusCode == 200){
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.operation_success);
                builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GeneralManager.setHashSmsNotif(code, isSMS);
                    }
                });
                builder.create();
                builder.show();
            }catch (Exception ignored){}
        }
    }

    @Override
    public void jsonCheckSmsInformResponse(int statusCode, String errorMessage, String response) {
        Log.i("RESPOSMS","RESPO = " + response);
        if (statusCode == Constants.SUCCESS) {
            isSMS = Boolean.valueOf(response);
            GeneralManager.setHashSmsNotif(code, isSMS);
            if (smsHolder != null)
                smsHolder.swSmsNot.setChecked(isSMS);
            GeneralManager.setIsFirstCheckSms(false);
        } else {
            isSMSFailure = true;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.imgItem) ImageView imgItem;
        @BindView(R.id.swUseQuickEntry) Switch swUseQuickEntry;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });

            swUseQuickEntry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(!isChecked) {
                        showAlert(swUseQuickEntry, getAdapterPosition());
                    }else {
                        final SharedPreferences pref = context.getSharedPreferences(MY_PREF_KEY,Context.MODE_APPEND);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(FINGERPRINT_BOOLEAN_KEY, true);
                        editor.putBoolean(USING_TOUCH_ID_KEY, true);
                        editor.apply();
                    }
                }
            });
            Drawable leftDrawable = AppCompatResources.getDrawable(itemView.getContext(), R.drawable.chevron_right);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
        }
    }

    public class ViewSMSHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvInfo) TextView tvInfo;
        @BindView(R.id.imgItem) ImageView imgItem;
        @BindView(R.id.swSmsNot) Switch swSmsNot;
        @BindView(R.id.imgReload) ImageView imgReload;

        public ViewSMSHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            imgReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkSMS();
                }
            });
            /*swSmsNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    Log.i("RESPOSMS","isChecked = "+isChecked);
                    sendSMS();
                    isSMS = isChecked;
                }
            });*/
        }
    }

    public class ViewPushNotification extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvInfo) TextView tvInfo;
        @BindView(R.id.imgItem) ImageView imgItem;
        @BindView(R.id.swSmsNot) Switch swSmsNot;
        @BindView(R.id.imgReload) ImageView imgReload;

        public ViewPushNotification(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvInfo.setVisibility(View.GONE);
            imgReload.setVisibility(View.GONE);
            swSmsNot.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public class ViewHistoryOperations extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvInfo) TextView tvInfo;
        @BindView(R.id.imgItem) ImageView imgItem;
        @BindView(R.id.swSmsNot) Switch swSmsNot;
        @BindView(R.id.imgReload) ImageView imgReload;

        public ViewHistoryOperations(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvInfo.setVisibility(View.GONE);
            imgReload.setVisibility(View.GONE);
            swSmsNot.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header) TextView tvName;

        private VHHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
