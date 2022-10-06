package kz.optimabank.optima24.secondary_registration.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kg.optima.mobile.R;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.IApiMethods;

import static kz.optimabank.optima24.utility.Constants.API_BASE_URL;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_TAG;

public class AgreementViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<BaseResponse<String>> agreementDocument;
    private MutableLiveData<BaseResponse<String>> checkUserResponse;
    private MutableLiveData<BaseResponse<String>> confirmCheckUserResponse;
    public MutableLiveData<String> errorMessage;

    public AgreementViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<BaseResponse<String>> getAgreementDocument() {
        if (agreementDocument == null) {
            agreementDocument = new MutableLiveData<>();
            requestAgreementDocument();
        }
        return agreementDocument;
    }

    // запрашиваем ссылку на получения документа пользовательского соглашения
    private void requestAgreementDocument() {
        compositeDisposable.add(getApi().getAgreement(OptimaBank.getInstance().getOpenSessionHeader(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response) {
                        if (response != null) agreementDocument.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorMessage.postValue(getApplication().getString(R.string.internet_connection_error));
                        Log.e(REGISTRATION_TAG, "requestAgreementDocument error " + e.getLocalizedMessage());
                    }
                }));
    }

    public LiveData<BaseResponse<String>> checkUser(String login, String password) {
        if (checkUserResponse == null) {
            checkUserResponse = new MutableLiveData<>();
            requestCheckUser(login, password);
        }
        return checkUserResponse;
    }

    // запрашиваем проверку пользователя перед началом регистрации
    private void requestCheckUser(String login, String password) {
        compositeDisposable.add(getApi().checkUser(login, password, null, OptimaBank.getInstance().getOpenSessionHeader(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response) {
                        if (response != null) checkUserResponse.postValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorMessage.postValue(getApplication().getString(R.string.internet_connection_error));
                        Log.e(REGISTRATION_TAG, "requestCheckUser error " + e.getLocalizedMessage());
                    }
                }));
    }

    public LiveData<BaseResponse<String>> confirmCheckUser(String login, String password, String confirmationCode) {
        confirmCheckUserResponse = new MutableLiveData<>();
        requestConfirmCheckUser(login, password, confirmationCode);
        return confirmCheckUserResponse;
    }

    // запрашиваем потверждение на проверку пользователя перед началом регистрации
    private void requestConfirmCheckUser(String login, String password, String confirmationCode) {
        Disposable disposable = getApi().checkUser(login, password, confirmationCode, OptimaBank.getInstance().getOpenSessionHeader(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response) {
                        if (response != null) {
                            confirmCheckUserResponse.postValue(response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorMessage.postValue(getApplication().getString(R.string.internet_connection_error));
                        Log.e(REGISTRATION_TAG, "requestConfirmCheckUser error " + e.getLocalizedMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    // получаем ссылку на API запрос
    private IApiMethods getApi() {
        ServiceGenerator request = new ServiceGenerator();
        return request.request(getApplication(), null, API_BASE_URL, false, false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}