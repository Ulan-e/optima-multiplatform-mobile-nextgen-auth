package kz.optimabank.optima24.fragment.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.CardLimitActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.controller.adapter.SettingsAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.SettingsModel;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.LimitInterface;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.LimitInterfaceImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.SELECT_LIMIT_REQUEST_CODE;
import static kz.optimabank.optima24.utility.ContactlessUtils.canPayWithNfc;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;
import static kz.optimabank.optima24.utility.Utilities.isDeviceScreenLocked;

/**
 * Created by Timur on 04.07.2017.
 */

public class SettingsFragment extends ATFFragment implements LimitInterfaceImpl.Callback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    SettingsAdapter adapter;
    ArrayList<SettingsModel> items;
    final String SAVED_PASSWORD = "saved_password";
    public static UserAccounts.Cards ACCOUNT;
    boolean CardSett, selectedDefaultCard;
    private LimitInterface limit;
    private ProgressDialog progressDialog;

    private static final String CODE = "code";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (canPayWithNfc(getActivity()) && GeneralManager.getInstance().isInitializedMobocardsSdk()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundle();
        initToolbar();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i("CardSett", "CardSett = " + CardSett);
        if (CardSett) {
            setAdapterForCard();
        } else {
            setAdapter();
        }
    }

    @Override
    public void onStop() {
        if (canPayWithNfc(getActivity()) && GeneralManager.getInstance().isInitializedMobocardsSdk()) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    private void setAdapter() {
        if (isAttached()) {
            items = GeneralManager.getInstance().getSettingsList(getActivity());
            final SharedPreferences pref = getPreferences(getActivity());

            String password = pref.getString(SAVED_PASSWORD, null);
            boolean hasFingerprint = pref.getBoolean(getString(R.string.use_fingerprint_to_authenticate_key), false);

            Log.d(TAG, "hasFingerprint = " + hasFingerprint);
            if (hasFingerprint) {
                items.add(new SettingsModel(Constants.ITEM_ID, getResources().getString(R.string.do_not_use_fingerprint_more_switch)));
            }

            /*
             если добавят возможность отключать пин код
            else if(password != null) {
                items.add(new SettingsModel(Constants.ITEM_ID,getResources().getString(R.string.do_not_use_code_more_switch)));
            }
*/
            adapter = new SettingsAdapter(getActivity(), items, setOnClick());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
    }

    private void setAdapterForCard() {
        if (isAttached()) {
            if (ACCOUNT != null) {
                boolean isDefaultCard = ACCOUNT.brandType != 1;
                items = GeneralManager.getInstance().getSettingsCardList(getActivity(), isDefaultCard);

                //limit = new LimitInterfaceImpl();
                //limit.registerCallBack(this);
                //limit.getLimit(getActivity(), ACCOUNT.code , true);

                adapter = new SettingsAdapter(getActivity(), items, setOnClickForCard(), ACCOUNT.getCode());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

                if (selectedDefaultCard) {
                }
            }
        }
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (items != null && !items.isEmpty()) {
                    SettingsModel item = items.get(position);
                    if (item != null) {
                        if (item.getName().equals(getString(R.string.visibility_account))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new AccountVisibilityFragment());
                        } else if (item.getName().equals(getString(R.string.change_password))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new ChangePasswordFragment());
                        } else if (item.getName().equals(getString(R.string.region))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new SelectRegionFragment());
                        } else if (item.getName().equals(getString(R.string.InterfaceLanguage))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new InterfaceLanguageFragment());
                        } else if (item.getName().equals(getString(R.string.SMS_alert))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new PushNotificationFragment());
                        }
                    }
                }
            }
        };
    }

    private OnItemClickListener setOnClickForCard() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (items != null && !items.isEmpty()) {
                    SettingsModel item = items.get(position);
                    if (item != null) {
                        if (item.getName().equals(getString(R.string.SMS_alert))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new SmsNotification());
                        } else if (item.getName().equals(getString(R.string.alert_info))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new LimitsInfoFragment());
                        } else if (item.getName().equals(getString(R.string.requisites))) {
                            //((SettingsActivity) getActivity()).navigateToSettingsPage(new ChangePasswordFragment());
                        } else if (item.getName().equals(getString(R.string.limits_info))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new LimitsInfoFragment());
                        } else if (item.getName().equals(getString(R.string.Cash_withdrawal))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(1);
                        }/* else if(item.getName().equals(getString(R.string.Internet_payments))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(2);
                        } else if(item.getName().equals(getString(R.string.pos_terminal_pay))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(3);
                        }*/ else if (item.getName().equals(getString(R.string.Internet_payments))) {
                            ((SettingsActivity) getActivity()).navigateToSettingsPage(new InternetPaymentsFragment());
                        } else if (item.getName().equals(getString(R.string.Limits))) {
                            Intent intent = new Intent(getActivity(), CardLimitActivity.class);
                            intent.putExtra(CODE, getArguments().getInt(CODE));
                            startActivityForResult(intent, SELECT_LIMIT_REQUEST_CODE);
//                            ((SettingsActivity) getActivity()).navigateToSettingsPage(4);
                        } else if (item.getName().equals(getString(R.string.Card_lock))) {
                            Fragment fragment = new CardLockFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("account", ACCOUNT);
                            fragment.setArguments(bundle);
                            ((SettingsActivity) getActivity()).navigateToPage(fragment);
                        } else if (item.getName().equals(getString(R.string.activation_contactless))) {
                            if (isDeviceScreenLocked(getActivity())) {
                            } else {
                                showScreenLockedSettingAlert();
                            }
                        } else if (item.getName().equals(getString(R.string.choose_card_for_payment))) {
                            if (isDeviceScreenLocked(getActivity())) {
                            } else {
                                showScreenLockedSettingAlert();
                            }
                        } else if (item.getName().equals(getString(R.string.suspend_contactless))) {
                            if (isDeviceScreenLocked(getActivity())) {
                                //suspendCard
                                showConfirmDialog(1, getResources().getString(R.string.suspend_confirm));
                            } else {
                                showScreenLockedSettingAlert();
                            }
                        }
                    }
                }
            }
        };
    }

    public void getBundle() {
        if (getArguments() != null) {
            ACCOUNT = (UserAccounts.Cards) getArguments().getSerializable("account");
            CardSett = getArguments().getBoolean("CardSett", false);
            selectedDefaultCard = getArguments().getBoolean("selectedDefaultCard", false);
            Log.i("ACCOUNT.cardholderName", "cardholderName = " + ACCOUNT.cardholderName);
            Log.i("ACCOUNT.brand", "brand = " + ACCOUNT.brand);
        }
    }

    public void initToolbar() {
        toolbar.setTitle("");
        ((SettingsActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((SettingsActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
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
        if (statusCode == 0) {

        }
    }

    public void showConfirmDialog(final int code, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showScreenLockedSettingAlert() {
        android.app.AlertDialog.Builder alertBox = new android.app.AlertDialog.Builder(getActivity());
        alertBox.setTitle(getActivity().getResources().getString(R.string.alert_info));
        alertBox.setMessage(getActivity().getResources().getString(R.string.screen_locked_setting_info));
        alertBox.setPositiveButton(getActivity().getResources().getString(R.string.status_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PackageManager packageManager = getActivity().getPackageManager();
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        alertBox.show();
    }

    private void showUpdateCardStatusDialog() {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(getActivity());
        alertBox.setTitle(getResources().getString(R.string.alert_info));
        alertBox.setCancelable(false);
        alertBox.setMessage(getResources().getString(R.string.change_card_status));
        alertBox.setPositiveButton(getResources().getString(R.string.status_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAdapterForCard();
            }
        });
        alertBox.show();
    }

    private void showNfcEnableDialog() {
        android.app.AlertDialog.Builder alertBox = new android.app.AlertDialog.Builder(getActivity());
        alertBox.setTitle(getActivity().getResources().getString(R.string.alert_info));
        alertBox.setMessage(getActivity().getResources().getString(R.string.enable_nfc));
        alertBox.setPositiveButton(getActivity().getResources().getString(R.string.status_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            }
        });
        alertBox.setNegativeButton(getActivity().getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBox.show();
    }

}
