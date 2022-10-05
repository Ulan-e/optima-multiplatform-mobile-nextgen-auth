package kz.optimabank.optima24.db.entry;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DigitizedCard extends RealmObject{
    private String userPhone;
    private int code;
    private String number;
    private String currency;
    private String name;
    private String cardholderName;
    private String brand;
    private String productName;
    private int cardAccountCode;
    private Date expireDate;
    private boolean isMultiBalance;
    @PrimaryKey
    private String rbsNumber;
    private byte[] byteArrayFullImg;
    private byte[] byteArrayMiniatureImg;
    private boolean isDefault;
    private boolean cardSuspended;
    private boolean cardInactive;
    private String tokenId;
    private boolean isBlocked;
    private boolean isVisible;
    private boolean isClosed;
    private boolean needReplenish;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCardAccountCode() {
        return cardAccountCode;
    }

    public void setCardAccountCode(int cardAccountCode) {
        this.cardAccountCode = cardAccountCode;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isMultiBalance() {
        return isMultiBalance;
    }

    public void setMultiBalance(boolean multiBalance) {
        isMultiBalance = multiBalance;
    }

    public String getRbsNumber() {
        return rbsNumber;
    }

    public void setRbsNumber(String rbsNumber) {
        this.rbsNumber = rbsNumber;
    }

    public byte[] getByteArrayFullImg() {
        return byteArrayFullImg;
    }

    public void setByteArrayFullImg(byte[] byteArrayFullImg) {
        this.byteArrayFullImg = byteArrayFullImg;
    }

    public byte[] getByteArrayMiniatureImg() {
        return byteArrayMiniatureImg;
    }

    public void setByteArrayMiniatureImg(byte[] byteArrayMiniatureImg) {
        this.byteArrayMiniatureImg = byteArrayMiniatureImg;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isCardSuspended() {
        return cardSuspended;
    }

    public void setCardSuspended(boolean cardSuspended) {
        this.cardSuspended = cardSuspended;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isCardInactive() {
        return cardInactive;
    }

    public void setCardInactive(boolean cardInactive) {
        this.cardInactive = cardInactive;
    }

    public boolean isNeedReplenish() {
        return needReplenish;
    }

    public void setNeedReplenish(boolean needReplenish) {
        this.needReplenish = needReplenish;
    }
}
