package kz.optimabank.optima24.fragment.account.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.fragment.account.model.UrgentMessage;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.API_BASE_URL;
import static kz.optimabank.optima24.utility.Constants.URGENT_MESSAGE_TAG;

public class AccountsListViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;
    private UrgentMessageListener urgentMessageListener;

    public AccountsListViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
    }

    // запрос на получение срочного сообщения
    public void requestNoticeMessage() {
        String sessionId = GeneralManager.getInstance().getSessionId();
        compositeDisposable.add(getApi().getUrgentMessage(HeaderHelper.getOpenSessionHeader(getApplication(), sessionId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse<UrgentMessage>>() {
                    @Override
                    public void onSuccess(BaseResponse<UrgentMessage> response) {
                        if (response != null) {
                            urgentMessageListener.setMessage(response.data);
                            Log.d(URGENT_MESSAGE_TAG, " success get urgent message" + response.data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(URGENT_MESSAGE_TAG, "> error getting urgent message " + e.getLocalizedMessage());
                    }
                }));
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

    public interface UrgentMessageListener {
        void setMessage(UrgentMessage message);
    }

    public void registerListener(UrgentMessageListener listener) {
        this.urgentMessageListener = listener;
    }
}