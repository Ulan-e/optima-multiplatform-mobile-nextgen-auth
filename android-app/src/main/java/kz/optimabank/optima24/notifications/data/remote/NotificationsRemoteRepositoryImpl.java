package kz.optimabank.optima24.notifications.data.remote;

import android.content.Context;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.notifications.models.DeliveredNotificationResponse;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.notifications.models.NotificationIdBody;
import kz.optimabank.optima24.notifications.models.NotificationResponse;
import retrofit2.Response;

import static kz.optimabank.optima24.utility.Constants.API_PUSH_URL;

public class NotificationsRemoteRepositoryImpl implements NotificationsRemoteRepository {

    private Context context;

    public NotificationsRemoteRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Single<List<Notification>> getUnreadNotifications(String clientId) {
        return getApi().getUnreadNotifications(clientId, getHeader());
    }

    @Override
    public Single<Response<DeliveredNotificationResponse>> requestNotificationDelivered(NotificationIdBody body) {
        return getApi().updateMessageToDelivered(body, getHeader());
    }

    @Override
    public Single<Response<NotificationResponse>> setReadNotification(NotificationIdBody body) {
        return getApi().setReadNotification(body, getHeader());
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