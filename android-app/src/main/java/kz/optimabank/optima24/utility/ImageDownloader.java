package kz.optimabank.optima24.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kz.optimabank.optima24.utility.Constants.API_BASE_URL;
import static kz.optimabank.optima24.utility.Utilities.isInternetConnectionError;

public final class ImageDownloader {
    public static String directory;

    public static String saveToInternalStorage(byte[] image, Context context, String imageName) {
        final String DIRECTORY_NAME = "Optima24Banners";
        ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
        File directory = contextWrapper.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE);
        File myPath = new File(directory, imageName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myPath);
            fileOutputStream.write(image);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(fileOutputStream).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static void loadImageFromStorage(String directory, ImageView imageView, String imageName) {
        try {
            File file = new File(directory, imageName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void getBannerImage(final Context context, final Map<String, String> header, final String bannerUrl, String imageName) {
        ServiceGenerator request = new ServiceGenerator();
        if (!isInternetConnectionError() && context != null) {
            IApiMethods iApiMethods = request.request(context, null, API_BASE_URL, false, false);
            Call<ResponseBody> call = iApiMethods.getBannerImage(header, bannerUrl);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            try {
                                directory = saveToInternalStorage(response.body().bytes(), context, imageName);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("ololo", "onResponse: " + e.getMessage());
                            }
                            BannersDirectoryNamePreferences.getInstance(context).saveDirectory(directory);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
