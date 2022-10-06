package kz.optimabank.optima24.fragment.authorization.source;

import android.content.Context;

import java.util.Map;

import io.reactivex.Single;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.notifications.models.RegistrationTokenBody;
import kz.optimabank.optima24.notifications.models.RegistrationTokenResponse;
import retrofit2.Response;

import static kz.optimabank.optima24.utility.Constants.API_PUSH_URL;

public class RegisterPushTokenActionImpl implements RegisterPushTokenAction {

    private Context context;

    public RegisterPushTokenActionImpl(Context context) {
        this.context = context;
    }

    @Override
    public Single<Response<RegistrationTokenResponse>> registerToken(RegistrationTokenBody body) {
        return getApi().registerToken(body, getHeader());
    }

    private IApiMethods getApi() {
        ServiceGenerator request = new ServiceGenerator();
        return request.request(context, null, API_PUSH_URL, true, false);
    }

    private Map<String, String> getHeader() {
        String sessionId = GeneralManager.getInstance().getSessionId();
        return HeaderHelper.getOpenSessionHeader(context, sessionId);
    }
}