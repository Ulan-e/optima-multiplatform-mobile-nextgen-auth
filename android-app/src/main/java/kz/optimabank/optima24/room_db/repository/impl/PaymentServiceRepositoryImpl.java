package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import java.util.List;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.room_db.daos.PaymentServiceDao;
import kz.optimabank.optima24.room_db.repository.PaymentServiceRepository;

public class PaymentServiceRepositoryImpl implements PaymentServiceRepository {

    private static final String TAG = PaymentServiceRepositoryImpl.class.getSimpleName();
    private final PaymentServiceDao dao = OptimaApp.Companion.getAppDatabase().paymentServiceDao();

    @Override
    public void insertAll(List<PaymentService> paymentServices) {
        List<Long> count = dao.insertAll(paymentServices);
        Log.d(TAG, "insertAll " + count.size());
    }

    @Override
    public List<PaymentService> getAll() {
        List<PaymentService> paymentServices = dao.getAll();
        Log.d(TAG, "getAll " + paymentServices.size());
        return paymentServices;
    }

    @Override
    public List<PaymentService> getServiceByCategoryId(int id) {
        List<PaymentService> paymentServices = dao.getByCategoryId(id);
        Log.d(TAG, "getServiceByCategoryId " + paymentServices.size());
        return paymentServices;
    }

    @Override
    public PaymentService getById(int id) {
        PaymentService paymentService = dao.getById(id);
        Log.d(TAG, "getById " + paymentService);
        return paymentService;
    }

    @Override
    public PaymentService getByExternalId(int id) {
        PaymentService paymentService = dao.getByExternalId(id);
        Log.d(TAG, "getByExternalId " + paymentService);
        return paymentService;
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
