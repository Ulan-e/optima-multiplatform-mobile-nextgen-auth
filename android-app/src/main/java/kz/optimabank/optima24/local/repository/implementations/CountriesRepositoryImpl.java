package kz.optimabank.optima24.local.repository.implementations;

import android.util.Log;

import java.util.List;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.local.daos.CountryDao;
import kz.optimabank.optima24.local.repository.CountriesRepository;

public class CountriesRepositoryImpl implements CountriesRepository {

    private static final String TAG = CountriesRepositoryImpl.class.getSimpleName();
    private final CountryDao dao = OptimaApp.Companion.getAppDatabase().countryDao();

    @Override
    public void insertAll(List<Country> countries) {
        List<Long> count = dao.insertAll(countries);
        Log.d(TAG, "insertAll " + count.size());
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