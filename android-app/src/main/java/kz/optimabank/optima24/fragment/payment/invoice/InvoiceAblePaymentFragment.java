package kz.optimabank.optima24.fragment.payment.invoice;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
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
import static kz.optimabank.optima24.utility.Utilities.doubleFormatter;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

public class InvoiceAblePaymentFragment extends CommonInvoiceFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, TextWatcher, PaymentsImpl.CallbackCheck, InvoicePaymentImpl.Callback {
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.tvDifCount)
    TextView tvDifCount;
    @BindView(R.id.edLastCount)
    EditText edLastCount;
    @BindView(R.id.edPrevCount)
    EditText edPrevCount;

    @BindView(R.id.linDebt)
    LinearLayout linDebt;
    @BindView(R.id.tvDebt)
    TextView tvDebt;
    @BindView(R.id.cbDebt)
    CheckBox cbDebt;

    @BindView(R.id.linPenalty)
    LinearLayout linPenalty;
    @BindView(R.id.tvPenalty)
    TextView tvPenalty;
    @BindView(R.id.cbPenalty)
    CheckBox cbPenalty;
    @BindView(R.id.isPay)
    CheckBox isPay;

    @BindView(R.id.tvTotalServiceAmount)
    TextView tvTotalServiceAmount;
    @BindView(R.id.tvTotalServiceText)
    TextView tvTotalServiceText;
    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;
    @BindView(R.id.edAmount)
    EditText edAmount;

    //tariff info
    @BindView(R.id.tvTariffLabel)
    TextView tvTariffLabel;
    @BindView(R.id.linMinTariff)
    LinearLayout linMinTariff;
    @BindView(R.id.linMiddleTariff)
    LinearLayout linMiddleTariff;
    @BindView(R.id.linMaxTariff)
    LinearLayout linMaxTariff;

    @BindView(R.id.tvMinThreshold)
    TextView tvMinThreshold;
    @BindView(R.id.tvMiddleThreshold)
    TextView tvMiddleThreshold;
    @BindView(R.id.tvMaxThreshold)
    TextView tvMaxThreshold;

    @BindView(R.id.tvMinTariffValue)
    TextView tvMinTariffValue;
    @BindView(R.id.tvMiddleTariffValue)
    TextView tvMiddleTariffValue;
    @BindView(R.id.tvMaxTariffValue)
    TextView tvMaxTariffValue;
    @BindView(R.id.tvIfOneTarif)
    TextView tvIfOneTarif;

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

    InvoiceContainerItem.BodyItem bodyItem;
    ArrayList<InvoiceContainerItem.BodyItem> counting;
    ArrayList<InvoiceContainerItem.BodyItem> cleerList = new ArrayList<InvoiceContainerItem.BodyItem>();
    Payments payments;
    CheckPaymentsResponse checkResponse;
    InvoicePayment invoicePayment;
    double sumWithAmount;
    int[] pisitions = new int[19];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.invoice_payment_fragment, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        if (invoiceItem != null && !invoiceItem.invoiceBody.getItems().isEmpty()) {
            bodyItem = invoiceItem.invoiceBody.getItems().get(position);
            counting = invoiceItem.invoiceBody.getItems();
            Log.i("counting", "counting.toArray().length = " + counting.toArray().length);
            //cleerList = invoiceItem.invoiceBody.getItems();
            try {
                Log.i("cleerList", "cleerList.toArray().length = " + cleerList.toArray().length);
            } catch (Exception e) {

            }

            for (int i = 0; i < counting.toArray().length; i++) {
                Log.i("i", "i = " + i);

                if (counting.get(i).isCounterService()) {
                    cleerList.add(counting.get(i));
                }

                /*if (invoiceItem.invoiceBody.getItems().get(position).isCounterService() && posOcup(position)) {
                    if (bodyItem != null) {
                        bodyItem = invoiceItem.invoiceBody.getItems().get(position);
                        Log.i("bodyItemIdName", "bodyItemIdName = " + bodyItem.getServiceId() + "  " + bodyItem.getServiceName());
                        setMetersData(bodyItem);
                        setDataView(bodyItem);
                        setTariffData(bodyItem, invoiceItem.invoiceBody.OccupantsCount);
                        pisitions[pisitions.length-1] = position;
                        Log.i("pisitions.size","pisitions.size = " + pisitions.length);
                        Log.i("pisitions.length","pisitions.length = " + pisitions.length);
                        break;
                    }
                } else {
                    position++;
                }*/
            }
            Log.i("cleerList", "cleerList.toArray().length = " + cleerList.toArray().length);
            Log.i("cleerList", "cleerList.get(position); = " + cleerList.get(position));
            bodyItem = cleerList.get(position);
            setMetersData(bodyItem);
            setDataView(bodyItem);
            setTariffData(bodyItem, invoiceItem.invoiceBody.OccupantsCount);
            edLastCount.setEnabled(bodyItem.isEditable());
            edPrevCount.setEnabled(bodyItem.isEditable());
            edAmount.setEnabled(bodyItem.isEditable());
            Log.i("bodyItem", "bodyItem = " + bodyItem);
            Log.i("bodyItem", "bodyItem.isCounterService() = " + bodyItem.isCounterService());
            Log.i("bodyItem", "bodyItem.getServiceName = " + bodyItem.getServiceName());
            Log.i("bodyItem", "bodyItem.getServiceId = " + bodyItem.getServiceId());
        }
        if (GeneralManager.getInstance().getInvoiceAccountFrom() != null) {
            accountFrom = GeneralManager.getInstance().getInvoiceAccountFrom();
            setAccountSpinnerFrom(accountFrom, tvSpinnerFrom, linAccountInfo, tvAccountName,
                    tvAccountBalance, tvAccountNumber, imgType, tvMulti);
        }
        linSpinnerFrom.setOnClickListener(this);
        btnPayment.setOnClickListener(this);

        edLastCount.addTextChangedListener(this);
        edPrevCount.addTextChangedListener(this);

        cbDebt.setOnCheckedChangeListener(this);
        cbPenalty.setOnCheckedChangeListener(this);

        Log.i("isPay", "onCreateView isPay = " + bodyItem.isPay());
        isPay.setChecked(bodyItem.isPay());
        isPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bodyItem.isPay(isChecked);
                setDataView(bodyItem);
            }
        });
        return view;
    }

    private boolean posOcup(int position) {
        for (int i = 0; i < pisitions.length; i++) {
            Log.i("l1", "l1 = " + i);
            Log.i("pisitions[i]", "pisitions[i] = " + pisitions[i]);
            if (pisitions[i] == position) {
                Log.i("false", "false");
                return false;
            }
        }
        Log.i("true", "true");
        return true;
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
                    Log.i("accountFrom.currency", "accountFrom.currency = " + accountFrom.currency);
                    if (!edAmount.getText().toString().isEmpty() && accountFrom.currency.equalsIgnoreCase("KGS") &&
                            accountFrom.balance < getDoubleType(edAmount.getText().toString())) {
                        onError(getResources().getString(R.string.error_large_sum));
                        return;
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (bodyItem != null) {
            if (editable == edLastCount.getEditableText()) {
                bodyItem.setServiceSum(-1000);
                if (!editable.toString().isEmpty()) {
                    bodyItem.setLastCount(Double.parseDouble(editable.toString().replace(" ", "")));
                } else {
                    bodyItem.setLastCount(0);
                }
                setDataView(bodyItem);
            } else if (editable == edPrevCount.getEditableText()) {
                bodyItem.setServiceSum(-1000);
                if (!editable.toString().isEmpty()) {
                    bodyItem.setPrevCount(Double.parseDouble(editable.toString().replace(" ", "")));
                } else {
                    bodyItem.setPrevCount(0);
                }
                setDataView(bodyItem);
            } else if (editable == edAmount.getEditableText()) {
                if (!editable.toString().isEmpty()) {
                    bodyItem.setServiceSum(Double.parseDouble(editable.toString().replace(" ", "")));
                } else {
                    bodyItem.setServiceSum(0);
                }
                tvTotalServiceAmount.setText(doubleFormatter(calculateServiceSum(bodyItem, invoiceItem.invoiceBody.OccupantsCount, true)));
                tvTotalAmount.setText(getFormattedBalance(calculateTotalSum(), "KGS"));
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (bodyItem != null) {
            switch (compoundButton.getId()) {
                case R.id.cbDebt:
                    bodyItem.setUseDebt(isChecked);
                    break;
                case R.id.cbPenalty:
                    bodyItem.setUsePenalty(isChecked);
                    break;
            }
            setDataView(bodyItem);
        }
    }

    @Override
    public void checkPaymentResponse(int statusCode, CheckPaymentsResponse response, String errorMessage, boolean isBalance) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setInvoiceCheckedStatus(true);
            enableDisableView(linMain, false);
            enableDisableView(btnPayment, true);
            linFee.setVisibility(View.VISIBLE);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });

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

            Log.i("ITINVO", "isTemplateIAPF = " + isTemplate);
            Log.i("ITINVO", "isTemplateIAPF = " + getArguments().getBoolean("isTemplate"));
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

    private void setTariffData(InvoiceContainerItem.BodyItem bodyItem, double occupantsCount) {
        tvIfOneTarif.setVisibility(View.GONE);
        double difCount = bodyItem.getLastCount() - bodyItem.getPrevCount();
        if (difCount > 0 && bodyItem.IsComplexCalculations) {
            tvIfOneTarif.setVisibility(View.GONE);
            if (difCount <= bodyItem.getMinTariffThreshold() * occupantsCount) {
                setMinTariffInfo(bodyItem, occupantsCount);
                linMiddleTariff.setVisibility(View.GONE);
                linMaxTariff.setVisibility(View.GONE);
            } else if (difCount <= bodyItem.getMiddleTariffThreshold() * occupantsCount) {
                setMinTariffInfo(bodyItem, occupantsCount);

                tvMiddleThreshold.setText(String.format("%s %s %s", getString(R.string.off),
                        bodyItem.getMinTariffThreshold() * occupantsCount, bodyItem.getMeasure()));

                tvMiddleTariffValue.setText(String.format("%s %s %s", getFormattedBalance(
                        bodyItem.getMiddleTariffValue(), "KGS"), getString(R.string.behind), bodyItem.getMeasure()));
                linMaxTariff.setVisibility(View.GONE);
            } else {
                setMinTariffInfo(bodyItem, occupantsCount);

                tvMiddleThreshold.setText(String.format("%s %s %s %s %s %s", getString(R.string.off),
                        bodyItem.getMinTariffThreshold() * occupantsCount, bodyItem.getMeasure(),
                        getString(R.string.before), bodyItem.getMiddleTariffThreshold(), bodyItem.getMeasure()));

                tvMiddleTariffValue.setText(String.format("%s %s %s", getFormattedBalance(bodyItem.getMiddleTariffValue(), "KGS"), getString(R.string.behind), bodyItem.getMeasure()));
                tvMaxThreshold.setText(String.format("%s %s %s", getString(R.string.off), bodyItem.getMiddleTariffThreshold(), bodyItem.getMeasure()));
                tvMaxTariffValue.setText(String.format("%s %s %s", getFormattedBalance(bodyItem.getMaxTariffValue(), "KGS"), getString(R.string.behind), bodyItem.getMeasure()));
            }
        } else if (!bodyItem.IsComplexCalculations) {
            tvIfOneTarif.setVisibility(View.VISIBLE);
            tvIfOneTarif.setText((String.format("%s", getFormattedBalance(bodyItem.getMinTariffValue(), "KGS"))));
            linMinTariff.setVisibility(View.GONE);
            linMiddleTariff.setVisibility(View.GONE);
            linMaxTariff.setVisibility(View.GONE);
        } else {
            tvIfOneTarif.setVisibility(View.GONE);
            tvTariffLabel.setVisibility(View.GONE);
            linMinTariff.setVisibility(View.GONE);
            linMiddleTariff.setVisibility(View.GONE);
            linMaxTariff.setVisibility(View.GONE);
        }
    }

    private void setMinTariffInfo(InvoiceContainerItem.BodyItem bodyItem, double occupantsCount) {
        tvMinThreshold.setText(String.format("%s %s %s", getString(R.string.before),
                bodyItem.getMinTariffThreshold() * occupantsCount, bodyItem.getMeasure()));
        tvMinTariffValue.setText((String.format("%s %s %s", getFormattedBalance(bodyItem.getMinTariffValue(), "KGS"), getString(R.string.behind), bodyItem.getMeasure())));
    }

    private void setDataView(InvoiceContainerItem.BodyItem bodyItem) {
        getInvoiceBodyParameters();
        edAmount.removeTextChangedListener(this);
        double difCount = bodyItem.getLastCount() - bodyItem.getPrevCount();
        if (difCount > 0) {
            tvDifCount.setText(doubleFormatter(difCount));
        } else {
            tvDifCount.setText(doubleFormatter(0.0));
        }

        if (bodyItem.getDebtInfo() != 0) {
            if (bodyItem.getDebtInfo() > 0) {
                cbDebt.setText(R.string.overpayment);
            } else {
                cbDebt.setText(R.string.pay_debt);
            }
            linDebt.setVisibility(View.VISIBLE);
            tvDebt.setText(doubleFormatter(bodyItem.getDebtInfo()));
            cbDebt.setChecked(bodyItem.isUseDebt());
        } else {
            linDebt.setVisibility(View.GONE);
        }

        if (bodyItem.getPenalty() > 0) {
            linPenalty.setVisibility(View.VISIBLE);
            tvPenalty.setText(doubleFormatter(bodyItem.getPenalty()));
            cbPenalty.setChecked(bodyItem.isUsePenalty());
        } else {
            linPenalty.setVisibility(View.GONE);
        }
        /*tvTotalServiceText.setText(String.format("%s %s:",getString(R.string.in_total),
                invoiceItem.invoiceBody.getItems().get(position).getServiceName()));*/
        tvTotalServiceText.setText(String.format("%s %s:", getString(R.string.in_total),
                bodyItem.getServiceName()));

        /*Log.i("FIXSUMSETTING","prepare");
        if (calculateServiceSum(bodyItem,invoiceItem.invoiceBody.OccupantsCount, false) > 0){
            Log.i("FIXSUMSETTING","if");
            edAmount.setText(doubleFormatter(calculateServiceSum(bodyItem,invoiceItem.invoiceBody.OccupantsCount, false)));
        } else {
            Log.i("FIXSUMSETTING","else");
            edAmount.addTextChangedListener(this);
            edAmount.setText(doubleFormatter(bodyItem.getFixSum()));
            edAmount.removeTextChangedListener(this);
        }*/
        edAmount.setText(doubleFormatter(calculateServiceSum(bodyItem, invoiceItem.invoiceBody.OccupantsCount, false)));

        //tvTotalServiceAmount.setText(doubleFormatter(calculateServiceSum(bodyItem,invoiceItem.invoiceBody.OccupantsCount, true)));

        //InvoiceContainerItem itemInv;
        if (calculateServiceSum(bodyItem, invoiceItem.invoiceBody.OccupantsCount, true) < 0) {
            tvTotalServiceAmount.setText("0");
        } else {
            tvTotalServiceAmount.setText(doubleFormatter(calculateServiceSum(bodyItem, invoiceItem.invoiceBody.OccupantsCount, true)));
        }
        tvTotalAmount.setText(getFormattedBalance(calculateTotalSum(), "KGS"));
        edAmount.addTextChangedListener(this);
    }

    private void setMetersData(InvoiceContainerItem.BodyItem bodyItem) {
        edLastCount.setText(doubleFormatter(bodyItem.getLastCount()));
        edPrevCount.setText(doubleFormatter(bodyItem.getPrevCount()));
    }

    @Override
    public void taxDictResponse(int statusCode, TaxDictResponse response, String errorMessage) {
    }
}
