package kz.optimabank.optima24.db.controllers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kg.optima.mobile.R;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.room_db.repository.CountriesRepository;
import kz.optimabank.optima24.room_db.repository.PaymentCategoryRepository;
import kz.optimabank.optima24.room_db.repository.PaymentRegionsRepository;
import kz.optimabank.optima24.room_db.repository.PaymentServiceRepository;
import kz.optimabank.optima24.room_db.repository.impl.CountriesRepositoryImpl;
import kz.optimabank.optima24.room_db.repository.impl.PaymentCategoryRepositoryImpl;
import kz.optimabank.optima24.room_db.repository.impl.PaymentRegionsRepositoryImpl;
import kz.optimabank.optima24.room_db.repository.impl.PaymentServiceRepositoryImpl;

/**
 * Created by Timur on 14.04.2017.
 */

public class PaymentContextController {
    private final PaymentServiceRepository serviceRepository;
    private final PaymentRegionsRepository regionsRepository;
    private final PaymentCategoryRepository categoryRepository;
    private final CountriesRepository countriesRepository;

    private PaymentContextController() {
        super();
        serviceRepository = new PaymentServiceRepositoryImpl();
        regionsRepository = new PaymentRegionsRepositoryImpl();
        categoryRepository = new PaymentCategoryRepositoryImpl();
        countriesRepository = new CountriesRepositoryImpl();
    }

    public static PaymentContextController getController() {
        return new PaymentContextController();
    }

    public void updatePaymentCategory(ArrayList<PaymentCategory> response) {
        categoryRepository.insertAll(response);
    }

    public void updatePaymentService(ArrayList<PaymentService> response) {
        serviceRepository.insertAll(response);
    }

    public void updatePaymentRegions(ArrayList<PaymentRegions> response) {
        regionsRepository.insertAll(response);
    }

    public void updatePaymentCountry(ArrayList<Country> response) {
        countriesRepository.insertAll(response);
    }

    public ArrayList<PaymentCategory> getAllPaymentCategory() {
        return new ArrayList<>(categoryRepository.getAll());
    }

    public ArrayList<PaymentRegions> getAllPaymentRegions() {
        return new ArrayList<>(regionsRepository.getAll());
    }

    public ArrayList<Country> getAllPaymentCountryes() {
        return new ArrayList<>(countriesRepository.getAll());
    }

    public ArrayList<PaymentCategory> getPaymentCategory(Context context) {
        ArrayList<PaymentCategory> paymentCategories = new ArrayList<>();

        for (PaymentCategory paymentCategory : getAllPaymentCategory()) {
            if (!paymentCategory.getName().toUpperCase().contains(context.getString(R.string.payment_fines).toUpperCase())) {
                paymentCategories.add(paymentCategory);
            } else {
                paymentCategories.add(paymentCategory);
            }
        }

        return paymentCategories;
    }

    public PaymentCategory getPaymentCategoryByServiceId(int serviceId) {
        return categoryRepository.getById(serviceId);
//        return mRealm.where(PaymentCategory.class).equalTo("id",serviceId).findFirst();
    }

    public ArrayList<PaymentService> getPaymentServiceByCategoryId(int id) {
        return new ArrayList<>(serviceRepository.getServiceByCategoryId(id));
//        return new ArrayList<> (mRealm.copyFromRealm
//                (mRealm.where(PaymentService.class).equalTo("paymentCategoryId",id).findAll()));
    }

    public PaymentService getPaymentServiceById(int id) {
        return serviceRepository.getById(id);
//        return mRealm.where(PaymentService.class).equalTo("id",id).findFirst();
    }

    public List<PaymentService> getAllPaymentService() {
        return serviceRepository.getAll();
//        return mRealm.where(PaymentService.class).findAll();
    }

    public PaymentService getPaymentServiceByExternalId(int id) {
        return serviceRepository.getByExternalId(id);
        //   return mRealm.where(PaymentService.class).equalTo("ExternalId",id).findFirst();
    }

    public void close() {

    }
}
