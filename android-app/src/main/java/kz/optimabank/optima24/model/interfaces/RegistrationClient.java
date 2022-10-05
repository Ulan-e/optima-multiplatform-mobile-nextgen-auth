package kz.optimabank.optima24.model.interfaces;


import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.RegistrationClientImpl;

public interface RegistrationClient {
    void checkClient(Context context, JSONObject body);
    void getSecretQuestions(Context context);
    void requestSmsClientBank(Context context, JSONObject body);
    void requestSmsNoClientBank(Context context, JSONObject clientBody);
    void finishRegForClientBank(Context context, JSONObject body);
    void finishRegForNoClientBank(Context context, JSONObject body);
    void changeTempPassword(Context context, JSONObject body);
    void requestSmsAgain(Context context, String phoneNumber);
    void checkPhoneNumber(Context context, String phoneNumber);
    void setRequestSmsAgainCallback(RegistrationClientImpl.RequestSmsAgainCallback requestSmsAgainCallback);
    void setRequestSmsNoClientBankCallback(RegistrationClientImpl.RequestSmsNoClientBankCallback requestSmsNoClientBankCallback);
    void setFinishRegCallback(RegistrationClientImpl.FinishRegCallback finishRegForClientBankCallback);
    void setSecretQuestionsCallback(RegistrationClientImpl.SecretQuestionsCallback secretQuestionsCallback);
    void setCheckClientCallback(RegistrationClientImpl.CheckClientCallback checkClientCallback);
    void setRequestSmsClientBankCallback(RegistrationClientImpl.RequestSmsClientBankCallback requestSmsClientBankCallback);
    void setChangeTempPasswordCallback(RegistrationClientImpl.ChangeTempPasswordCallback changeTempPasswordCallback);
}
