package kz.optimabank.optima24.fragment.confirm;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.template.CreateTemplateFragment;
import kz.optimabank.optima24.model.interfaces.Accounts;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountsImpl;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.IS_HIDE_CREATE_TEMPLATE;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

/**
 * Created by Timur on 15.05.2017.
 */

public class SuccessOperation extends ATFFragment implements View.OnClickListener, AccountsImpl.UpdateCallback,
        TransferAndPaymentImpl.UpdateCallback {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.textInfo)
    TextView textInfo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvOperationDescription)
    TextView tvOperationDescription;
    @BindView(R.id.linCreateTemplate)
    LinearLayout linCreateTemplate;
    @BindView(R.id.btn_create_template)
    Button btnCreateTemplate;

    TransferAndPayment transferAndPayment;
    boolean isTransfer, isPayment, isTemplate, isChange, isClickToTemplateGridView, isTemplateExist, isApplication, customText, isCreateTemplate, isTransferTemplateChange, isPaymentTemplateChange, isInterBankSwiftTransfer, isInterBankInSomTemplateChange;
    String paymentTitle, operationCurrency, isTTF, isTransferAtTempl, customTextS, sumWithAmount;
    double feeWithAmount, amount;
    public boolean isTemp = false;
    Accounts accounts;
    public boolean isTempl;
    int isTTA = 9;
    private int isHideCreateTemplate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.success_operation, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        checkIsCreateTemplate();
        hideSoftKeyBoard(getContext());
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvOperationDescription);
        initToolbar();
        btnCreateTemplate.setOnClickListener(this);

        if (isTemplateExist) {
            linCreateTemplate.setVisibility(View.INVISIBLE);
        }
        if (isTemplate || isClickToTemplateGridView || isApplication) {
            linCreateTemplate.setVisibility(View.GONE);
        } else {
            btnCreateTemplate.setOnClickListener(this);
        }

        if(isHideCreateTemplate == 1){
            linCreateTemplate.setVisibility(View.GONE);
        }
        setOperationDescription();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideSoftKeyBoard(requireActivity());
        Log.d("TAG", "isChange = " + isChange);
        if (GeneralManager.getInstance().isNeedToUpdateAccounts() && isChange) {
            accounts = new AccountsImpl();
            accounts.registerUpdateCallBack(this);
            accounts.getAccounts(requireActivity(), false, true);
        }
        GeneralManager.getInstance().setNeedToUpdateAccounts(true);
        GeneralManager.getInstance().setNeedToUpdateHistory(true);
        GeneralManager.getInstance().setNeedToUpdatePayTempl(true); // TODO: 17.02.2021 update Template
        GeneralManager.getInstance().setNeedToUpdateTemplate(true);
        if (customText) {
            textInfo.setText(customTextS);
        }
        if (isTemplate) {
            transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerUpdateCallBack(this);
            if (isTransfer) {
                transferAndPayment.getTransferTemplate(requireActivity());
            } else if (isPayment) {
                transferAndPayment.getPaymentSubscriptions(requireActivity());  // todo just else without {
            }
        }
    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setNeedToUpdateTemplate(true);
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonTransferSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setNeedToUpdateTemplate(true);
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_template:
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        ((SmsConfirmActivity) requireContext()).openFragment(new CreateTemplateFragment());
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view, animatorListener);
                break;
        }
    }


    private void initToolbar() {
        if (isTransfer) {
            tvTitle.setText(getString(R.string.transfer_title));
        } else if (isPayment) {
            tvTitle.setText(getString(R.string.t_payments_title));
        } else if (isCreateTemplate) {
            tvTitle.setText(getString(R.string.create_template_));
            linCreateTemplate.setVisibility(View.GONE);
        }
        toolbar.setTitle("");
        ((OptimaActivity) (requireActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBundle();
                Log.i("isTTF101", "" + isTTF);
                if (isTTF != null) {
                    if (isTTF.equals("1")) {
                        requireActivity().onBackPressed();
                        requireActivity().finish();
                    }
                }
                requireActivity().finish();

                if (getArguments().getBoolean("isApplication")) {
                    getFragmentManager().popBackStack();
                } else {
                    requireActivity().onBackPressed();
                }
                getFragmentManager().popBackStack();
            }

        });
    }


    private void getBundle() {
        if (getArguments() != null) {
            isTTF = getArguments().getString("isTTF");
            isTTA = getArguments().getInt("isTTA");
            isTransferAtTempl = getArguments().getString("isTransferAtTempl");
            isTemplateExist = getArguments().getBoolean("isTemplateExist", false);
            isTransfer = getArguments().getBoolean("isTransfer", false);
            isClickToTemplateGridView = getArguments().getBoolean("isClickToTemplateGridView", false);
            isPayment = getArguments().getBoolean("isPayment", false);
            isCreateTemplate = getArguments().getBoolean("isCreateTemplate");
            isTemplate = getArguments().getBoolean("isTemplate", false);
            customText = getArguments().getBoolean("customText", false);
            isChange = getArguments().getBoolean("isChange", false);
            operationCurrency = getArguments().getString("operationCurrency");
            customTextS = getArguments().getString("customTextS");
            feeWithAmount = getArguments().getDouble("feeWithAmount", 0);
            amount = getArguments().getDouble("amount", 0);
            isTransferTemplateChange = getArguments().getBoolean("isTransferTemplateChange");
            isPaymentTemplateChange = getArguments().getBoolean("isPaymentTemplateChange");
            isInterBankSwiftTransfer = getArguments().getBoolean("isTransferInterBankSwift");
            isApplication = getArguments().getBoolean("isApplication", false);
            isInterBankInSomTemplateChange = getArguments().getBoolean("isInterbankTemplate");
            isHideCreateTemplate = getArguments().getInt(IS_HIDE_CREATE_TEMPLATE);
            if (isPayment) {
                paymentTitle = getArguments().getString("paymentTitle");
                sumWithAmount = getArguments().getString("sumWithAmount");
            } else if (isInterBankSwiftTransfer) {

                sumWithAmount = getArguments().getString("totalAmount");
                operationCurrency = getArguments().getString("FeeCurrency");
            }
        }
    }

    public void checkIsCreateTemplate() {
        if (isTransferTemplateChange || isPaymentTemplateChange || isInterBankInSomTemplateChange || isTemplate) {
            linCreateTemplate.setVisibility(View.GONE);
            tvOperationDescription.setVisibility(View.GONE);
        } else {
            linCreateTemplate.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setOperationDescription() {
        if (feeWithAmount != 0 || sumWithAmount != null) {
            if (isTransfer) {
                tvOperationDescription.setText(getResources().getString(R.string.transfer_amount) + " " + getFormattedBalance(amount, operationCurrency));
            } else if (isPayment) {
                tvOperationDescription.setText(getResources().getString(R.string.payment_amount) + " " + sumWithAmount);
            } else if (isCreateTemplate) {
                tvOperationDescription.setText(getResources().getString(R.string.template_create_successfully) + "");
            } else if (isInterBankSwiftTransfer) {
                tvOperationDescription.setText(getResources().getString(R.string.payment_amount) + " " + sumWithAmount + " " + operationCurrency);
            }
        } else {
            tvOperationDescription.setVisibility(View.GONE);
        }
    }

    @Override
    public void jsonAccountsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setNeedToUpdateAccounts(true);
            GeneralManager.getInstance().setNeedToUpdateHistory(true);
        } else {
            onError(errorMessage);
        }
    }
}
