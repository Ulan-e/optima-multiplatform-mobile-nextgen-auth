package kz.optimabank.optima24.fragment.authorization.source;

import io.reactivex.Single;
import kz.optimabank.optima24.notifications.models.RegistrationTokenBody;
import kz.optimabank.optima24.notifications.models.RegistrationTokenResponse;
import retrofit2.Response;

public interface RegisterPushTokenAction {

    // регистрация токена на новый пользователь
    Single<Response<RegistrationTokenResponse>> registerToken(RegistrationTokenBody body);
}