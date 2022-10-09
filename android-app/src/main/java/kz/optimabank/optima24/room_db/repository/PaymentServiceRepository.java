package kz.optimabank.optima24.room_db.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.PaymentService;

public interface PaymentServiceRepository {

    void insertAll(List<PaymentService> paymentServices);

    List<PaymentService> getAll();

    List<PaymentService> getServiceByCategoryId(int id);

    PaymentService getById(int id);

    PaymentService getByExternalId(int id);

    void deleteAll();
}