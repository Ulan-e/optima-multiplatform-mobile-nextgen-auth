package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
  Created by Timur on 28.02.2017.
 */

public class AccountsResponse {
    @SerializedName("Cards")
    public ArrayList<UserAccounts.Cards> cards;
    @SerializedName("CardAccounts")
    public ArrayList<UserAccounts.CardAccounts> cardsAccounts;
    @SerializedName("CheckingAccounts")
    public ArrayList<UserAccounts.CheckingAccounts> checkingAccounts;
    @SerializedName("CreditAccounts")
    public ArrayList<UserAccounts.CreditAccounts> credit;
    @SerializedName("DepositAccounts")
    public ArrayList<UserAccounts.DepositAccounts> deposits;
    @SerializedName("WishDeposits")
    public ArrayList<UserAccounts.WishAccounts> wishDeposits;
    @SerializedName("EncryptedCards")
    public ArrayList<UserAccounts.EncryptedCard> encryptedCard;
}
