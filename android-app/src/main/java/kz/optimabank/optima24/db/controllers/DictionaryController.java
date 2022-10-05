package kz.optimabank.optima24.db.controllers;

import java.util.ArrayList;

import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.ForeignBank;

/**
  Created by Timur on 13.05.2017.
 */

public class DictionaryController extends DBController {
    private static DictionaryController controller;

    private DictionaryController() {
        super();
    }

    public static DictionaryController getController() {
        if(controller == null) {
            return new DictionaryController();
        } else {
            return controller;
        }
    }

    public void updateDictionary(ArrayList<Dictionary> response) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(response);
        mRealm.commitTransaction();
        mRealm.close();
    }

    /*public void updateBicDictionary(ArrayList<Dictionary> response) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mRealm.where(Dictionary.class).equalTo("type",0),response);

        mRealm.where(Dictionary.class).equalTo("type",0).
        mRealm.commitTransaction();
        mRealm.close();
    }*/

    public void updateCountries(ArrayList<Country> response) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(response);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public void updateForeignBanks(ArrayList<ForeignBank> response) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(response);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public ArrayList<Dictionary> getDictionaryKnp() {
        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
                .equalTo("type",1).findAll()));
    }

    public ArrayList<Dictionary> getDictionaryVoCodes() {
        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
                .equalTo("type",4).findAll()));
    }

    public ArrayList<Dictionary> getDictionaryBic() {
        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
                .equalTo("type",0).findAll()));
    }

    public ArrayList<Dictionary> getCountriesForRegMastercard() {
        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
                .equalTo("type",7).findAll()));
    }

    public Dictionary getKnpByCode(String code) {
        return mRealm.where(Dictionary.class).equalTo("code",code).findFirst();
    }

    public Dictionary getBicByCode(String code) {
        return mRealm.where(Dictionary.class).equalTo("code",code).findFirst();
    }

    public ArrayList<Country> getCountries() {
        return new ArrayList<> (mRealm.where(Country.class).findAll());
    }

    public ArrayList<ForeignBank> getForeignBanks() {
        return new ArrayList<>(mRealm.where(ForeignBank.class).findAll());
    }
}
