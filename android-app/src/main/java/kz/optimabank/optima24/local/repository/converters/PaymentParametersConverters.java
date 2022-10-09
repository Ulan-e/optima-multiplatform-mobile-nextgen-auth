package kz.optimabank.optima24.local.repository.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import kz.optimabank.optima24.model.base.PaymentServiceParameter;

public class PaymentParametersConverters {

    @TypeConverter
    public String fromServiceParameters(List<PaymentServiceParameter> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PaymentServiceParameter>>() {
        }.getType();
        return gson.toJson(countryLang, type);
    }

    @TypeConverter
    public List<PaymentServiceParameter> toServiceParameters(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<PaymentServiceParameter>>() {
        }.getType();
        return gson.fromJson(countryLangString, type);
    }
}
