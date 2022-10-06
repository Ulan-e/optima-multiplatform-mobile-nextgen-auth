package kz.optimabank.optima24.fragment.authorization.viewModel;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kg.optima.mobile.BuildConfig;
import kz.optimabank.optima24.fragment.authorization.source.RegisterPushTokenAction;
import kz.optimabank.optima24.fragment.authorization.source.RegisterPushTokenActionImpl;
import kz.optimabank.optima24.notifications.models.RegistrationTokenBody;
import kz.optimabank.optima24.notifications.models.RegistrationTokenResponse;
import retrofit2.Response;

import static kz.optimabank.optima24.utility.Constants.APP_NAME;
import static kz.optimabank.optima24.utility.Constants.DEVICE_OS;
import static kz.optimabank.optima24.utility.Constants.PUSH_TAG;

public class RegistrationPushViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;
    private RegisterPushTokenAction registerPushTokenAction;

    private MutableLiveData<RegistrationTokenResponse> registerToken;

    public RegistrationPushViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        registerPushTokenAction = new RegisterPushTokenActionImpl(application);
    }

    public LiveData<RegistrationTokenResponse> getRegisterToken(String clientId, String login) {
        if (registerToken == null) {
            registerToken = new MutableLiveData<>();
            RegistrationTokenBody body = getRegistrationBody(clientId, login);
            requestRegisterToken(body);
        }
        return registerToken;
    }

    // регистрация токена пуша
    private void requestRegisterToken(RegistrationTokenBody requestBody) {

        compositeDisposable.add(registerPushTokenAction.registerToken(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<RegistrationTokenResponse>>() {
                    @Override
                    public void onSuccess(Response<RegistrationTokenResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null)
                                registerToken.setValue(response.body());
                        }
                        Log.d(PUSH_TAG, "success register code " + response.code());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(PUSH_TAG, "register token error " + e.getLocalizedMessage());
                    }
                })
        );
    }

    // request body отправляемые при регистрации на сервис пушей
    private RegistrationTokenBody getRegistrationBody(String clientId, String token) {
        RegistrationTokenBody requestBody = new RegistrationTokenBody();
        requestBody.setPushToken(token);
        requestBody.setClientId(clientId);

        // инфо об операционной системе устройства
        RegistrationTokenBody.DeviceOS os = new RegistrationTokenBody.DeviceOS();
        os.setVersion(Build.VERSION.RELEASE);
        os.setPlatform(DEVICE_OS);
        requestBody.setDeviceOS(os);

        // инфо об приложение Optima24
        RegistrationTokenBody.App app = new RegistrationTokenBody.App();
        app.setName(APP_NAME);
        app.setVersion(BuildConfig.VERSION_NAME);
        requestBody.setApp(app);
        return requestBody;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}