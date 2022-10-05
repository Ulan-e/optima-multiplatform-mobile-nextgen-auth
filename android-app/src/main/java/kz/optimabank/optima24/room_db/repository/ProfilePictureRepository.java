package kz.optimabank.optima24.room_db.repository;

import kz.optimabank.optima24.db.entry.ProfilePicture;

public interface ProfilePictureRepository {

    void insert(ProfilePicture profilePicture);

    ProfilePicture getByPhone(String phone);

    void delete();
}