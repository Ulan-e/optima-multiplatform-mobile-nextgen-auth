package kz.optimabank.optima24.db.controllers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
    private static PaymentContextController controller;
    private PaymentServiceRepository serviceRepository;
    private PaymentRegionsRepository regionsRepository;
    private PaymentCategoryRepository categoryRepository;
    private CountriesRepository countriesRepository;

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
        return (ArrayList<PaymentCategory>) categoryRepository.getAll();
//        return new ArrayList<>(mRealm.where(PaymentCategory.class).findAll());
    }

    public ArrayList<PaymentRegions> getAllPaymentRegions() {
        return (ArrayList<PaymentRegions>) regionsRepository.getAll();
//        return new ArrayList<>(mRealm.where(PaymentRegions.class).findAll());
    }

    public ArrayList<Country> getAllPaymentCountryes() {
        return (ArrayList<Country>) countriesRepository.getAll();
//        return new ArrayList<>(mRealm.where(Country.class).findAll());
    }

    public ArrayList<PaymentCategory> getPaymentCategory(Context context) {
        return new ArrayList<>(getAllPaymentCategory());
    }

    public PaymentCategory getPaymentCategoryByServiceId(int serviceId) {
        return categoryRepository.getById(serviceId);
//        return mRealm.where(PaymentCategory.class).equalTo("id",serviceId).findFirst();
    }

    public ArrayList<PaymentService> getPaymentServiceByCategoryId(int id) {
        return (ArrayList<PaymentService>) serviceRepository.getAllById(id);
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
        return serviceRepository.getById(id);
        //   return mRealm.where(PaymentService.class).equalTo("ExternalId",id).findFirst();
    }

    public void close(){
        serviceRepository.deleteAll();
    }
}
