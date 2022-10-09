package kz.optimabank.optima24.fragment.settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.db.controllers.DigitizedCardController;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.CardLock;
import kz.optimabank.optima24.model.service.CardLockImpl;
import kz.optimabank.optima24.utility.Constants;

import static android.app.Activity.RESULT_OK;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.SELECT_CITIZENSHIP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;

/**
 * Created by Max on 28.08.2017.
 */

public class CardLockFragment extends ATFFragment implements View.OnClickListener, CardLockImpl.Callback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.linSpinnerLin)
    LinearLayout linSpinnerLin;
    @BindView(R.id.tvSpinner)
    TextView tvSpinner;
    @BindView(R.id.lock_card_hint_textView)
    TextView lock_card_hint;
    @BindView(R.id.btnLockCard)
    Button btnLockCard;
    @BindView(R.id.lock_hint_tv)
    TextView lockHintTv;
    public static UserAccounts.Cards ACCOUNT;
    CardLock cardLock;
    private String[] reasonsOfBlockingArray;
    private DigitizedCardController digitizedCardController;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private boolean isDefaultCard;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_lock, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();

        lock_card_hint.setText(getString(R.string.lock_card_hint) + " " + ACCOUNT.name);
        linSpinnerLin.setOnClickListener(this);
        btnLockCard.setOnClickListener(this);
        digitizedCardController = DigitizedCardController.getController();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reasonsOfBlockingArray = new String[]{
                getString(R.string.lost_card),
                getString(R.string.theft_card),
                getString(R.string.comprosentation),
                getString(R.string.reason_temporary_blocking)
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linSpinnerLin:
                createAlert();
                break;
            case R.id.btnLockCard:
                cardLock = new CardLockImpl();
                cardLock.registerCallBack(this);

                // проверка типа карты Visa или Элкарт

                if (ACCOUNT != null) isDefaultCard = ACCOUNT.brandType != 1;

                builder = new AlertDialog.Builder(requireContext());
                builder.setCancelable(false);
                builder.setMessage(R.string.alert_you_sure);
                builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String code = "";
                        for (int i = 0; i < reasonsOfBlockingArray.length; i++) {
                            if (String.valueOf(tvSpinner.getText()).equals(reasonsOfBlockingArray[i])) {
                                switch (i) {
                                    case 0:
                                        if (isDefaultCard) code = CardLockReason.LOST.getValue();
                                        else code = CardLockReasonElcart.LOST.getValue();
                                        break;
                                    case 1:
                                        if (isDefaultCard) code = CardLockReason.THEFT.getValue();
                                        else code = CardLockReasonElcart.THEFT.getValue();
                                        break;
                                    case 2:
                                        if (isDefaultCard)
                                            code = CardLockReason.COMPROMISE.getValue();
                                        else code = CardLockReasonElcart.COMPROMISE.getValue();
                                        break;
                                    case 3:
                                        if (isDefaultCard)
                                            code = CardLockReason.TEMPORARY.getValue();
                                        else code = CardLockReasonElcart.TEMPORARY.getValue();
                                }
                            }
                        }
                        JSONObject object = new JSONObject();
                        try {
                            object.put("Reason", code);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error when put fields to json", e);
                        }
                        cardLock.setCardLock(requireContext(), ACCOUNT.code, object);
                    }
                });
                builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requireActivity().finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        digitizedCardController.close();
    }

    public void initToolbar() {
        toolbar.setTitle("");
        ((SettingsActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((SettingsActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SettingsActivity) requireActivity()).onBackPressed();
            }
        });
    }

    public void getBundle() {
        if (getArguments() != null) {
            ACCOUNT = (UserAccounts.Cards) getArguments().getSerializable("account");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CITIZENSHIP_REQUEST_CODE && data != null) {
            String resident = (String) data.getSerializableExtra(STRING_KEY);
            tvSpinner.setText(resident);
            tvSpinner.setError(null);
        }
    }

    private void createAlert() {
        builder = new AlertDialog.Builder(requireContext());
        View v = View.inflate(requireContext(), R.layout.alert_custom_card_lock, null);
        final TextView tvTitle = v.findViewById(R.id.tvTitle);
        final TextView tvFirst = v.findViewById(R.id.tvFirst);
        final TextView tvSecond = v.findViewById(R.id.tvSecond);
        final TextView tvThird = v.findViewById(R.id.tvThird);
        final TextView tvFour = v.findViewById(R.id.tvFour);

        tvTitle.setText(getString(R.string.reason_card_lock));
        tvFirst.setText(getString(R.string.lost_card));
        tvSecond.setText(getString(R.string.theft_card));
        tvThird.setText(getString(R.string.comprosentation));
        tvFour.setText(getString(R.string.reason_temporary_blocking));

        tvFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSpinner.setText(getString(R.string.lost_card));
                tvFirst.setTextColor(getResources().getColor(R.color.blue_0_93_186));
                tvSecond.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvThird.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvFour.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                lockHintTv.setText(getString(R.string.lock_hint));
            }
        });
        tvSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSpinner.setText(getString(R.string.theft_card));
                tvFirst.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvSecond.setTextColor(getResources().getColor(R.color.blue_0_93_186));
                tvThird.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvFour.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                lockHintTv.setText(getString(R.string.reason_compromising_data));
            }
        });
        tvThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSpinner.setText(getString(R.string.comprosentation));
                tvFirst.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvSecond.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvThird.setTextColor(getResources().getColor(R.color.blue_0_93_186));
                tvFour.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                lockHintTv.setText(getString(R.string.reason_compromising_data));
            }
        });

        tvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSpinner.setText(getString(R.string.reason_temporary_blocking));
                tvFirst.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvSecond.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvThird.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvFour.setTextColor(getResources().getColor(R.color.blue_0_93_186));
                lockHintTv.setText(getString(R.string.lock_hint));
            }
        });

        builder.setView(v);
        builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void jsonCardLockResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            showSuccessDialog();
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }


    private void showSuccessDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setMessage(R.string.operation_success)
                .setPositiveButton(getString(R.string.status_ok), (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.BLOCK_CARD_ACTION, true);
                    requireActivity().setResult(RESULT_OK, intent);
                    requireActivity().finish();
                }).create();
        dialog.show();
    }

    private enum CardLockReason {
        LOST("41"),
        THEFT("43"),
        COMPROMISE("7"),
        TEMPORARY("5");

        private String value;

        CardLockReason(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private enum CardLockReasonElcart {
        CARD_UNBLOCK("0"),
        LOST("5"),
        THEFT("1"),
        COMPROMISE("4"),
        TEMPORARY("6");

        private String value;

        CardLockReasonElcart(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}