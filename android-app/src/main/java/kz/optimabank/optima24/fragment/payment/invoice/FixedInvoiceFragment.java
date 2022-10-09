package kz.optimabank.optima24.fragment.payment.invoice;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.controller.adapter.FixedInvoiceAdapter;
import kz.optimabank.optima24.model.base.InvoiceContainerItem;
import kz.optimabank.optima24.model.gson.response.CheckPaymentsResponse;
import kz.optimabank.optima24.model.gson.response.TaxDictResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.InvoicePayment;
import kz.optimabank.optima24.model.interfaces.Payments;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.InvoicePaymentImpl;
import kz.optimabank.optima24.model.service.PaymentsImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

/**
 * Created by Timur on 26.07.2017.
 */

public class FixedInvoiceFragment extends CommonInvoiceFragment implements View.OnClickListener,
        InvoicePaymentImpl.Callback, PaymentsImpl.CallbackCheck {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;
    //spinner from
    @BindView(R.id.tvSpinnerFrom)
    TextView tvSpinnerFrom;
    @BindView(R.id.linSpinnerFrom)
    LinearLayout linSpinnerFrom;
    @BindView(R.id.linAccountInfo)
    LinearLayout linAccountInfo;
    @BindView(R.id.imgType)
    ImageView imgType;
    @BindView(R.id.tvAccountName)
    TextView tvAccountName;
    @BindView(R.id.tvAccountBalance)
    TextView tvAccountBalance;
    @BindView(R.id.tvAccountNumber)
    TextView tvAccountNumber;
    @BindView(R.id.tv_multi)
    TextView tvMulti;

    @BindView(R.id.btnPayment)
    Button btnPayment;

    FixedInvoiceAdapter adapter;
    InvoicePayment invoicePayment;
    CheckPaymentsResponse checkResponse;
    Payments payments;
    double sumWithAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fixed_invoice_fragment, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();

        if (GeneralManager.getInstance().getInvoiceAccountFrom() != null) {
            accountFrom = GeneralManager.getInstance().getInvoiceAccountFrom();
            setAccountSpinnerFrom(accountFrom, tvSpinnerFrom, linAccountInfo, tvAccountName,
                    tvAccountBalance, tvAccountNumber, imgType, tvMulti);
        }
        linSpinnerFrom.setOnClickListener(this);

        tvTotalAmount.setText(getFormattedBalance(calculateTotalSum(), invoiceItem.invoiceBody.Currency));

        btnPayment.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linSpinnerFrom:
                Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("isCards", true);
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                break;
            case R.id.btnPayment:
                if (accountFrom == null) {
                    tvSpinnerFrom.setError(getString(R.string.choose_card_toast));
                    return;
                }
                if (GeneralManager.getInstance().isInvoiceCheckedStatus()) {
                    if (invoicePayment == null) {
                        invoicePayment = new InvoicePaymentImpl();
                        invoicePayment.registerCallBack(this);
                    }
                    if (checkResponse != null) {
                        JSONObject body = getInvoiceBody(checkResponse);
                        Log.d(TAG, "InvoiceBody = " + body);
                        invoicePayment.invoicePayment(getActivity(), body);
                    }
                } else {
                    if (payments == null) {
                        payments = new PaymentsImpl();
                        payments.registerCheckCallBack(this);
                    }
                    payments.checkPayments(getActivity(), false, getCheckBody());
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                Log.d("TAG", "data = " + data);
                if (data != null) {
                    accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                    setAccountSpinnerFrom(accountFrom, tvSpinnerFrom, linAccountInfo, tvAccountName,
                            tvAccountBalance, tvAccountNumber, imgType, tvMulti);
                    GeneralManager.getInstance().setInvoiceAccountFrom(accountFrom);
                }
            }
        }
    }

    @Override
    public void checkPaymentResponse(int statusCode, CheckPaymentsResponse response, String errorMessage, boolean isBalance) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setInvoiceCheckedStatus(true);
            enableDisableView(linMain, false);
            enableDisableView(btnPayment, true);
            linFee.setVisibility(View.VISIBLE);
            sumWithAmount = response.fee + totalAmount;
            tvFee.setText(getFormattedBalance(response.fee, invoiceItem.invoiceBody.Currency));
            tvSumWithFee.setText(getFormattedBalance(sumWithAmount, invoiceItem.invoiceBody.Currency));
            checkResponse = response;
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
            checkResponse = null;
        }
    }

    @Override
    public void jsonInvoicePaymentResponse(int statusCode, String errorMessage) {
        if (statusCode == 200) {
            GeneralManager.getInstance().setPaymentParameters(getBodyParameters(paymentService));
            Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
            intent.putExtra("isPayment", true);
            intent.putExtra("isSuccess", true);

            Log.i("ITINVO", "isTemplateFIF = " + isTemplate);
            Log.i("ITINVO", "isTemplateFIF = " + getArguments().getBoolean("isTemplate"));
            intent.putExtra("isTemplate", isTemplate);
            intent.putExtra("isClickToTemplateGridView", false);
            intent.putExtra("feeWithAmount", sumWithAmount);
            intent.putExtra("paymentTitle", categoryName);
            intent.putExtra("operationCurrency", invoiceItem.invoiceBody.Currency);
            //for template
            if (!isTemplate) {
                intent.putExtra("amount", String.valueOf(totalAmount));
                intent.putExtra("sourceAccountId", accountFrom.code);
                intent.putExtra("serviceId", paymentService.id);
            }
            startActivity(intent);
            getActivity().finish();
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }


    @Override
    public void taxDictResponse(int statusCode, TaxDictResponse response, String errorMessage) {
    }

    private void setAdapter() {
        adapter = new FixedInvoiceAdapter(getActivity(), getFixedInvoice(), this);
        recyclerView.setAdapter(adapter);
    }

    public void updateTotalSum() {
        tvTotalAmount.setText(getFormattedBalance(calculateTotalSum(), invoiceItem.invoiceBody.Currency));
    }

    private ArrayList<InvoiceContainerItem.BodyItem> getFixedInvoice() {
        ArrayList<InvoiceContainerItem.BodyItem> items = new ArrayList<>();
        for (InvoiceContainerItem.BodyItem item : invoiceItem.invoiceBody.getItems()) {
            if (!item.isCounterService()) {
                items.add(item);
            }
        }
        return items;
    }
}
