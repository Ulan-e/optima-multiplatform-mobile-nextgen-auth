package kz.optimabank.optima24.model.interfaces;

import android.content.Context;
import android.widget.ImageView;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.ProfImageLoaderImpl;

public interface ProfImageLoader {
    void setProfImage(Context context, JSONObject body);
    void getProfImage(Context context);
    void registerCallBack(ProfImageLoaderImpl.Callback callback);
    void registerSetProfImageCallBack(ProfImageLoaderImpl.SetProfImageCallback callback);

}
