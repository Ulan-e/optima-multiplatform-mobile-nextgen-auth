package kz.optimabank.optima24.fragment.confirm;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.service.NumberOfAttemptsImpl;
import kz.optimabank.optima24.model.service.RemoveOtpKeyImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Utilities.doubleFormatter;

/**
 * Created by Timur on 15.05.2017.
 */

public class ParentSmsConfirmFragment extends ATFFragment implements NumberOfAttemptsImpl.Callback, RemoveOtpKeyImpl.OtpKeyCallBack {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.feeInfo)
    TextView feeInfo;
    @BindView(R.id.tvSumWithFee)
    TextView tvSumWithFee;
    @BindView(R.id.edSms)
    EditText edSms;
    @BindView(R.id.counter_tv)
    TextView counter_tv;
    @BindView(R.id.counter_layout)
    ConstraintLayout counter_layout;

    @BindView(R.id.layout_amount)
    LinearLayout layoutAmount;
    @BindView(R.id.layout_fee_with_sum)
    LinearLayout layoutFeeWithSum;

    int counter;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sms_confirm_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(isAttached()) {
            getMaxNumberOfAttempts();
        }

        Button btnShowReceipt = getActivity().findViewById(R.id.btn_show_receipt);
        if (btnShowReceipt != null) {
            btnShowReceipt.setVisibility(View.GONE);
        }
    }

    private void getMaxNumberOfAttempts() {
        NumberOfAttemptsImpl attemptCountImpl = new NumberOfAttemptsImpl();
        attemptCountImpl.registerCallBack(this);
        attemptCountImpl.getMaxNumberOfAttemptsRequest(requireContext());
    }

    private void removeOtpKeyRequest(int otpKey) {
        RemoveOtpKeyImpl removeOtpKeyImpl = new RemoveOtpKeyImpl();
        removeOtpKeyImpl.registerOtpKeyRequest(this);
        removeOtpKeyImpl.removeOtpKeyRequest(requireContext(), otpKey);
    }

    public void decrementCounter(String errorMessage, int otpKey) {
        counter--;
        counter_tv.setText(String.valueOf(counter));
        counter_layout.setVisibility(View.VISIBLE);
        new AlertDialog.Builder(requireContext()).setMessage(errorMessage).
                setCancelable(false).
                setIcon(R.drawable.ic_dialog_alert).
                setTitle(R.string.alert_error).
                setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (counter != 0) {
                        dialog.cancel();
                    } else {
                        removeOtpKeyRequest(otpKey);
                        requireActivity().finish();
                    }
                }).show();
    }

    public void confirmResponse(int statusCode, String errorMessage, int otpKey) {
        if (statusCode == Constants.SUCCESS) {
            EventBus.getDefault().post(new FinishActivityEvent());
            ((SmsConfirmActivity) (requireActivity())).openFragment(new SuccessOperation());
        } else {
            decrementCounter(errorMessage, otpKey);
        }
    }

    @Override
    public void onNumberOfAttemptsResponse(int statusCode, String errorMessage, String count) {
        if (statusCode == Constants.SUCCESS) {
            counter = Integer.parseInt(count);
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void onOtpKeyResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode != 0) {
            onError(errorMessage);
        }
    }

    public static class FinishActivityEvent {
    }
}