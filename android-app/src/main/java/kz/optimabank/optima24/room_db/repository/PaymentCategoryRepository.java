package kz.optimabank.optima24.room_db.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.PaymentCategory;

public interface PaymentCategoryRepository {

    void insertAll(List<PaymentCategory> paymentCategories);

    List<PaymentCategory> getAll();

    List<PaymentCategory> getAllById(Integer id);

    PaymentCategory getById(Integer id);

    void deleteAll();
}