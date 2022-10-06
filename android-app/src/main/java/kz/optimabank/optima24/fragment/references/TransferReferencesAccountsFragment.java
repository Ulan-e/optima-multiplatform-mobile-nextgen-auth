package kz.optimabank.optima24.fragment.references;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.CommonStatusCodes;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SelectParameterActivity;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.fragment.transfer.TransferAccountsFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.DICTIONARY_KEY;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_TO_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CITIZENSHIP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CURRENCY_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_KNP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

/**
 * Created by Timur on 16.06.2017.
 */

public class TransferReferencesAccountsFragment extends TransferAccountsFragment {
    private static final String TAG = TransferReferencesAccountsFragment.class.getSimpleName();

    boolean isRefillDeposit, isRefillWish, isRefillAccount;
    int NON_PRODUCT_TYPE = -10;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "accountFrom = " + accountFrom);
        Log.d(TAG, "accountTo = " + accountTo);
        if (accountFrom != null) {
            setAccountSpinnerFrom(accountFrom);
            setCurrency();
            setEdSpinnerToParams();
            getCurrencyAccountFrom();
            clickSpinnerTo();
        } else if (accountTo != null) {
            if (Replenish) {
                isRefillAccount = true;
                setAccountSpinnerTo(accountTo);
            } else {
                if (accountTo instanceof UserAccounts.DepositAccounts) {
                    UserAccounts.DepositAccounts depositAccounts = (UserAccounts.DepositAccounts) accountTo;
                    if (depositAccounts instanceof UserAccounts.WishAccounts) {
                        isRefillWish = true;
                    } else {
                        isRefillDeposit = true;
                    }
                    setAccountSpinnerTo(accountTo);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("TAG", "onActivityResult TRAF");
        Log.i("TAG", "requestCode Traf = " + requestCode);
        Log.i("TAG", "resultCode Traf = " + resultCode);
        Log.i("TAG", "data Traf = " + data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            ifChange = true;
            if (resultCode == Activity.RESULT_OK) {
                // TODO SCAN card
//                Card card = data.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD);
//                //etReceiverName.getText().clear();
//                edSpinnerTo.setText(card.getCardNumber());
            } else if (resultCode == 101) {
                edSpinnerTo.getText().clear();
                edSpinnerTo.requestFocus();
            }
        }

        if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
            if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
                if (!isRefillDeposit && !isRefillWish && !isRefillAccount) {
                    if (accountFrom instanceof UserAccounts.Cards && !(data.getSerializableExtra(ACCOUNT_KEY) instanceof UserAccounts.Cards) ||
                            !(accountFrom instanceof UserAccounts.Cards) && data.getSerializableExtra(ACCOUNT_KEY) instanceof UserAccounts.Cards) {
                        resetSpinnerTo();
                    }
                    accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                    if (accountTo != null) {
                        if (accountFrom.code == accountTo.code) {
                            resetSpinnerTo();
                        }
                    }
                }
                tvSpinnerFrom.setError(null);
                accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                if (accountTo != null) {
                    if (accountFrom.code == accountTo.code) {
                        resetSpinnerTo();
                    }
                }
                setAccountSpinnerFrom(accountFrom);
                setEdSpinnerToParams();
                getCurrencyAccountFrom();
                getCurrencyAccountTo();
                setCurrency();
                needSendFeeRespAfterGetAccData = false;
                fetchAccountData();
                clickSpinnerTo();
            } else if (requestCode == SELECT_ACCOUNT_TO_REQUEST_CODE) {
                accountTo = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                isForeignCardIsElkart = false;
                isCheking = false;
                if (accountTo.code == Constants.NEW_CARD_ID || (accountFrom != null && accountFrom.code == accountTo.code)) {
                    resetSpinnerTo();
                    if (accountFrom instanceof UserAccounts.Cards) {
                        scan.setVisibility(View.VISIBLE);
                    }
                } else {
                    scan.setVisibility(View.GONE);
                    setAccountSpinnerTo(accountTo);
                    getCurrencyAccountTo();
                    setCurrency();
                    clickSpinnerTo();
                }
            } else if (requestCode == SELECT_CURRENCY_REQUEST_CODE) {
                String currency = (String) data.getSerializableExtra(STRING_KEY);
                this.currency = currency;
                tvCurrency.setText(currency);
                if (accountFrom != null && accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).isMultiBalance) {
                    for (UserAccounts.Cards.MultiBalanceList multi : ((UserAccounts.Cards) accountFrom).multiBalanceList) {
                        if (this.currency.equals(multi.currency)) {
                            String balance = getFormattedBalance(multi.amount, currency);
                            tvFromAccountBalance.setText(balance);
                        }
                    }
                }
            } else if (requestCode == SELECT_CITIZENSHIP_REQUEST_CODE) {
                String resident = (String) data.getSerializableExtra(STRING_KEY);
                tvSpinnerCitizenship.setError(null);
                tvSpinnerCitizenship.setText(resident);
                isResident = resident.equals(getResources().getString(R.string.text_resident));
            } else if (requestCode == SELECT_KNP_REQUEST_CODE) {
                knp = (Dictionary) data.getSerializableExtra(DICTIONARY_KEY);
                tvSpinnerKNP.setError(null);
                tvSpinnerKNP.setText(knp.getDescription());
            } else if (requestCode == SELECT_COUNTRY_FOR_REG_MASTERCARD_REQUEST_CODE) {
                Dictionary countryForRegMaster = (Dictionary) data.getSerializableExtra(DICTIONARY_KEY);
                etCountry.setText(countryForRegMaster.getCode() + " - " + countryForRegMaster.getDescription());
                tvCurrency.setText(getString(R.string.tenge_icon));
                isoCodeCountry = countryForRegMaster.getCode();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linSpinnerFrom:

                intent = new Intent(getActivity(), SelectAccountActivity.class);
                if (isRefillDeposit || isRefillWish || isRefillAccount) {
                    intent.putExtra("refillDeposit", true);
                } else {
                    intent.putExtra("transferAccFrom", true);
                }
                if (accountTo != null && accountTo instanceof UserAccounts.CheckingAccounts) {
                    intent.putExtra("exceptElcart", true);
                }
                if (accountTo != null && accountTo.number.length() < 16) {
                    intent.putExtra("depVos", true);
                } else {
                    intent.putExtra("depVos", false);
                }
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                break;
            case R.id.imgSpinnerTo:
                actionForSpinnerTo();
                edSpinnerTo.setText("");
                break;
            case R.id.scan:
                if (accountFrom != null) {
                    if (accountFrom instanceof UserAccounts.Cards) {
                        scanCard();
                    }
                }
                break;
            case R.id.linSpinnerTo:
                actionForSpinnerTo();
                break;
            case R.id.edSpinnerTo:
                actionForSpinnerTo();
                break;
            case R.id.linCurrency:
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("Currency", true);
                intent.putExtra("listCurrency", stringList);
                startActivityForResult(intent, SELECT_CURRENCY_REQUEST_CODE);
                break;
            case R.id.linSpinnerCitizenship:
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("citizenship", true);
                intent.putExtra("citizenshipList", citizenshipList);
                startActivityForResult(intent, SELECT_CITIZENSHIP_REQUEST_CODE);
                break;
            case R.id.linSpinnerKNP:
                intent = new Intent(getActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.penalty_knp));
                startActivityForResult(intent, SELECT_KNP_REQUEST_CODE);
                break;
            case R.id.btnTransfer:
                if (!isGetAccountDataRequested) {        // проверяем, отправлялся ли запрос GetAccData посредством скрытия клавиатуры
                    needSendFeeRespAfterGetAccData = true;                  //true потому что после возврата GetAccData нужно запрос комиссии отправить сразу
                    fetchAccountData();
                }
                if ((accountFrom instanceof UserAccounts.Cards || accountFrom instanceof UserAccounts.CheckingAccounts) && accountTo == null) {
                    if (edSpinnerTo.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                        if (checkField() && isBackPressed && !isShowFeeInfo && !needSendFeeRespAfterGetAccData) {
                            transfer.checkMt100Transfer(requireContext(), getBody(NON_PRODUCT_TYPE));
                        } else if (isShowFeeInfo) {
                            transfer.registerCallbackConfirm(this);
                            transfer.confirmMt100Transfer(requireContext(), getBody(NON_PRODUCT_TYPE));
                            isShowFeeInfo = false;
                        }
                    }
                } else {
                    if (checkField() && isBackPressed) {
                        transfer.checkMt100Transfer(requireContext(), getBody(NON_PRODUCT_TYPE));
                    } else if (!isBackPressed) {
                        transfer.registerCallbackConfirm(this);
                        transfer.confirmMt100Transfer(requireContext(), getBody(NON_PRODUCT_TYPE));
                        isShowFeeInfo = false;
                    }
                }
                break;
            case R.id.linSelectCountry:
                intent = new Intent(parentActivity, SelectParameterActivity.class);
                intent.putExtra("parameterName", getString(R.string.country));
                startActivityForResult(intent, SELECT_COUNTRY_FOR_REG_MASTERCARD_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void actionForSpinnerTo() {
        Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
        if (isRefillDeposit) {
            intent.putExtra("onlyDeposit", true);
            startActivityForResult(intent, SELECT_ACCOUNT_TO_REQUEST_CODE);
        } else if (isRefillWish) {
            intent.putExtra("refillWish", true);
            startActivityForResult(intent, SELECT_ACCOUNT_TO_REQUEST_CODE);
        } else if (isRefillAccount) {
            intent.putExtra("refillDeposit", true);
            if (accountFrom != null && accountFrom instanceof UserAccounts.CheckingAccounts) {
                intent.putExtra("exceptElcart", true);
            }
            startActivityForResult(intent, SELECT_ACCOUNT_TO_REQUEST_CODE);
        } else if (accountFrom != null) {
            /*if(accountFrom instanceof UserAccounts.CheckingAccounts ||
                    accountFrom instanceof UserAccounts.CardAccounts ||
                    accountFrom instanceof UserAccounts.DepositAccounts) {
                intent.putExtra("checking_Card_Deposit_Accounts", true);
            } else {
                intent.putExtra("isCards", true);
            }*/
            if (accountFrom instanceof UserAccounts.CheckingAccounts) {
                intent.putExtra("isCardsAndChekingAndDepositAccounts", true);
                intent.putExtra("exceptElcart", true);
            } else if (accountFrom instanceof UserAccounts.CardAccounts) {
                intent.putExtra("isChekingAndDepositAccounts", true);
            } else if (accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).brandType == 1) {
                intent.putExtra("isElcart", true);
            } else {
                intent.putExtra("isAccountsForDebitWithoutCardAcc", true);
                intent.putExtra("tvCurrency", currency);
                if (accountFrom instanceof UserAccounts.DepositAccounts)
                    intent.putExtra("exceptElcart", true);
            }
            intent.putExtra("accountFrom", accountFrom);
            startActivityForResult(intent, SELECT_ACCOUNT_TO_REQUEST_CODE);
        } else {
            tvSpinnerFrom.setFocusable(true);
            tvSpinnerFrom.setFocusableInTouchMode(true);
            tvSpinnerFrom.requestFocus();
            tvSpinnerFrom.setError(getString(R.string.error_empty));
        }
    }
}
