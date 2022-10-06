package kz.optimabank.optima24.room_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.db.entry.ProfilePicture;
import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.room_db.daos.CardImageDao;
import kz.optimabank.optima24.room_db.daos.CountryDao;
import kz.optimabank.optima24.room_db.daos.DictionaryDao;
import kz.optimabank.optima24.room_db.daos.ForeignBankDao;
import kz.optimabank.optima24.room_db.daos.NotificationDao;
import kz.optimabank.optima24.room_db.daos.PaymentCategoryDao;
import kz.optimabank.optima24.room_db.daos.PaymentRegionsDao;
import kz.optimabank.optima24.room_db.daos.PaymentServiceDao;
import kz.optimabank.optima24.room_db.daos.ProfilePictureDao;

@Database(entities = {
        Notification.class,
        DigitizedCard.class,
        Country.class,
        Dictionary.class,
        ForeignBank.class,
        PaymentRegions.class,
        PaymentService.class,
        ProfilePicture.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // не менять название базы данных
    private static final String DATABASE_NAME = "notifications11.db";
    private static volatile AppDatabase INSTANCE;

    public abstract NotificationDao notificationDao();

    public abstract CardImageDao cardImageDao();

    public abstract CountryDao countryDao();

    public abstract DictionaryDao dictionaryDao();

    public abstract ForeignBankDao foreignBankDao();

    public abstract PaymentCategoryDao paymentCategoryDao();

    public abstract PaymentServiceDao paymentServiceDao();

    public abstract PaymentRegionsDao paymentRegionsDao();

    public abstract ProfilePictureDao profilePictureDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if (INSTANCE != null) {
            if (INSTANCE.isOpen())
                INSTANCE.close();
            INSTANCE = null;
        }
    }
}