package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import java.util.List;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.room_db.daos.CountryDao;
import kz.optimabank.optima24.room_db.repository.CountriesRepository;

public class CountriesRepositoryImpl implements CountriesRepository {

    private static final String TAG = CountriesRepositoryImpl.class.getSimpleName();
    private final CountryDao dao = OptimaApp.Companion.getAppDatabase().countryDao();

    @Override
    public void insertAll(List<Country> countries) {
        Long count = dao.insertAll(countries);
        Log.d(TAG, "insertAll " + count);
    }

    @Override
    public List<Country> getAll() {
        List<Country> countries = dao.getAll();
        Log.d(TAG, "getAll " + countries.size());
        return countries;
    }

    @Override
    public void delete() {
        dao.deleteAll();
    }
}