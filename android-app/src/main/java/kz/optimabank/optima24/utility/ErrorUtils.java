package kz.optimabank.optima24.utility;

import android.util.Log;

import java.lang.annotation.Annotation;

import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.APIError;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
  Created by Timur on 23.02.2017.
 */

public class ErrorUtils {
    public static APIError parseError(ResponseBody response) {
        Retrofit retrofit = ServiceGenerator.retrofit();
        if (NetworkResponse.getInstance().error == null) {
            if (retrofit != null) {
                Converter<ResponseBody, APIError> converter =
                        ServiceGenerator.retrofit().responseBodyConverter(APIError.class, new Annotation[0]);

                APIError error;

                try {
                    error = converter.convert(response);
                } catch (Exception e) {
                    Log.e("ErrorUtils", "error when convert " + e);
                    return new APIError();
                }

                return error;
            }
            return null;
        } else {
            APIError error = NetworkResponse.getInstance().error;
            NetworkResponse.getInstance().error = null;
            return error;
        }
    }
}
