package kz.optimabank.optima24.fragment.references;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.RequisitesAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.BankRequisitesResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class RequisiteDetailFragment extends ATFFragment {
    UserAccounts userAccounts;
    UserAccounts.Cards currentCard;
    UserAccounts.CheckingAccounts checkingAccounts;
    UserAccounts.DepositAccounts depositAccounts;

    BankRequisitesResponse bankRequisites;
    BankRequisitesResponse.RequisitesSwiftTransfer swiftRequisites;
    RequisitesAdapter adapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Boolean isCardRequisite;

    String currency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requisite_detail, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        Log.d("RequisiteDetailFragment", GeneralManager.getInstance().getUser().address.toLowerCase());
    }

    private void initViews() {
        ArrayList<String> lists = new ArrayList<>();
        BankRequisitesResponse.Data data = bankRequisites.getData();
        if (isCardRequisite) {
            //Другие валюты кроме сома
            if (swiftRequisites != null) { // Убрал проверку на multiBalance
                lists.add(swiftRequisites.getIntermediaryBank());
                lists.add(swiftRequisites.getBeneficiaryBank());
                String beneficiaryBank = swiftRequisites.getBeneficiary();
                Log.d("beneficiaryBank", beneficiaryBank);
                beneficiaryBank = beneficiaryBank.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                beneficiaryBank = beneficiaryBank.replace("<#BankAccount>", data.getAccount());
                beneficiaryBank = beneficiaryBank.replace("<#UserAccount>", GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number);
                beneficiaryBank = beneficiaryBank.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add(beneficiaryBank);
                String detailsOfPayment = swiftRequisites.getDetailOfPayments();
                detailsOfPayment = detailsOfPayment.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                detailsOfPayment = detailsOfPayment.replace("<#BankAccount>", data.getAccount());
                detailsOfPayment = detailsOfPayment.replace("<#UserAccount>", GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number);
                detailsOfPayment = detailsOfPayment.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add(detailsOfPayment);
            } else {
                lists.add("Банк Получатель: " + getString(R.string.bank_of_name) + " \n" + "Филиал: " + data.getBranchName() + "\n" + "Адрес: " + data.getAddresss() + "\n" + "Бик: " + data.getBic());
                lists.add("Получатель: " + GeneralManager.getInstance().getUser().fullName + " \n" + "Транзитный счёт №: " + data.getAccount() + " \n" + "Адрес: " + GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add("Назначение платежа (за что, номер инвойса, контракта, дата)" + " \n" + "Номер счёта: " + GeneralManager.getInstance().getCardAccountByCode(currentCard.cardAccountCode).number);
            }
        } else {
            //Сом
            if (swiftRequisites != null) {
                lists.add(swiftRequisites.getIntermediaryBank());
                lists.add(swiftRequisites.getBeneficiaryBank());
                String beneficiaryBank = swiftRequisites.getBeneficiary();
                Log.d("beneficiaryBank", beneficiaryBank);
                beneficiaryBank = beneficiaryBank.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                beneficiaryBank = beneficiaryBank.replace("<#BankAccount>", data.getAccount());
                beneficiaryBank = beneficiaryBank.replace("<#UserAccount>", userAccounts.number);
                beneficiaryBank = beneficiaryBank.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add(beneficiaryBank);
                String detailsOfPayment = swiftRequisites.getDetailOfPayments();
                detailsOfPayment = detailsOfPayment.replace("<#UserName>", GeneralManager.getInstance().getUser().fullName);
                detailsOfPayment = detailsOfPayment.replace("<#BankAccount>", data.getAccount());
                detailsOfPayment = detailsOfPayment.replace("<#UserAccount>", userAccounts.number);
                detailsOfPayment = detailsOfPayment.replace("<#UserAddress>", GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add(detailsOfPayment);
            } else {
                lists.add("Банк Получатель" + ": " + getString(R.string.bank_of_name) + " \n" + "Филиал: " + data.getBranchName() + "\n" + "Адрес: " + data.getAddresss() + "\n" + "Бик: " + data.getBic());
                lists.add("Получатель" + ": " + GeneralManager.getInstance().getUser().fullName + " \n" + "Транзитный счёт №: " + data.getAccount() + " \n" + "Адрес: " + GeneralManager.getInstance().getUser().address.toLowerCase());
                lists.add("Назначение платежа (за что, номер инвойса, контракта, дата)" + " \n" + "Номер счёта: " + userAccounts.number);
            }
        }
        adapter = new RequisitesAdapter(lists, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void getBundle() {
        if (getArguments() != null) {
            userAccounts = (UserAccounts) getArguments().getSerializable("account");
            bankRequisites = (BankRequisitesResponse) getArguments().getSerializable("response");
            swiftRequisites = (BankRequisitesResponse.RequisitesSwiftTransfer) getArguments().getSerializable("swift");
            isCardRequisite = requireArguments().getBoolean("isCardRequisite");
            currency = getArguments().getString("currency");
            if (getArguments().getString("isFrom") != null) {
                String isFrom = getArguments().getString("isFrom");
                switch (isFrom) {
                    case "deposit":
                        depositAccounts = (UserAccounts.DepositAccounts) userAccounts;
                        break;
                    case "cheking":
                        checkingAccounts = (UserAccounts.CheckingAccounts) userAccounts;
                        break;
                }
            } else {
                currentCard = (UserAccounts.Cards) userAccounts;
            }
        }
    }

}