package kz.optimabank.optima24.local.repository.implementations;

import android.util.Log;

import java.util.List;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.local.daos.PaymentRegionsDao;
import kz.optimabank.optima24.local.repository.PaymentRegionsRepository;

public class PaymentRegionsRepositoryImpl implements PaymentRegionsRepository {

    private static final String TAG = PaymentRegionsRepositoryImpl.class.getSimpleName();
    private final PaymentRegionsDao dao = OptimaApp.Companion.getAppDatabase().paymentRegionsDao();

    @Override
    public void insertAll(List<PaymentRegions> paymentRegions) {
        List<Long> count = dao.insertAll(paymentRegions);
        Log.d(TAG, "insertAll " + count.size());
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
