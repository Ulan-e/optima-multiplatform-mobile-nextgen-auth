package kz.optimabank.optima24.room_db.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.PaymentService;

public interface PaymentServiceRepository {

    void insertAll(List<PaymentService> paymentServices);

    List<PaymentService> getAll();

    List<PaymentService> getAllById(Integer id);

    PaymentService getById(Integer id);

    void deleteAll();
}
