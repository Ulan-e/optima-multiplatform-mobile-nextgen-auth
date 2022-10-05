package kz.optimabank.optima24.fragment.references;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.TabAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.BankRequisitesResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.IRequisites;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.RequisitesImpl;
import kz.optimabank.optima24.utility.ShareUtility;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;

/**
 * Created by Timur on 31.05.2017.
 * Edited by Dastan 12.12.2020
 */

public class RequisitesFragment extends ATFFragment implements RequisitesImpl.Callback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.currency)
    TextView tVcurrency;

    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    @BindView(R.id.currency_cardview)
    CardView chooseCurrency;

    @BindView(R.id.currency_arrow)
    ImageView currencyArrow;

    @BindView(R.id.float_btn)
    FloatingActionButton share_btn;

    UserAccounts userAccounts;
    UserAccounts.Cards currentCard;


    IRequisites requisites;

    TabAdapter viewPagerAdapter;

    //переменная которая нужна чтобы взять из картачного и депозитного, счёт до востребования.
    String number = "";
    Boolean isCardRequisite;
    String isFrom;
    String currency;

    BankRequisitesResponse shareData;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requisites_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.float_btn)
    void onShareClicked() {
        String[] list = {getString(R.string.share), getString(R.string.export_pdf)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.how_share));
        builder.setItems(list, (dialogInterface, index) -> {
            switch (index) {
                case 0:
                    ShareUtility.shareViaText(requireContext(), getShareText());
                    Log.d("RequisitesFragment", getShareText());
                    break;
                case 1:
                    ShareUtility.exportToPdfFromHtml(requireContext(), shareData, userAccounts, isCardRequisite, tVcurrency.getText().toString());
                    break;
                case 2:
                    //ShareUtility.shareViaText(requireContext(),getShareText());
                    break;
            }
        });
        builder.show();
    }

    private String getShareText() {
        int index = 0;
        if (viewPager.getAdapter().getCount() != 0) {
            index = viewPager.getCurrentItem();
        }

        ArrayList<String> lists = new ArrayList<>();

        if (isCardRequisite) {
            if (shareData.getData().getRequisitesSwiftTransfer().size() != 0 && shareData.getData().getRequisitesSwiftTransfer() != null) {
                BankRequisitesResponse.RequisitesSwiftTransfer swiftRequisites = shareData.getData().getRequisitesSwiftTransfer().get(index);
                lists.add(swiftRequisites.getIntermediaryBank());
                lists.add(swiftRequisites.getBeneficiaryBank());
                String beneficiaryBank = swiftRequisites.getBeneficiary();
                Log.d("beneficiaryBank", beneficiaryBank);
                beneficiaryBank = beneficiaryBank.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                beneficiaryBank = beneficiaryBank.replace("<#BankAccount>", shareData.getData().getAccount());
                beneficiaryBank = beneficiaryBank.replace("<#UserAccount>", GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number);
                beneficiaryBank = beneficiaryBank.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add(beneficiaryBank);
                lists.add(swiftRequisites.getDetailOfPayments().replace("<#UserAccount>", GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number));
            } else {
                lists.add("Банк Получатель: " + getString(R.string.bank_of_name) + " \n" + "Филиал: " + shareData.getData().getBranchName() + "\n" + "Адрес: " + shareData.getData().getAddresss() + "\n" + "Бик: " + shareData.getData().getBic());
                lists.add("Получатель: " + GeneralManager.getInstance().getUser().fullName + " \n" + "Транзитный счёт №: " + shareData.getData().getAccount() + " \n" + "Адрес: " + GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add("Назначение платежа (за что, номер инвойса, контракта, дата)" + " \n" + "Номер счёта: " + GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number);
            }
        } else {
            if (shareData.getData().getRequisitesSwiftTransfer().size() != 0 && shareData.getData().getRequisitesSwiftTransfer() != null) {
                BankRequisitesResponse.RequisitesSwiftTransfer swiftRequisites = shareData.getData().getRequisitesSwiftTransfer().get(index);
                lists.add(swiftRequisites.getIntermediaryBank());
                lists.add(swiftRequisites.getBeneficiaryBank());
                String beneficiaryBank = swiftRequisites.getBeneficiary();
                Log.d("beneficiaryBank", beneficiaryBank);
                beneficiaryBank = beneficiaryBank.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                beneficiaryBank = beneficiaryBank.replace("<#BankAccount>", shareData.getData().getAccount());
                beneficiaryBank = beneficiaryBank.replace("<#UserAccount>", userAccounts.number);
                beneficiaryBank = beneficiaryBank.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add(beneficiaryBank);
                lists.add(swiftRequisites.getDetailOfPayments().replace("<#UserAccount>", userAccounts.number));
            } else {
                lists.add("Банк Получатель" + ": " + getString(R.string.bank_of_name) + " \n" + "Филиал: " + shareData.getData().getBranchName() + "\n" + "Адрес: " + shareData.getData().getAddresss() + "\n" + "Бик: " + shareData.getData().getBic());
                lists.add("Получатель" + ": " + GeneralManager.getInstance().getUser().fullName + " \n" + "Транзитный счёт №: " + shareData.getData().getAccount() + " \n" + "Адрес: " + GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add("Назначение платежа (за что, номер инвойса, контракта, дата)" + " \n" + "Номер счёта: " + userAccounts.number);
            }
        }

        StringBuilder text = new StringBuilder();

        for (String a : lists) {
            text.append(a);
            text.append("\n");
        }

        return text.toString();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initToolbar();
        getBundle();
        requisites = new RequisitesImpl();
        requisites.registerCallBack(this);
        progressDialog = Utilities.progressDialog(parentActivity, getString(R.string.t_loading));
        initDefaultCurrency();
    }

    private void chooseCurrency() {
        ArrayList<String> array = new ArrayList<>();
        if(currentCard.brand != null){
            if (!currentCard.brand.equals("elkart")) {
            if (currentCard.isMultiBalance) {// Если карта multiBalance взять валюты с запроса
                currencyArrow.setVisibility(View.VISIBLE);
                for (int i = 0; i < currentCard.multiBalanceList.size(); i++) {
                    array.add(currentCard.multiBalanceList.get(i).currency);
                }
            } else {
                currencyArrow.setVisibility(View.VISIBLE);
                array.add("EUR"); // Добавление валют для показа в диалоге
                array.add("RUB");
                array.add("KGS");
                array.add("USD");
            }
            String[] allCurrency = array.toArray(new String[0]);
            chooseCurrency.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.choose_currency));
                builder.setItems(allCurrency, (dialogInterface, index) -> {
                    getNewCurrency(array.get(index));
                    currency = array.get(index);  // Выбранная валюта отправляется для запроса
                });
                builder.show();
            });
        }

        }
    }


    private void initDefaultCurrency() {
        progressDialog.show();
        if (isCardRequisite) {
            // реквизиты карты
            if (currentCard.isMultiBalance) {
                currencyArrow.setVisibility(View.VISIBLE);

                // получение реквизитов для мульти баланса
                if (number != null) {
                    requisites.getBanksRequisites(
                            getContext(),
                            number.substring(3, 5),
                            currentCard.multiBalanceList.get(1).currency,
                            number
                    );
                }
                tVcurrency.setText(currentCard.multiBalanceList.get(1).currency);
                currency = currentCard.multiBalanceList.get(1).currency;
            } else {
                currencyArrow.setVisibility(View.GONE);

                // получение реквизитов
                if (number != null) {
                    requisites.getBanksRequisites(
                            getContext(),
                            number.substring(3, 5),
                            currentCard.currency,
                            number
                    );
                }
                tVcurrency.setText(currentCard.currency);
                currency = currentCard.currency;
            }
            chooseCurrency();
        } else {
            // получение реквизита депозита, расчётного счета
            currencyArrow.setVisibility(View.GONE);
            if (number != null) {
                requisites.getBanksRequisites(
                        getContext(),
                        userAccounts.number.substring(3, 5),
                        userAccounts.currency,
                        number
                );
            }
            tVcurrency.setText(userAccounts.currency);
            currency = userAccounts.currency;
            Log.d("RequisitesFragment", userAccounts.currency + userAccounts.number);
        }
    }

    private void getNewCurrency(String currency) {
        String number = GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number;

        // получение реквизита для новой валюты
        if (number != null) {
            requisites.getBanksRequisites(
                    getContext(),
                    number.substring(3, 5),
                    currency,
                    number
            );
        }
        tVcurrency.setText(currency);
    }

    private void initToolbar() {
        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((NavigationActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());
    }

    private void getBundle() {
        if (getArguments() != null) {
            userAccounts = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            isFrom = getArguments().getString("isFrom");
            //проверка для того чтобы достать номер счета из депозита и счета без карты
            if (requireArguments().getString("numberFromAccountDetail") != null) {
                number = requireArguments().getString("numberFromAccountDetail");
                isCardRequisite = false;
            } else {
                if (userAccounts != null) currentCard = (UserAccounts.Cards) userAccounts;
                number = GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number;
                isCardRequisite = true;
            }
        }
        Log.d("RequisitesFragment", "number is" + number);
    }

    @Override
    public void successBankRequisites(int statusCode, String errorMessage, BankRequisitesResponse response) {
        shareData = response;
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        Bundle bundle;
        RequisiteDetailFragment fragment;
        if (response.getData().getRequisitesSwiftTransfer() != null) {
            if (response.getData().getRequisitesSwiftTransfer().size() != 0) {
                for (BankRequisitesResponse.RequisitesSwiftTransfer requisitesSwiftTransfer : response.getData().getRequisitesSwiftTransfer()) {
                    bundle = new Bundle();
                    bundle.putBoolean("isCardRequisite", isCardRequisite);
                    bundle.putString("isFrom", isFrom);
                    Log.d("RequisitesFragment", requisitesSwiftTransfer.toString());
                    bundle.putSerializable(ACCOUNT, userAccounts);
                    bundle.putSerializable("response", response);
                    bundle.putSerializable("swift", requisitesSwiftTransfer);
                    bundle.putString("currency", currency);
                    fragment = new RequisiteDetailFragment();
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                    Log.d("RequisitesFragment", "CARD OTHER");
                    indicator.setVisibility(View.VISIBLE);
                }
                viewPagerAdapter = new TabAdapter(getChildFragmentManager(), fragments);
            } else {
                bundle = new Bundle();
                bundle.putBoolean("isCardRequisite", isCardRequisite);
                bundle.putString("isFrom", isFrom);
                bundle.putSerializable(ACCOUNT, userAccounts);
                bundle.putSerializable("response", response);
                bundle.putString("currency", currency);
                fragment = new RequisiteDetailFragment();
                fragment.setArguments(bundle);
                fragments.add(fragment);
                viewPagerAdapter = new TabAdapter(getChildFragmentManager(), fragments);
                indicator.setVisibility(View.GONE);
                Log.d("RequisitesFragment", "CARD KGS");
            }
            viewPager.setAdapter(viewPagerAdapter);
            indicator.setViewPager(viewPager);
            progressDialog.dismiss();
            Log.d("RequisitesFragment", "viewPager.setAdapter(viewPagerAdapter);" + userAccounts.number);
        }
    }
}