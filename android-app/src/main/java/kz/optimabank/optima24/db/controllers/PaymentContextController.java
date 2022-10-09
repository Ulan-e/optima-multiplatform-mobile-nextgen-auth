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
        Log.d("terra", "getAllPaymentCategory --->>>>>>>" + categoryRepository.getAll().size());
        return new ArrayList<>(categoryRepository.getAll());
//        return new ArrayList<>(mRealm.where(PaymentCategory.class).findAll());
    }

    public ArrayList<PaymentRegions> getAllPaymentRegions() {
        Log.d("terra", "getAllPaymentRegions --->>>>>>>" + regionsRepository.getAll().size());
        return new ArrayList<>(regionsRepository.getAll());
//        return new ArrayList<>(mRealm.where(PaymentRegions.class).findAll());
    }

    public ArrayList<Country> getAllPaymentCountryes() {
        Log.d("terra", "getAllPaymentCountryes --->>>>>" + countriesRepository.getAll().size());
        return new ArrayList<>(countriesRepository.getAll());
//        return new ArrayList<>(mRealm.where(Country.class).findAll());
    }

    public ArrayList<PaymentCategory> getPaymentCategory(Context context) {
        ArrayList<PaymentCategory> paymentCategories = new ArrayList<>();

        for(PaymentCategory paymentCategory : getAllPaymentCategory()) {
            if(!paymentCategory.getName().toUpperCase().contains(context.getString(R.string.payment_fines).toUpperCase())) {
                paymentCategories.add(paymentCategory);
            }else{
                paymentCategories.add(paymentCategory);
            }
        }

        return paymentCategories;
    }

    public PaymentCategory getPaymentCategoryByServiceId(int serviceId) {
        Log.d("terra", "getPaymentCategoryByServiceId " + serviceId);
        return categoryRepository.getById(serviceId);
//        return mRealm.where(PaymentCategory.class).equalTo("id",serviceId).findFirst();
    }

    public ArrayList<PaymentService> getPaymentServiceByCategoryId(int id) {
        Log.d("terra", "getPaymentServiceByCategoryId --->>>> " + serviceRepository.getServiceByCategoryId(id).size());
        return new ArrayList<>(serviceRepository.getServiceByCategoryId(id));
//        return new ArrayList<> (mRealm.copyFromRealm
//                (mRealm.where(PaymentService.class).equalTo("paymentCategoryId",id).findAll()));
    }

    public PaymentService getPaymentServiceById(int id) {
        Log.d("terra", "getPaymentServiceById <<<<<<<<" + serviceRepository.getById(id));
        return serviceRepository.getById(id);
//        return mRealm.where(PaymentService.class).equalTo("id",id).findFirst();
    }

    public List<PaymentService> getAllPaymentService() {
        return serviceRepository.getAll();
//        return mRealm.where(PaymentService.class).findAll();
    }

    public PaymentService getPaymentServiceByExternalId(int id) {
        Log.d("terra", "getPaymentServiceByExternalId " + id);
        return serviceRepository.getByExternalId(id);
        //   return mRealm.where(PaymentService.class).equalTo("ExternalId",id).findFirst();
    }

    public void close(){
        serviceRepository.deleteAll();
    }
}
