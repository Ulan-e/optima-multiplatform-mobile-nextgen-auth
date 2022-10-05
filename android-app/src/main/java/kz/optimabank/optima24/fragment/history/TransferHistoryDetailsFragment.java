package kz.optimabank.optima24.fragment.history;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.ReceiptType;
import kz.optimabank.optima24.model.base.HistoryItem;
import kz.optimabank.optima24.receipt.model.ReceiptModel;
import kz.optimabank.optima24.receipt.ui.ReceiptActivity;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.HISTORY_KEY;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_MODEL;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_TYPE;
import static kz.optimabank.optima24.utility.Utilities.formatDate;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

public class TransferHistoryDetailsFragment extends ATFFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.ivType)
    ImageView ivType;
    @BindView(R.id.btn_share)
    Button btnShare;

    //params
    @BindView(R.id.tvAmountWithFee)
    TextView tvAmountWithFee;
    @BindView(R.id.tvAmountWithFeeInfo)
    TextView tvAmountWithFeeInfo;

    @BindView(R.id.linAccountNumber)
    LinearLayout linAccountNumber;
    @BindView(R.id.tvAccountNumber)
    TextView tvAccountNumber;
    @BindView(R.id.linContragentName)
    LinearLayout linContragentName;
    @BindView(R.id.tvContragentName)
    TextView tvContragentName;
    @BindView(R.id.linContragentAccountNumber)
    LinearLayout linContragentAccountNumber;
    @BindView(R.id.tvContragentAccountNumber)
    TextView tvContragentAccountNumber;
    @BindView(R.id.titleContragentAccountNumber)
    TextView titleContragentAccountNumber;
    @BindView(R.id.linContragentBicName)
    LinearLayout linContragentBicName;
    @BindView(R.id.tvContragentBicName)
    TextView tvContragentBicName;

    @BindView(R.id.linType)
    LinearLayout linType;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvTypeInfo)
    TextView tvTypeInfo;

    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvStatusInfo)
    TextView tvStatusInfo;

    HistoryItem.TransferHistoryItem historyItem;

    private ReceiptModel receiptModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transfer_history_details_fragment, container, false);
        ButterKnife.bind(this, view);
        Utilities.setRobotoTypeFaceToTextView(getContext(), tvAmount);
        Utilities.setRobotoTypeFaceToTextView(getContext(), tvAmountWithFeeInfo);
        initToolbar();
        getBundle();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (historyItem != null) {
            receiptModel = new ReceiptModel();
            tvDate.setText(Utilities.formatDateString(parentActivity, formatDate(false, historyItem.createDate), false) + " " +
                    getString(R.string.in) + " " + historyItem.getOperationTime());
            tvAmount.setText(getFormattedBalance(historyItem.getAmount(),
                    historyItem.getCurrency()));

            receiptModel.setOperationTime(Utilities.formatDateString(parentActivity, formatDate(false, historyItem.createDate), false) + " " + " " + historyItem.getOperationTime());
            receiptModel.setAmount(getFormattedBalance(historyItem.getAmount(), historyItem.getCurrency()));
            receiptModel.setFee(getFormattedBalance(historyItem.getFee(), historyItem.getFeeCurrency()));
            receiptModel.setReceiptNumber("№ "+ historyItem.getDocumentNumber());

            if (historyItem.getAccountNumber() != null && !historyItem.getAccountNumber().isEmpty()) {
                linAccountNumber.setVisibility(View.VISIBLE);
                tvAccountNumber.setText(historyItem.getAccountNumber());
                receiptModel.setSendCardNumber(historyItem.getAccountNumber());
            }
            if (historyItem.getContragentName() != null && !historyItem.getContragentName().isEmpty()) {
                linContragentName.setVisibility(View.VISIBLE);
                tvContragentName.setText(historyItem.getContragentName());
                receiptModel.setRecipientName(historyItem.getContragentName());
            }

            if (historyItem.getContragentAccountNumber() != null && !historyItem.getContragentAccountNumber().isEmpty()) {
                linContragentAccountNumber.setVisibility(View.VISIBLE);
                if(historyItem.getTransferMethod() > 0){
                    titleContragentAccountNumber.setText(getActivity().getResources().getString(R.string.phone_number_requesites));
                    tvContragentAccountNumber.setText(historyItem.getReceiverRequisites());
                    receiptModel.setType(1);
                    receiptModel.setRecipientCardNumber(historyItem.getReceiverRequisites());
                } else {
                    tvContragentAccountNumber.setText(historyItem.getContragentAccountNumber());
                    receiptModel.setRecipientCardNumber(historyItem.getContragentAccountNumber());
                }
            }
            if (historyItem.getContragentBicName() != null && !historyItem.getContragentBicName().isEmpty()) {
                linContragentBicName.setVisibility(View.VISIBLE);
                tvContragentBicName.setText(historyItem.getContragentBicName());
            }

            HistoryItem.TransferHistoryItem transferHistoryItem = historyItem;
            if (transferHistoryItem.getType() == 100) {
                ivType.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_image_dark_transfers_local));
            } else if (transferHistoryItem.getType() == 110) {
                ivType.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_image_dark_transfers_swift));
            } else {
                int sizeImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(sizeImage, sizeImage);
                lp.topMargin = 10;
                ivType.setLayoutParams(lp);
                ivType.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_image_dark_transfers_internal));
            }

            tvStatus.setText(getString(R.string.status));

            String transferType = "";
            switch (transferHistoryItem.getType()) {
                case 10:
                    transferType = getString(R.string.TransferMoneyToDeposit);
                    break;
                case 20:
                case 21:
                    transferType = getString(R.string.TransferMoneyInsideClientCards);
                    break;
                case 30:
                    transferType = getString(R.string.TransferMoneyInsideClientAccounts);
                    break;
                case 40:
                    transferType = getString(R.string.TransferMoneyInsideClientCardToAccount);
                    break;
                case 50:
                    transferType = getString(R.string.TransferMoneyInsideClientAccountToCard);
                    break;
                case 60:
                    transferType = getString(R.string.TransferMoneyInsideBankFromAccountToCard);
                    break;
                case 70:
                    transferType = getString(R.string.TransferMoneyInsideBankFromCardAccountToCard);
                    break;
                case 80:
                    transferType = getString(R.string.TransferMoneyInsideBankFromAccountToAccount);
                    break;
                case 90:
                    transferType = getString(R.string.TransferMoneyInsideBankFromCardAccountToAccount);
                    break;
                case 100:
                    transferType = getString(R.string.TransferMoneyToAnotherBank);
                    break;
                case 102:
                    transferType = getString(R.string.TransferMoneyToOutsideCard);
                    break;
                case 103:
                    transferType = getString(R.string.TransferMoneyMasterCardToMasterCard);
                    break;
                case 104:
                    transferType = getString(R.string.TransferMoneyFromVisaToMaster);
                    break;
                case 105:
                    transferType = getString(R.string.TransferMoneyFromMasterToVisa);
                    break;
                case 110:
                    transferType = getString(R.string.TransferForeignCurrencyToAnotherBank);
                    break;
                case 120:
                    transferType = getString(R.string.TransferForeignCurrencyToAnotherBank);
                    break;
                case 140:
                    transferType = getString(R.string.СurrencyExchange);
                    break;
            }

            linType.setVisibility(View.VISIBLE);
            tvType.setText(getResources().getString(R.string.transfer_type));
            tvTypeInfo.setText(transferType);
            tvAmountWithFee.setText(getResources().getString(R.string.fee));
            tvAmountWithFeeInfo.setText(transferHistoryItem.getFee() + " " + transferHistoryItem.getFeeCurrency());

            showReceiptDetails();
            switch (transferHistoryItem.getState()) {
                case 10:
                    tvStatusInfo.setText(R.string.not_done);
                    tvStatusInfo.setTextColor(getResources().getColor(R.color.red_255_58_58));
                    receiptModel.setOperationStatus(requireActivity().getResources().getString(R.string.text_transfer_failed));
                    break;
                case 8:
                    tvStatusInfo.setText(R.string.done);
                    tvStatusInfo.setTextColor(getResources().getColor(R.color.green_65_117_5));
                    receiptModel.setOperationStatus(requireActivity().getResources().getString(R.string.text_transfer_done));
                    btnShare.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[0]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[0]);
                    break;
                case 1:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[1]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[1]);
                    break;
                case 5:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[2]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[2]);
                    break;
                case 6:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[3]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[3]);
                    break;
                case 2:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[4]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[4]);
                    break;
                case 7:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[5]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[5]);
                    break;
                case 9:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[6]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[6]);
                    break;
                case 4:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[7]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[7]);
                    break;
                case 3:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[8]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[8]);
                    break;
                case 11:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[9]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[9]);
                    break;
                case 99:
                    tvStatusInfo.setText(getResources().getStringArray(R.array.transfer_history_status)[10]);
                    receiptModel.setOperationStatus(getResources().getStringArray(R.array.transfer_history_seal_status)[10]);
                    break;
            }
        }
    }

    private void showReceiptDetails(){
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ReceiptActivity.class);
            intent.putExtra(RECEIPT_MODEL, receiptModel);
            intent.putExtra(RECEIPT_TYPE, ReceiptType.TRANSFER);
            startActivity(intent);
        });
    }

    public void initToolbar() {
        toolbar.setTitle("");
        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle() {
        if (getArguments() != null) {
            historyItem = (HistoryItem.TransferHistoryItem) getArguments().getSerializable(HISTORY_KEY);
        }
    }
}
