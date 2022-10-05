package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import java.util.List;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.room_db.daos.DictionaryDao;
import kz.optimabank.optima24.room_db.repository.DictionariesRepository;

public class DictionariesRepositoryImpl implements DictionariesRepository {

    private static final String TAG = DictionariesRepositoryImpl.class.getSimpleName();
    private final DictionaryDao dao = OptimaBank.getInstance().appDatabase.dictionaryDao();

    @Override
    public void insertAll(List<Dictionary> dictionaries) {
        Long count = dao.insertAll(dictionaries);
        Log.d(TAG, "insertAll " + count);
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