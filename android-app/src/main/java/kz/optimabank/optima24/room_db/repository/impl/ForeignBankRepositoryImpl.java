package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import java.util.List;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.room_db.daos.ForeignBankDao;
import kz.optimabank.optima24.room_db.repository.ForeignBankRepository;

public class ForeignBankRepositoryImpl implements ForeignBankRepository {

    private static final String TAG = ForeignBankRepositoryImpl.class.getSimpleName();
    private final ForeignBankDao dao = OptimaBank.getInstance().appDatabase.foreignBankDao();

    @Override
    public void insertAll(List<ForeignBank> foreignBanks) {
        Long count = dao.insertAll(foreignBanks);
        Log.d(TAG, "insertAll " + count);
    }

    @Override
    public List<ForeignBank> getAll() {
        List<ForeignBank> foreignBanks = dao.getAll();
        Log.d(TAG, "getAll " + foreignBanks.size());
        return foreignBanks;
    }

    @Override
    public ForeignBank getForeignBank(int id) {
        ForeignBank foreignBank = dao.getById(id);
        Log.d(TAG,"getForeignBank " +foreignBank);
        return foreignBank;
    }

    @Override
    public void delete() {
        dao.deleteAll();
    }
}
