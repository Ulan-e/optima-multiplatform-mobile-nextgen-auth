package kz.optimabank.optima24.local.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.PaymentRegions;

public interface PaymentRegionsRepository {

    void insertAll(List<PaymentRegions> paymentRegions);

    List<PaymentRegions> getAll();

    PaymentRegions getById(Integer id);

    void deleteAll();
}
