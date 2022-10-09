package kz.optimabank.optima24.db.controllers;

import kz.optimabank.optima24.db.entry.ProfilePicture;

/**
 * Created by Rasul on 23.04.2018.
 */

public class ProfilePictureController {

    private static ProfilePictureController controller;

    public static ProfilePictureController getController() {
        if (controller == null) {
            controller = new ProfilePictureController();
        }
        return controller;
    }

    public void addPicture(ProfilePicture profilePicture) {
        return;
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(profilePicture);
//        mRealm.commitTransaction();
    }

    public void deletePicture(ProfilePicture profilePicture) {
        return;
//        mRealm.beginTransaction();
//        profilePicture.deleteFromRealm();
//        mRealm.commitTransaction();
    }

    public ProfilePicture getPictureByPhone(String phone) {
        return null;
//        return mRealm.where(ProfilePicture.class).equalTo("phone", phone).findFirst();
    }

    public void close(){

    }
}
