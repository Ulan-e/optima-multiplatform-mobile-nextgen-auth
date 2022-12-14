package kz.optimabank.optima24.local.repository.implementations;

import android.util.Log;

import java.util.List;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.local.daos.DictionaryDao;
import kz.optimabank.optima24.local.repository.DictionariesRepository;

public class DictionariesRepositoryImpl implements DictionariesRepository {

    private static final String TAG = DictionariesRepositoryImpl.class.getSimpleName();
    private final DictionaryDao dao = OptimaApp.Companion.getAppDatabase().dictionaryDao();

    @Override
    public void insertAll(List<Dictionary> dictionaries) {
        List<Long> count = dao.insertAll(dictionaries);
        Log.d(TAG, "insertAll " + count.size());
    }

    @Override
    public List<Dictionary> getAll() {
        List<Dictionary> dictionaries = dao.getAll();
        Log.d(TAG, "getAll " + dictionaries.size());
        return dictionaries;
    }

    @Override
    public List<Dictionary> getAllByType(Integer type) {
        List<Dictionary> dictionaries = dao.fetchByType(type);
        Log.d(TAG, "getByType " + dictionaries);
        return dictionaries;
    }

    @Override
    public Dictionary getByType(String code) {
        Dictionary dictionary = dao.getByCode(code);
        Log.d(TAG, "getByType " + dictionary);
        return dictionary;
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}