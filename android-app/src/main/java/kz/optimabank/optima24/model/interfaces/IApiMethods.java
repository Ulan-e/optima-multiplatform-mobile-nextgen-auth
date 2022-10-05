package kz.optimabank.optima24.model.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.fragment.account.model.UrgentMessage;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.AccountPhoneNumberResponse;
import kz.optimabank.optima24.model.base.ApplicationTypeDto;
import kz.optimabank.optima24.model.base.Banner;
import kz.optimabank.optima24.model.base.BaseRegistrationResponse;
import kz.optimabank.optima24.model.base.CancelApplicationModel;
import kz.optimabank.optima24.model.base.Category;
import kz.optimabank.optima24.model.base.HistoryApplications;
import kz.optimabank.optima24.model.base.HistoryDetailsApplications;
import kz.optimabank.optima24.model.base.HistoryItem.PaymentHistoryItem;
import kz.optimabank.optima24.model.base.HistoryItem.TransferHistoryItem;
import kz.optimabank.optima24.model.base.InvoiceContainerItem;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.base.Limit.Internet;
import kz.optimabank.optima24.model.base.NewsItem;
import kz.optimabank.optima24.model.base.PushResponse;
import kz.optimabank.optima24.model.base.PushResponse.DeviceConfirmResponse;
import kz.optimabank.optima24.model.base.PushResponse.DeviceRegisterResponse;
import kz.optimabank.optima24.model.base.PushResponse.MessageResponse;
import kz.optimabank.optima24.model.base.PushResponse.MessageUnreadResponse;
import kz.optimabank.optima24.model.base.PushResponse.PushSettings;
import kz.optimabank.optima24.model.base.Rate;
import kz.optimabank.optima24.model.base.SecretQuestionResponse;
import kz.optimabank.optima24.model.base.StatementsWithStats;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.base.Terminal;
import kz.optimabank.optima24.model.gson.response.AccStatusResponse;
import kz.optimabank.optima24.model.gson.response.AccountsResponse;
import kz.optimabank.optima24.model.gson.response.AuthorizationResponse;
import kz.optimabank.optima24.model.gson.response.BankReference;
import kz.optimabank.optima24.model.gson.response.BankRequisitesResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.Bool;
import kz.optimabank.optima24.model.gson.response.CheckPaymentsResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.Data;
import kz.optimabank.optima24.model.gson.response.DictionaryResponse;
import kz.optimabank.optima24.model.gson.response.LoanScheduleResponse;
import kz.optimabank.optima24.model.gson.response.MobileOperatorResponse;
import kz.optimabank.optima24.model.gson.response.PaymentContextResponse;
import kz.optimabank.optima24.model.gson.response.PaymentTemplateResponse;
import kz.optimabank.optima24.model.gson.response.QuestionsResponse;
import kz.optimabank.optima24.model.gson.response.TaxDictResponse;
import kz.optimabank.optima24.model.gson.response.TransferConfirmResponse;
import kz.optimabank.optima24.notifications.models.DeliveredNotificationResponse;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.NotificationIdBody;
import kz.optimabank.optima24.notifications.models.RegistrationTokenBody;
import kz.optimabank.optima24.notifications.models.RegistrationTokenResponse;
import kz.optimabank.optima24.notifications.models.NotificationResponse;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IApiMethods {
    @GET("api/security/EncryptRegistrationKey/{RegistrationKey}")
    Call<ResponseBody> calculateSignature(@HeaderMap Map<String, String> map, @Path("RegistrationKey") String str);

    @GET("api/applications/cancel/{id}")
    Call<CancelApplicationModel> cancelApplicationFirst(@Path("id") int i, @HeaderMap Map<String, String> map);

    @POST("api/applications/cancel")
    Call<ResponseBody> cancelApplicationSecond(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @PUT("api/payments/templates/{Id}/state")
    Call<BaseResponse<String>> changeActivePaymentTemplate(@HeaderMap Map<String, String> map, @Path("Id") int i, @Query("IsActive") boolean z);

    @PUT("api/transfers/templates/{Id}/state")
    Call<BaseResponse<String>> changeActiveTransfersTemplate(@HeaderMap Map<String, String> map, @Path("Id") int i, @Query("IsActive") boolean z);

    @PATCH("api/users")
    Call<BaseResponse<String>> changePassword(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @POST("api/users/image")
    Call<BaseResponse<String>> setProfImage(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @GET("api/users/image")
    Call<ResponseBody> getProfImage(@HeaderMap Map<String, String> map);

    @PUT("api/payments/templates/{Id}")
    Call<BaseResponse<String>> changePaymentTemplate(@HeaderMap Map<String, String> map, @Body RequestBody requestBody, @Path("Id") int i, @Query("ProcessAfterSaving") boolean z);

    @POST("api/registration/changePassword")
    Call<BaseRegistrationResponse> changeTempPassword(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @PUT("api/transfers/templates/{Id}")
    Call<BaseResponse<String>> changeTransfersTemplate(@HeaderMap Map<String, String> map, @Body RequestBody requestBody, @Path("Id") int i);

    @GET("api/transfers/accdata")
    Call<BaseResponse<AccStatusResponse>> getAccData(@HeaderMap Map<String, String> map, @Query("number") String str);

    @POST("api/registration/check")
    Call<BaseRegistrationResponse> checkClient(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @GET("api/accinfo/{Code}/internet")
    Call<BaseResponse<Internet>> checkInternet(@HeaderMap Map<String, String> map, @Path("Code") int i);

    @POST("api/transfers/mt100/fee")
    Call<BaseResponse<CheckResponse>> checkMt100Transfer(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @POST("api/payments/check")
    Call<BaseResponse<CheckPaymentsResponse>> checkPayments(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @GET("api/registration/checkNumber/{phoneNumber}")
    Call<ResponseBody> checkPhoneNumber(@Path("phoneNumber") String str, @HeaderMap Map<String, String> map);

    @GET("api/accinfo/{Code}/inform")
    Call<BaseResponse<Bool>> checkSmsInform(@HeaderMap Map<String, String> map, @Path("Code") int i);

    @POST("api/transfers/swift/fee")
    Call<BaseResponse<CheckResponse>> checkSwift(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @GET("api/versions")
    Call<BaseResponse<String>> checkVersion(@HeaderMap Map<String, String> map);

    @POST("api/transfers/mt100")
    Call<BaseResponse<TransferConfirmResponse>> confirmMt100Transfer(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @POST("api/payments/instant")
    Call<BaseResponse<BankReference>> confirmPayments(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @GET("api/tax/dict")
    Call<BaseResponse<TaxDictResponse>> getTaxDict(@HeaderMap Map<String, String> map);

    @POST("api/transfers/swift")
    Call<BaseResponse<TransferConfirmResponse>> confirmSwift(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @POST("api/applications")
    Call<ResponseBody> createApplication(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @POST("api/payments/templates")
    Call<BaseResponse<String>> createPaymentTemplate(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @PUT("api/transfers/templates")
    Call<BaseResponse<String>> createTransfersTemplate(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @DELETE("api/payments/templates/{Id}")
    Call<BaseResponse<String>> deletePaymentTemplate(@HeaderMap Map<String, String> map, @Path("Id") int i);

    @DELETE("api/transfers/templates")
    Call<BaseResponse<String>> deleteTransfersTemplate(@HeaderMap Map<String, String> map, @Query("Id") int i);

    /**
     * Запрос на получение реквизитов по счету
     * @param map данные пользователя и об устройстве
     * @param branchCode
     * @param currency валюта
     * @param accountNumber номер счета
     * @return возвращает реквизиты
     */
    @GET("api/dictionaries/requisites/{branchcode}/{currency}/{pAccountNumber}")
    Call<BankRequisitesResponse> getBanksRequisites(
            @HeaderMap Map<String, String> map,
            @Path("branchcode") String branchCode,
            @Path("currency") String currency,
            @Path("pAccountNumber") String accountNumber
    );

    @GET("api/dictionaries/foreign/{param}")
    Call<BaseResponse<ArrayList<ForeignBank>>> filterForeignBanks(@HeaderMap Map<String, String> map, @Path("param") String str);

    @PUT("api/registration/client")
    Call<BaseRegistrationResponse> finishRegForClientOfBank(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @PUT("api/registration/nonClient")
    Call<BaseRegistrationResponse> finishRegForNoClientOfBank(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @GET("api/accstatements/{Code}/ext")
    Call<BaseResponse<StatementsWithStats>> getAccountOperationsAndStats(@Path("Code") int i, @Query("fromDate") String str, @Query("toDate") String str2, @HeaderMap Map<String, String> map);

    @GET("api/accounts")
    Call<BaseResponse<AccountsResponse>> getAccounts(@HeaderMap Map<String, String> map, @Query("needUpdate") boolean z);

    @GET("api/accstatements/operations")
    Call<BaseResponse<ArrayList<ATFStatement>>> getAccountsOperations(@Query("fromDate") String str, @Query("toDate") String str2, @HeaderMap Map<String, String> map);

    @GET("api/dictionaries")
    Call<BaseResponse<DictionaryResponse>> getAllDictionary(@HeaderMap Map<String, String> map);

    @GET("unread")
    @Headers({"Content-Type:application/json"})
    Call<MessageUnreadResponse> getAllPushMessage(@HeaderMap Map<String, String> map);

    @GET("api/applications/{id}")
    Call<ApplicationTypeDto> getApplicationById(@HeaderMap Map<String, String> map, @Path("id") int i);

    @GET("api/applications/details/{id}")
    Call<ApplicationTypeDto> getApplicationDetailsById(@HeaderMap Map<String, String> map, @Path("id") int i);

    @GET("api/banners")
    Call<BaseResponse<ArrayList<Banner>>> getBanner(@HeaderMap Map<String, String> map);

    @GET
    Call<ResponseBody> getBannerImage(@HeaderMap Map<String, String> map, @Url String url);

    @GET("api/servicePoints")
    Call<BaseResponse<ArrayList<Terminal>>> getAllServicePoints(@HeaderMap Map<String, String> map);

    @GET("api/accounts/categories")
    Call<BaseResponse<ArrayList<Category>>> getCategories(@HeaderMap Map<String, String> map);

    @GET("api/applications/history")
    Call<ArrayList<HistoryApplications>> getHistoryApplications(@HeaderMap Map<String, String> map);

    @GET("api/applications/history")
    Call<ArrayList<HistoryApplications>> getHistoryApplicationsByDate(@Query("dateFrom") String str, @Query("dateTo") String str2, @HeaderMap Map<String, String> map);

    @GET("api/applications/applicationdetails/{id}")
    Call<HistoryDetailsApplications> getHistoryDetailsById(@HeaderMap Map<String, String> map, @Path("id") int i);

    @GET("api/payments/invoice/{invoiceId}")
    Call<InvoiceContainerItem> getInvoice(@HeaderMap Map<String, String> map, @Path("invoiceId") long j);

    @GET("api/cards/{Code}/limits")
    Call<BaseResponse<ArrayList<Limit>>> getLimit(@HeaderMap Map<String, String> map, @Query("Code") int i);

    @GET("api/accstatements/{Code}/schedule")
    Call<BaseResponse<ArrayList<LoanScheduleResponse>>> getLoanSchedule(@HeaderMap Map<String, String> map, @Path("Code") int i);

    @GET("api/payments/services")
    Call<MobileOperatorResponse> getMobileOperator(@HeaderMap Map<String, String> map, @Query("mobileNumber") String str);

    @GET("api/publications")
    Call<BaseResponse<ArrayList<NewsItem>>> getNews(@HeaderMap Map<String, String> map);

    @GET("api/publications/image")
    Call<BaseResponse<String>> getNewsImage(@HeaderMap Map<String, String> map, @Query("name") String name, @Query("category") String category);

    @GET("api/payments/context")
    Call<BaseResponse<PaymentContextResponse>> getPaymentContext(@HeaderMap Map<String, String> map);

    @GET("api/payments/history")
    Call<BaseResponse<ArrayList<PaymentHistoryItem>>> getPaymentHistory(@Query("from") String str, @Query("to") String str2, @HeaderMap Map<String, String> map);

    @GET("api/payments/templates")
    Call<BaseResponse<PaymentTemplateResponse>> getPaymentSubscriptions(@HeaderMap Map<String, String> map);

    @GET("messages/{messageId}")
    @Headers({"Content-Type:application/json"})
    Call<MessageResponse> getPushMessage(@HeaderMap Map<String, String> map, @Path("messageId") String str);

    @GET("api/settings/push")
    Call<BaseResponse<ArrayList<PushSettings>>> getPushSettings(@HeaderMap Map<String, String> map);

    @GET("api/rates")
    Call<BaseResponse<ArrayList<Rate>>> getRates(@HeaderMap Map<String, String> map);

    @GET("api/registration/Questions")
    Call<List<SecretQuestionResponse>> getSecretQuestions(@HeaderMap Map<String, String> map);

    @GET("api/transfers/history")
    Call<BaseResponse<ArrayList<TransferHistoryItem>>> getTransferHistory(@Query("from") String str, @Query("to") String str2, @HeaderMap Map<String, String> map);

    @GET("api/transfers/templates")
    Call<BaseResponse<ArrayList<TemplateTransfer>>> getTransfersTemplate(@HeaderMap Map<String, String> map);

    @GET("api/applications/types")
    Call<ArrayList<ApplicationTypeDto>> getTypesApplications(@HeaderMap Map<String, String> map);

    @POST("api/payments/invoice")
    Call<ResponseBody> invoicePayment(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @POST("api/auth/session")
    Call<BaseResponse<AuthorizationResponse>> openSession(@Body Map<String, String> map, @HeaderMap Map<String, String> map2);

    @POST("api/auth/session/keepalive")
    Call<BaseResponse<String>> keepAliveRequest(@HeaderMap Map<String, String> map);

    @POST("api/payments/template/{Id}")
    Call<BaseResponse<String>> quickPayment(@HeaderMap Map<String, String> map, @Path("Id") int i);

    @POST("api/transfers/card/client")
    Call<BaseResponse<ResponseBody>> registerMastercard(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @GET("api/registration/resendSms")
    Call<BaseRegistrationResponse> requestSmsAgain(@HeaderMap Map<String, String> map, @Query("phoneNumber") String str);

    @POST("api/registration/client")
    Call<BaseRegistrationResponse> requestSmsForClientOfBank(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @POST("api/registration/nonClient")
    Call<BaseRegistrationResponse> requestSmsForNoClientOfBank(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @POST("api/security/otp/{key}")
    Call<BaseResponse<String>> sendSMS(@Path("key") int i, @HeaderMap Map<String, String> map);

    @PUT("api/accinfo/{Code}/internet")
    Call<BaseResponse<String>> setInternet(@HeaderMap Map<String, String> map, @Path("Code") int i, @Body RequestBody requestBody);

    @PUT("api/cards/{Code}/limits")
    Call<BaseResponse<String>> setLimit(@HeaderMap Map<String, String> map, @Path("Code") int i, @Body RequestBody requestBody);

    @PATCH("api/accounts/{Code}")
    Call<BaseResponse<String>> setLockCard(@Path("Code") int i, @HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @PUT("api/settings/push")
    Call<ResponseBody> setPushSettings(@HeaderMap Map<String, String> map, @Body RequestBody requestBody);

    @PUT("api/accinfo/{Code}/inform")
    Call<BaseResponse<String>> setSmsInform(@HeaderMap Map<String, String> map, @Path("Code") int i, @Body RequestBody requestBody);

    @PATCH("api/accounts")
    Call<BaseResponse<String>> setVisibilityAccounts(@Body RequestBody requestBody, @HeaderMap Map<String, String> map);

    @GET("api/interfaceBuilder/formItems")
    Call<BaseResponse<List<Data>>> getInterfaceViewData(@HeaderMap Map<String, String> map);

    @POST("/api/security/OtpWithText/{key}/{text}/{operationCode}")
    Call<BaseResponse<String>> sendAmountAndCurrency(@Path("key") int otp, @Path("text") String amount, @Path("operationCode") String operationCode, @HeaderMap Map<String, String> map);

    @GET("/api/security/GetAttemptCount")
    Call<BaseResponse<String>> getAttemptCount(@HeaderMap Map<String, String> map);

    @POST("/api/security/RemoveOtpKey/{key}")
    Call<BaseResponse<String>> removeOtpKey(@Path("key") int otp, @HeaderMap Map<String, String> map);

    @POST("/api/security/IsOtpKeyValid/{key}/{securityCode}")
    Call<BaseResponse<String>> isOtpKeyValid(@Path("key") int otp, @Path("securityCode") String securityCode, @HeaderMap Map<String, String> map);

    /**
     * Проверка доступности сервера при нажатии на вход
     * @param header передаем пустой header
     * @return получаем результат об успешности
     */
    @POST("/api/auth/session/checkServer")
    Single<Response<BaseResponse<String>>> checkServer(@HeaderMap Map<String, String> header);

    /**
     * Получения срочного сообщения при входе для пользователя
     * @param header передаем пустой header
     * @return получаем сообщение
     */
    @GET("api/notifications/urgent")
    Single<BaseResponse<UrgentMessage>> getUrgentMessage(@HeaderMap Map<String, String> header);

    /**
     * Запрос для получения секретных вопросов, которые помогут при восстановление аккаунта
     * @param map в Headers передаем язык и данные об устройстве и текущую версию приложения
     * @return получаем список вопросов
     */
    @GET("/api/registration/questions")
    Single<QuestionsResponse> getQuestions(
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос для получения документ соглашения
     * @param map в Headers передаем язык и данные об устройстве и текущую версию приложения
     * @return получаем url файла
     */
    @GET("/api/registration/agreement")
    Single<BaseResponse<String>> getAgreement(
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос для получения типа авторизации при первичном входе в приложение
     * @param bankId логин пользователя банка
     * @param map в Headers передаем язык и данные об устройстве и текущую версию приложения
     * @return если возвращаемый код 0 то успешно получили ответ, если нет то получаем сообщение
     * об ошибке в поле data получаем тип регистрации
     */
    @GET("/api/auth/GetAuthorizationType/{BankId}")
    Call<BaseResponse<String>> getAuthorizationType(
            @Path("BankId") String bankId,
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос на регистрацию токена
     * @param body в Body передаем токен полученный от Firebase, логин пользователся, версию Android
     *              и текущую версию приложения
     * @param map в HeaderManager передаем текущий язык и информацию о приложении
     * @return если успешно success true то получаем данные о токене
     */
    @POST("ns/token-register")
    Single<Response<RegistrationTokenResponse>> registerToken(
            @Body RegistrationTokenBody body,
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос на отчет о доставке уведомления
     * @param body request body c айди уведомления
     * @param map      в HeaderManager передаем текущий язык и информацию о приложении
     * @return получаем список всех непрочитаныных уведомлений
     */
    @POST("ns/message-delivered")
    Single<Response<DeliveredNotificationResponse>> updateMessageToDelivered(
            @Body NotificationIdBody body,
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос на получение непрочитанных уведомлений
     * @param clientId логин пользователя
     * @param map      в HeaderManager передаем текущий язык и информацию о приложении
     * @return получаем список всех непрочитаныных уведомлений
     */
    @GET("ns/unread-messages")
    Single<List<Notification>> getUnreadNotifications(
            @Query("clientId") String clientId,
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос для проверки регистрационного кода
     *
     * @param bankId логин пользователя
     * @param registrationCode регистрационный код состоит только из цифр, и является уникальным
     *                         для каждого пользователя
     * @param confirmationCode при первом запросе поле пустое, при следуещем запросе передаем смс код
     *                         полученный на устройство
     * @param map в Headers пердаем язык и данные об устройстве и текущую версию приложения
     * @return если возвращаемый код 0 то успешно получаем ответ, если нет то получаем сообщение об ошибке
     */
    @POST("/api/registration/checkUser/{BankId}/{RegistrationCode}")
    Single<BaseResponse<String>> checkUser(
            @Path("BankId") String bankId,
            @Path("RegistrationCode") String registrationCode,
            @Query("ConfirmationCode") String confirmationCode,
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос на сохрание данных нового пользователя в системе
     * @param requestBody в Body передаем данные нового пользователя логин, пароль, идентификатор вопроса и ответ на вопрос
     * @param map в Headers пердаем язык и данные об устройстве и версию приложения
     * @return если возвращаемый код 0 то успешно получили ответ, если нет то получаем сообщение об ошибке
     */
    @POST("/api/registration/saveUser")
    Single<BaseResponse<String>> saveUser(
            @Body RequestBody requestBody,
            @HeaderMap Map<String, String> map
    );

    /**
     * Запрос для изменения статуса уведомления, то есть метод делаем его прочитанным
     * @param body в Body добавляем идентификатор уведомления
     * @param map  в HeaderManager передаем текущий язык и информацию о приложении
     * @return если успешно success true то получаем сообщение
     */
    @POST("ns/read-message")
    Single<Response<NotificationResponse>> setReadNotification(
            @Body NotificationIdBody body,
            @HeaderMap Map<String, String> map
    );

    @POST("api/accounts/SetDefaultStatusForCard/{accId}")
    Call<BaseResponse<String>> setDefaultStatusForCard(
            @HeaderMap Map<String, String> map,
            @Path("accId") long accountId
    );

    @GET("api/transfers/GetAccountDataByPhoneNumber/{phoneNumber}")
    Call<BaseResponse<AccountPhoneNumberResponse>> getAccountDataByPhoneNumber(
            @HeaderMap Map<String, String> map,
            @Path("phoneNumber") String phoneNumber
    );
}