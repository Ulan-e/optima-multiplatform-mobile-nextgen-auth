package kz.optimabank.optima24.notifications.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationTokenBody {

    @SerializedName("pushToken")
    @Expose
    private String pushToken;
    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("os")
    @Expose
    private DeviceOS deviceOS;
    @SerializedName("app")
    @Expose
    private App app;

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
    public String getPushToken() {
        return pushToken;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getClientId() {
        return  clientId;
    }

    public void setDeviceOS(DeviceOS os) {
        this.deviceOS = os;
    }
    public DeviceOS getOS() {
        return deviceOS;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public static class App {

        @SerializedName("ver")
        @Expose
        private String ver;
        @SerializedName("name")
        @Expose
        private String name;

        public String getVersion() {
            return ver;
        }

        public void setVersion(String ver) {
            this.ver = ver;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "App{" +
                    "ver='" + ver + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class DeviceOS {

        @SerializedName("ver")
        @Expose
        private String ver;
        @SerializedName("platform")
        @Expose
        private String platform;

        public void setVersion(String ver) {
            this.ver = ver;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        @Override
        public String toString() {
            return "DeviceOS{" +
                    "ver='" + ver + '\'' +
                    ", platform='" + platform + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RegistrationTokenBody{" +
                "pushToken='" + pushToken + '\'' +
                ", clientId='" + clientId + '\'' +
                ", deviceOS=" + deviceOS +
                ", app=" + app +
                '}';
    }
}