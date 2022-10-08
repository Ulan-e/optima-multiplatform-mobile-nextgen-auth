package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import java.util.List;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.room_db.daos.PaymentCategoryDao;
import kz.optimabank.optima24.room_db.repository.PaymentCategoryRepository;

public class PaymentCategoryRepositoryImpl implements PaymentCategoryRepository {

    private static final String TAG = PaymentCategoryRepositoryImpl.class.getSimpleName();
    private final PaymentCategoryDao dao = OptimaApp.Companion.getAppDatabase().paymentCategoryDao();

    @Override
    public void insertAll(List<PaymentCategory> paymentCategories) {
        List<Long> count = dao.insertAll(paymentCategories);
        Log.d(TAG, "insertAll " + count.size());
    }

    @Override
    public List<PaymentCategory> getAll() {
        List<PaymentCategory> categories = dao.getAll();
        Log.d(TAG, "getAll " + categories.size());
        return categories;
    }

    @Override
    public List<PaymentCategory> getAllById(Integer id) {
        List<PaymentCategory> categories = dao.getAllBYId(id);
        Log.d(TAG, "getAllById " + categories.size());
        return categories;
    }

    @Override
    public PaymentCategory getById(Integer id) {
        PaymentCategory paymentCategory = dao.getById(id);
        Log.d(TAG, "getById " + paymentCategory);
        return paymentCategory;
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
