package kz.optimabank.optima24.room_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import kz.optimabank.optima24.notifications.models.Notification;
import kz.optimabank.optima24.room_db.daos.NotificationDao;

@Database(entities = {Notification.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // не менять название базы данных
    private static final String DATABASE_NAME = "notifications11.db";
    private static volatile AppDatabase INSTANCE;

    public abstract NotificationDao notificationDao();

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