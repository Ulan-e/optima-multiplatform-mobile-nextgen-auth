package kz.optimabank.optima24.secondary_registration.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kg.optima.mobile.R;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.model.base.SecretQuestionResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.QuestionsResponse;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import kz.optimabank.optima24.utility.crypt.CryptoUtils;
import okhttp3.RequestBody;

import static kz.optimabank.optima24.utility.Constants.API_BASE_URL;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_TAG;

public class RegistrationViewModel extends AndroidViewModel {

    private static final String OK_HTTP_PARAM = "application/json; charset=utf-8";
    private static final String BANK_ID = "BankId";
    private static final String HASH_PASSWORD = "HashPassword";
    private static final String ANSWER = "Answer";
    private static final String QUESTION_ID = "QuestionId";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<List<SecretQuestionResponse>> questions;
    private MutableLiveData<BaseResponse<String>> saveUserResult;
    public MutableLiveData<String> errorMessage;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<SecretQuestionResponse>> getQuestions() {
        if (questions == null) {
            questions = new MutableLiveData<>();
            fetchQuestions();
        }
        return questions;
    }

    // запрос на получения вопросов при для регистрации
    public void fetchQuestions() {
        compositeDisposable.add(getApi().getQuestions(OptimaBank.getInstance().getOpenSessionHeader(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<QuestionsResponse>() {
                    @Override
                    public void onSuccess(QuestionsResponse response) {
                        if (response != null) questions.postValue(response.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(REGISTRATION_TAG, "fetchQuestions error " + e.getLocalizedMessage());
                    }
                }));
    }

    public LiveData<BaseResponse<String>> saveUser(String login, String password, String answer, String questionId) {
        if (saveUserResult == null) {
            saveUserResult = new MutableLiveData<>();
            requestSaveUser(login, password, answer, questionId);
        }
        return saveUserResult;
    }

    // запрос для сохранения пользователя в системе как зарегистрированным
    private void requestSaveUser(String login, String password, String answer, String questionId) {
        compositeDisposable.add(getApi().saveUser(
                getRequestBody(login, password, answer, questionId),
                OptimaBank.getInstance().getOpenSessionHeader(null)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response) {
                        if (response != null) saveUserResult.postValue(response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        errorMessage.postValue(getApplication().getString(R.string.internet_connection_error));
                        Log.e(REGISTRATION_TAG, "requestSaveUser error " + t.getLocalizedMessage());
                    }
                }));
    }

    // получаем ссылку на API запрос
    private IApiMethods getApi() {
        ServiceGenerator request = new ServiceGenerator();
        return request.request(getApplication(), null, API_BASE_URL, false, false);
    }

    // конвертируем в RequestBody
    private RequestBody getRequestBody(String login, String password, String answer, String questionId) {
        JSONObject jsonObject = getBody(login, password, answer, questionId);
        return RequestBody.create(
                okhttp3.MediaType.parse(OK_HTTP_PARAM),
                (jsonObject).toString()
        );
    }

    private JSONObject getBody(String login, String password, String answer, String questionId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(BANK_ID, login);
            jsonObject.put(HASH_PASSWORD, CryptoUtils.getHash(password));
            jsonObject.put(ANSWER, answer);
            jsonObject.put(QUESTION_ID, questionId);
        } catch (JSONException e) {
            Log.e(REGISTRATION_TAG, "Error getting JSON " + e.getLocalizedMessage());
        }
        return jsonObject;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}