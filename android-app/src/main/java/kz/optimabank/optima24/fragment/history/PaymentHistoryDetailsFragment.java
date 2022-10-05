package kz.optimabank.optima24.fragment.history;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
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
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
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
import static kz.optimabank.optima24.utility.Utilities.setPaymentImage;

public class PaymentHistoryDetailsFragment extends ATFFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.tvServiceName) TextView tvServiceName;
    @BindView(R.id.tvAmount) TextView tvAmount;
    @BindView(R.id.ivType) ImageView ivType;
    @BindView(R.id.btn_share) Button btnShare;

    //params
    @BindView(R.id.tvAmountWithFee) TextView tvAmountWithFee;
    @BindView(R.id.tvAmountWithFeeInfo) TextView tvAmountWithFeeInfo;

    @BindView(R.id.linCardNumber) LinearLayout linCardNumber;
    @BindView(R.id.tvCardNumber) TextView tvCardNumber;
    @BindView(R.id.tvCardNumberInfo) TextView tvCardNumberInfo;


    @BindView(R.id.linType) LinearLayout linType;
    @BindView(R.id.tvType) TextView tvType;
    @BindView(R.id.tvTypeInfo) TextView tvTypeInfo;

    @BindView(R.id.linReference) LinearLayout linReference;
    @BindView(R.id.tvReference) TextView tvReference;
    @BindView(R.id.tvReferenceInfo) TextView tvReferenceInfo;

    @BindView(R.id.linFullName) LinearLayout linFullName;
    @BindView(R.id.tvFullNameInfo) TextView tvFullNameInfo;
    @BindView(R.id.tvFullName) TextView tvFullName;
    @BindView(R.id.linTaxeCode) LinearLayout linTaxeCode;
    @BindView(R.id.tvTaxeInfo) TextView tvTaxeInfo;
    @BindView(R.id.tvTaxe) TextView tvTaxe;
    @BindView(R.id.linRegionCode) LinearLayout linRegionCode;
    @BindView(R.id.tvRegionInfo) TextView tvRegionInfo;
    @BindView(R.id.tvRegion) TextView tvRegion;
    @BindView(R.id.linAreaCode) LinearLayout linAreaCode;
    @BindView(R.id.tvAreaInfo) TextView tvAreaInfo;
    @BindView(R.id.tvArea) TextView tvArea;
    @BindView(R.id.linSettlementCode) LinearLayout linSettlementCode;
    @BindView(R.id.tvSettlementInfo) TextView tvSettlementInfo;
    @BindView(R.id.tvSettlement) TextView tvSettlement;
    @BindView(R.id.linVehicleNum) LinearLayout linVehicleNum;
    @BindView(R.id.tvVehicleNumInfo) TextView tvVehicleNumInfo;
    @BindView(R.id.tvVehicleNum) TextView tvVehicleNum;

    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvStatusInfo) TextView tvStatusInfo;

    HistoryItem.PaymentHistoryItem historyItem;
    PaymentContextController paymentController;

    private ReceiptModel receiptModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_history_details_fragment, container, false);
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
        if(historyItem!=null) {
            receiptModel = new ReceiptModel();
            tvServiceName.setText(historyItem.getServiceName());
            tvDate.setText(Utilities.formatDateString(parentActivity, formatDate(false, historyItem.createDate), false) + " " +
                    getString(R.string.in) + " " + historyItem.getOperationTime());
            tvAmount.setText(getFormattedBalance(historyItem.getAmount(),
                    historyItem.getCurrency()));
            receiptModel.setOperationTime(Utilities.formatDateString(parentActivity, formatDate(false, historyItem.createDate), false) + " " + historyItem.getOperationTime());
            receiptModel.setServiceName(historyItem.getServiceName());
            receiptModel.setFee(getFormattedBalance(historyItem.getAmount(), historyItem.getCurrency()));

            if (historyItem instanceof HistoryItem.PaymentHistoryItem) {
                HistoryItem.PaymentHistoryItem paymentHistoryItem = (HistoryItem.PaymentHistoryItem) historyItem;

                paymentController = PaymentContextController.getController();
                PaymentCategory paymentCategory = null;
                PaymentService paymentService = paymentController.getPaymentServiceById(paymentHistoryItem.ServiceId);
                if(paymentService!=null) {
                    paymentCategory = paymentController.getPaymentCategoryByServiceId(paymentService.paymentCategoryId);
                }
                if (paymentCategory!=null && paymentCategory.getExternalId() != null) {
                    setPaymentImage(getActivity(), ivType, paymentCategory.alias);
                }
                tvAmountWithFee.setText(getResources().getString(R.string.including_commission));
                tvAmountWithFeeInfo.setText(getAmountWithFee(paymentHistoryItem.getAmount(),
                        paymentHistoryItem.getFee(),paymentHistoryItem.getCurrency()));
                receiptModel.setAmount(getAmountWithFee(paymentHistoryItem.getAmount(),
                        paymentHistoryItem.getFee(),paymentHistoryItem.getCurrency()));

                tvCardNumber.setText(getString(R.string.card));
                tvCardNumberInfo.setText(paymentHistoryItem.getAccountNumber());
                receiptModel.setSendCardNumber(paymentHistoryItem.getAccountNumber());

                if(paymentHistoryItem.getParameters().size() == 1){
                    HistoryItem.HistoryItemParameters parameters = paymentHistoryItem.getParameters().get(0);
                    tvType.setText(String.valueOf(parameters.getLabel()));
                    tvTypeInfo.setText(parameters.getValue());
                    receiptModel.setAccountNumber(parameters.getValue());

                } else if (paymentHistoryItem.getParameters().size() > 1){
                    for(HistoryItem.HistoryItemParameters parameters: paymentHistoryItem.getParameters()){
                        if(parameters.getValue() != null){
                            if(parameters.getServiceParameterId() == 69){
                                tvType.setText(String.valueOf(parameters.getLabel()));
                                tvTypeInfo.setText(parameters.getValue());
                            } else if(parameters.getServiceParameterId() == 0){
                                linFullName.setVisibility(View.VISIBLE);
                                tvFullName.setText(String.valueOf(parameters.getLabel()));
                                tvFullNameInfo.setText(parameters.getValue());
                            } else if(parameters.getServiceParameterId() == 71){
                                linTaxeCode.setVisibility(View.VISIBLE);
                                tvTaxe.setText(String.valueOf(parameters.getLabel()));
                                tvTaxeInfo.setText(parameters.getValue());
                            } else if(parameters.getServiceParameterId() == 72){
                                linRegionCode.setVisibility(View.VISIBLE);
                                tvRegion.setText(String.valueOf(parameters.getLabel()));
                                tvRegionInfo.setText(parameters.getValue());
                            }else if(parameters.getServiceParameterId() == 73){
                                linAreaCode.setVisibility(View.VISIBLE);
                                tvArea.setText(String.valueOf(parameters.getLabel()));
                                tvAreaInfo.setText(parameters.getValue());
                            }else if(parameters.getServiceParameterId() == 74){
                                linSettlementCode.setVisibility(View.VISIBLE);
                                tvSettlement.setText(String.valueOf(parameters.getLabel()));
                                tvSettlementInfo.setText(parameters.getValue());
                            }else if(parameters.getServiceParameterId() == 70){
                                linVehicleNum.setVisibility(View.VISIBLE);
                                tvVehicleNum.setText(String.valueOf(parameters.getLabel()));
                                tvVehicleNumInfo.setText(parameters.getValue());
                            }
                        }
                    }
                }

                tvReference.setText(getString(R.string.receipt));
                tvReferenceInfo.setText("№ " + paymentHistoryItem.getReference());
                isTextViewEmpty(linReference,tvReferenceInfo);
                receiptModel.setReceiptNumber("№ " + paymentHistoryItem.getReference());

                tvStatus.setText(getString(R.string.status));
                tvStatusInfo.setText(paymentHistoryItem.status);
                if(!paymentHistoryItem.success) {
                    tvStatusInfo.setTextColor(ContextCompat.getColor(parentActivity, R.color.red_255_58_58));
                    receiptModel.setOperationStatus(requireActivity().getResources().getString(R.string.text_payment_in_process));
                } else {
                    btnShare.setVisibility(View.VISIBLE);
                    tvStatusInfo.setTextColor(ContextCompat.getColor(parentActivity, R.color.green_65_117_5));
                    receiptModel.setOperationStatus(requireActivity().getResources().getString(R.string.text_payment_is_done));
                }
            }
            showReceiptDetails();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(paymentController!=null){
            paymentController.close();
        }
    }

    private void showReceiptDetails(){
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ReceiptActivity.class);
            intent.putExtra(RECEIPT_MODEL, receiptModel);
            intent.putExtra(RECEIPT_TYPE, ReceiptType.PAYMENT);
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
        if(getArguments()!=null) {
            historyItem = (HistoryItem.PaymentHistoryItem) getArguments().getSerializable(HISTORY_KEY);
        }
    }

    private String getAmountWithFee(double amount, double fee, String currency) {
        double amountWithFee = amount + fee;
        return getFormattedBalance(amountWithFee, currency);
    }

    private void isTextViewEmpty(LinearLayout lin, TextView textViewInfo) {
        if(textViewInfo!=null && textViewInfo.getText().toString().isEmpty()) {
            lin.setVisibility(View.GONE);
        }
    }
}
