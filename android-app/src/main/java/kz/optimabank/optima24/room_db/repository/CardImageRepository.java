package kz.optimabank.optima24.room_db.repository;

import java.util.List;

import kz.optimabank.optima24.db.entry.DigitizedCard;

public interface CardImageRepository{

    List<DigitizedCard> getAll(String phone);

    void addCardImage(DigitizedCard digitizedCard);

    DigitizedCard getCardImage(String rbsNumber, String phone);

    void delete();
}
