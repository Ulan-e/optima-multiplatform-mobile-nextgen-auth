package kz.optimabank.optima24.utility;

import kz.optimabank.optima24.model.gson.response.AuthorizationResponse;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class UserDetailsUtils {

    public static String getUserInitials(){
        AuthorizationResponse.User user = GeneralManager.getInstance().getUser();
        String firstName = user.getFirstName().substring(0, 1);
        String middleName = user.getMiddleName().substring(0, 1);
        return user.getLastName() + firstName + ". " + middleName + ".";
    }

    public static String getUserIdn(){
        AuthorizationResponse.User user = GeneralManager.getInstance().getUser();
        return user.getIdn();
    }
}