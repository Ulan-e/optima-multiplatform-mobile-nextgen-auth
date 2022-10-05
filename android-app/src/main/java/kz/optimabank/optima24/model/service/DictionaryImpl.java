package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.db.controllers.DictionaryController;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.DictionaryResponse;
import kz.optimabank.optima24.model.interfaces.DictionaryContext;

/**
  Created by Timur on 13.05.2017.
 */

public class DictionaryImpl extends GeneralService implements DictionaryContext {
    private Callback callback;

    @Override
    public void getAllDictionary(Context context) {
        NetworkResponse.getInstance().getAllDictionary(context, OptimaBank.getInstance().getOpenSessionHeader(null),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<DictionaryResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<DictionaryResponse> response, String errorMessage, int code) {
                        if(code==0) {
                            DictionaryController dictionaryController = DictionaryController.getController();
                            ArrayList<kz.optimabank.optima24.db.entry.Dictionary> dictionary = new ArrayList<>();
                            ArrayList<Country> countries = new ArrayList<>();
                            ArrayList<ForeignBank> foreignBanks = new ArrayList<>();
                            foreignBanks.addAll(response.data.foreignBanks);
                            countries.addAll(response.data.countries);
                            dictionary.addAll(response.data.bic);
                            dictionary.addAll(response.data.knp);
                            dictionary.addAll(response.data.vo_codes);
                            dictionary.addAll(response.data.countriesForRegMastercard);
                            dictionaryController.updateDictionary(dictionary);
                            dictionaryController.updateForeignBanks(foreignBanks);
                            Log.i("getDictionaryBic","response.bic.size = "+response.data.foreignBanks.size());
                            Log.i("getDictionaryBic","response.bic = "+response.data.bic);
                            try {
                                dictionaryController.updateCountries(countries);
                                Log.i("customList","countries = "+countries.size());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        if (callback!=null)
                        callback.jsonDictionaryResponse(code,errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonDictionaryResponse(int statusCode, String errorMessage);
    }
}
