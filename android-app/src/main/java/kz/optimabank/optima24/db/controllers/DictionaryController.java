package kz.optimabank.optima24.db.controllers;

import java.util.ArrayList;

import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.room_db.repository.CountriesRepository;
import kz.optimabank.optima24.room_db.repository.DictionariesRepository;
import kz.optimabank.optima24.room_db.repository.ForeignBankRepository;
import kz.optimabank.optima24.room_db.repository.impl.CountriesRepositoryImpl;
import kz.optimabank.optima24.room_db.repository.impl.DictionariesRepositoryImpl;
import kz.optimabank.optima24.room_db.repository.impl.ForeignBankRepositoryImpl;

/**
 * Created by Timur on 13.05.2017.
 */

public class DictionaryController {
    private static DictionaryController controller;

    private DictionariesRepository dictionariesRepository;
    private CountriesRepository countriesRepository;
    private ForeignBankRepository foreignBankRepository;

    private DictionaryController() {
        super();
        dictionariesRepository = new DictionariesRepositoryImpl();
        countriesRepository = new CountriesRepositoryImpl();
        foreignBankRepository = new ForeignBankRepositoryImpl();
    }

    public static DictionaryController getController() {
        if (controller == null) {
            return new DictionaryController();
        } else {
            return controller;
        }
    }

    public void updateDictionary(ArrayList<Dictionary> response) {
        dictionariesRepository.insertAll(response);
    }

    public void updateCountries(ArrayList<Country> response) {
        countriesRepository.insertAll(response);
    }

    public void updateForeignBanks(ArrayList<ForeignBank> response) {
        foreignBankRepository.insertAll(response);
    }

    public ArrayList<Dictionary> getDictionaryKnp() {
        return (ArrayList<Dictionary>) dictionariesRepository.getAllByType(1);
//        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
//                .equalTo("type",1).findAll()));
    }

    public ArrayList<Dictionary> getDictionaryVoCodes() {
        return (ArrayList<Dictionary>) dictionariesRepository.getAllByType(4);
//        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
//                .equalTo("type",4).findAll()));
    }

    public ArrayList<Dictionary> getDictionaryBic() {
        return (ArrayList<Dictionary>) dictionariesRepository.getAllByType(0);
//        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
//                .equalTo("type",0).findAll()));
    }

    public ArrayList<Dictionary> getCountriesForRegMastercard() {
        return (ArrayList<Dictionary>) dictionariesRepository.getAllByType(7);
//        return new ArrayList<> (mRealm.copyFromRealm(mRealm.where(Dictionary.class)
//                .equalTo("type",7).findAll()));
    }

    public Dictionary getKnpByCode(String code) {
        return dictionariesRepository.getByType(code);
//        return mRealm.where(Dictionary.class).equalTo("code",code).findFirst();
    }

    public Dictionary getBicByCode(String code) {
        return dictionariesRepository.getByType(code);
//        return mRealm.where(Dictionary.class).equalTo("code",code).findFirst();
    }

    public ArrayList<Country> getCountries() {
        return (ArrayList<Country>) countriesRepository.getAll();
//        return new ArrayList<> (mRealm.where(Country.class).findAll());
    }

    public ArrayList<ForeignBank> getForeignBanks() {
        return (ArrayList<ForeignBank>) foreignBankRepository.getAll();
//        return new ArrayList<>(mRealm.where(ForeignBank.class).findAll());
    }

    public void close(){
        countriesRepository.delete();
        dictionariesRepository.deleteAll();
        foreignBankRepository.delete();
    }
}
