package kz.optimabank.optima24.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import kg.optima.mobile.BuildConfig;
import kg.optima.mobile.R;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.TLSSocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit retrofit;
    private static ServiceGenerator request;

    public static ServiceGenerator getInstance() {
        if (request == null) {
            request = new ServiceGenerator();
        }
        return request;
    }

    public static OkHttpClient okHttpClient(Context context, boolean isPush) {
        if (isPush) {
            return createPushOkHttp();
        } else {
            return createOptima24OkHttp(context);
        }
    }

    // http клиент для API Optima24 с сертификатом
    private static OkHttpClient createOptima24OkHttp(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(interceptor);
            initUnsafeSSL(httpClient);
        }else {
            setOptima24Certificate(context, httpClient);
        }
        httpClient.readTimeout(80, TimeUnit.SECONDS);
        httpClient.connectTimeout(80, TimeUnit.SECONDS);
        httpClient.writeTimeout(80, TimeUnit.SECONDS);
        return httpClient.build();
    }

    public static void setOptima24Certificate(Context context, OkHttpClient.Builder okHttpClient) {
        try {
            InputStream in;
            KeyStore trusted = KeyStore.getInstance(context.getString(R.string.BKS));
            in = context.getResources().openRawResource(R.raw.keyatf24); //test TLS  -b2018        atf24_key  - old          keyatf24   -  new
            trusted.load(in, "testing".toCharArray());
            in.close();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trusted);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                sslSocketFactory = new TLSSocketFactory(sslContext.getSocketFactory());
            } else {
                sslSocketFactory = sslContext.getSocketFactory();
            }
            okHttpClient.sslSocketFactory(sslSocketFactory, trustManager);
            okHttpClient.hostnameVerifier((hostname, session) -> true);
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException | IOException | CertificateException e) {
            e.printStackTrace();
        }
    }

    // http клиент для API сервиса пушей с сертификатом
    private static OkHttpClient createPushOkHttp() {
        OkHttpClient.Builder okhttp = new OkHttpClient.Builder();
        okhttp.readTimeout(80, TimeUnit.SECONDS);
        okhttp.connectTimeout(80, TimeUnit.SECONDS);
        okhttp.writeTimeout(80, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            initHttpLogging(okhttp);
        }
        return okhttp.build();
    }

    private static void initHttpLogging(OkHttpClient.Builder httpClientBuilder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(logging);
    }

    private static void initUnsafeSSL(OkHttpClient.Builder okhttpBuilder) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            okhttpBuilder.sslSocketFactory(sslContext.getSocketFactory(), systemDefaultTrustManager())
                    .hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SSLContext createCertificate(InputStream trustedCertificateIS) throws CertificateException, IOException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try {
            ca = cf.generateCertificate(trustedCertificateIS);
        } finally {
            trustedCertificateIS.close();
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    private static X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError();
        }
    }

    public IApiMethods request(Context context, ProgressDialog progressDialog, String url, boolean isPush, boolean needRefreshAlarmManager) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            try {
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (needRefreshAlarmManager)
            GeneralManager.getInstance().refreshAlarmManager(context);

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient(context, isPush))
                .build();

        return retrofit.create(IApiMethods.class);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }
}