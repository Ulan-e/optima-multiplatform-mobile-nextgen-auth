package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.ProfilePicture;
import kz.optimabank.optima24.room_db.daos.ProfilePictureDao;
import kz.optimabank.optima24.room_db.repository.ProfilePictureRepository;

public class ProfilePictureRepositoryImpl implements ProfilePictureRepository {

    private static final String TAG = ProfilePictureRepositoryImpl.class.getSimpleName();
    private final ProfilePictureDao dao = OptimaApp.Companion.getAppDatabase().profilePictureDao();

    @Override
    public void insert(ProfilePicture profilePicture) {
        Long count = dao.insert(profilePicture);
        Log.d(TAG, "insertAll " + count);
    }

    @Override
    public ProfilePicture getByPhone(String phone) {
        ProfilePicture profilePicture = dao.getByPhone(phone);
        Log.d(TAG, "getByPhone " + profilePicture);
        return profilePicture;
    }

    @Override
    public void delete() {
        dao.delete();
    }
}