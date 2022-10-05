package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import java.util.List;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.room_db.daos.PaymentRegionsDao;
import kz.optimabank.optima24.room_db.repository.PaymentRegionsRepository;

public class PaymentRegionsRepositoryImpl implements PaymentRegionsRepository {

    private static final String TAG = PaymentRegionsRepositoryImpl.class.getSimpleName();
    private final PaymentRegionsDao dao = OptimaBank.getInstance().appDatabase.paymentRegionsDao();

    @Override
    public void insertAll(List<PaymentRegions> paymentRegions) {
        Long count = dao.insertAll(paymentRegions);
        Log.d(TAG, "insertAll " + count);
    }

    @Override
    public List<PaymentRegions> getAll() {
        List<PaymentRegions> paymentRegions = dao.getAll();
        Log.d(TAG, "getAll " + paymentRegions.size());
        return paymentRegions;
    }

    @Override
    public PaymentRegions getById(Integer id) {
        PaymentRegions paymentRegions = dao.getById(id);
        Log.d(TAG, "getById " + paymentRegions);
        return paymentRegions;
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
