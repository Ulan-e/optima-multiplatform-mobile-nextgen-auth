package kz.optimabank.optima24.db.module;

/**
  Created by Timyr on 13.01.2017.
 */

import io.realm.annotations.RealmModule;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.DHKey;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.db.entry.Message;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.db.entry.PredefinedValues;
import kz.optimabank.optima24.db.entry.ProfilePicture;
import kz.optimabank.optima24.db.entry.PushTokenKey;
import kz.optimabank.optima24.db.entry.Region;
import kz.optimabank.optima24.db.entry.Sender;
import kz.optimabank.optima24.model.base.Banner;
import kz.optimabank.optima24.model.base.PaymentServiceParameter;
import kz.optimabank.optima24.notifications.models.Notification;

@RealmModule(classes = {
        DHKey.class,
        Sender.class,
        PushTokenKey.class,
        PaymentCategory.class,
        PaymentService.class,
        PaymentServiceParameter.class,
        Region.class,
        Dictionary.class,
        Country.class,
        PaymentRegions.class,
        PredefinedValues.class,
        ForeignBank.class,
        ProfilePicture.class,
        DigitizedCard.class,
        Banner.class
})
public class LocalModule {
}