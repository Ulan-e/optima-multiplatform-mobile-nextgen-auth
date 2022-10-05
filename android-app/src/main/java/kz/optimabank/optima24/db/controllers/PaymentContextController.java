package kz.optimabank.optima24.db.controllers;

import android.content.Context;

import java.util.ArrayList;

import io.realm.RealmResults;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.db.entry.PaymentService;

/**
  Created by Timur on 14.04.2017.
 */

public class PaymentContextController extends DBController {
    private static PaymentContextController controller;

    private PaymentContextController() {
        super();
    }

    public static PaymentContextController getController() {
        return new PaymentContextController();
    }

    public void updatePaymentCategory(ArrayList<PaymentCategory> response) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(response);
        mRealm.commitTransaction();
    }

    public void updatePaymentService(ArrayList<PaymentService> response) {
        mRealm.beginTransaction();
        //mRealm.copyToRealmOrUpdate(response);
        mRealm.insertOrUpdate(response);
        mRealm.commitTransaction();
    }

    public void updatePaymentRegions(ArrayList<PaymentRegions> response) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(response);
        mRealm.commitTransaction();
    }

    public void updatePaymentCountry(ArrayList<Country> response) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(response);
        mRealm.commitTransaction();
    }

    public ArrayList<PaymentCategory> getAllPaymentCategory() {
        return new ArrayList<>(mRealm.where(PaymentCategory.class).findAll());
    }

    public ArrayList<PaymentRegions> getAllPaymentRegions() {
        return new ArrayList<>(mRealm.where(PaymentRegions.class).findAll());
    }

    public ArrayList<Country> getAllPaymentCountryes() {
        return new ArrayList<>(mRealm.where(Country.class).findAll());
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
        return mRealm.where(PaymentCategory.class).equalTo("id",serviceId).findFirst();
    }

    public ArrayList<PaymentService> getPaymentServiceByCategoryId(int id) {
        return new ArrayList<> (mRealm.copyFromRealm
                (mRealm.where(PaymentService.class).equalTo("paymentCategoryId",id).findAll()));
    }

    public PaymentService getPaymentServiceById(int id) {
        return mRealm.where(PaymentService.class).equalTo("id",id).findFirst();
    }

    public RealmResults<PaymentService> getAllPaymentService() {
        return mRealm.where(PaymentService.class).findAll();
    }

    public PaymentService getPaymentServiceByExternalId(int id) {
        return mRealm.where(PaymentService.class).equalTo("ExternalId",id).findFirst();
    }
}
