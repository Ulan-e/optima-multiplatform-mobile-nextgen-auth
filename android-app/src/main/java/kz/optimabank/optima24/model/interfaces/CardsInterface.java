package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import java.util.List;

import kz.optimabank.optima24.model.gson.response.Data;
import kz.optimabank.optima24.model.service.BannerImpl;
import kz.optimabank.optima24.model.service.CardDataImpl;

public interface CardsInterface {
    void getInterfaceViewData(Context context);
    void registerCallBack(CardDataImpl.Callback callback);
}
