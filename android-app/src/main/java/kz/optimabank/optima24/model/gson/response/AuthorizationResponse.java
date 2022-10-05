package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

/**
  Created by Timur on 12.02.2017.
 */

public class AuthorizationResponse {
    @SerializedName("AccessToken")
    public String accessToken;
    @SerializedName("SessionId")
    public String session_id;
    @SerializedName("StartDateTime")
    public String startDateTime;
    @SerializedName("LastUpdate")
    public String lastUpdate;
    @SerializedName("User")
    public User user;
    @SerializedName("Duration")
    public int sessionDuration;

    public User getUser() {
        return user;
    }

    public static class User {
        @SerializedName("FirstName")
        public String firstName;
        @SerializedName("LastName")
        public String lastName;
        @SerializedName("MiddleName")
        public String middleName;
        @SerializedName("FullName")
        public String fullName;
        @SerializedName("Idn")
        public String idn;
        @SerializedName("Sex")
        public String sex;
        @SerializedName("Login")
        public String login;
        @SerializedName("Address")
        public String address;
        @SerializedName("AutoEncrypt")
        public boolean autoEncrypt;
        @SerializedName("MobilePhoneNumber")
        public String mobilePhoneNumber;
        @SerializedName("ImageHash")
        public String imageHash;

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setIdn(String idn) {
            this.idn = idn;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setAutoEncrypt(boolean autoEncrypt) {
            this.autoEncrypt = autoEncrypt;
        }

        public void setMobilePhoneNumber(String mobilePhoneNumber) {
            this.mobilePhoneNumber = mobilePhoneNumber;
        }

        public void setImageHash(String imageHash) {
            this.imageHash = imageHash;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public String getFullName() {
            return fullName;
        }

        public String getIdn() {
            return idn;
        }

        public String getSex() {
            return sex;
        }

        public String getLogin() {
            return login;
        }

        public String getAddress() {
            return address;
        }

        public boolean isAutoEncrypt() {
            return autoEncrypt;
        }

        public String getMobilePhoneNumber() {
            return mobilePhoneNumber;
        }

        public String getImageHash() {
            return imageHash;
        }
        @SerializedName("BankId")
        public String bankId;

        public String getBankId() {
            return bankId;
        }
    }
}