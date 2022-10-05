package kz.optimabank.optima24.room_db.repository.impl;

import android.util.Log;

import java.util.List;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.room_db.daos.CardImageDao;
import kz.optimabank.optima24.room_db.repository.CardImageRepository;

public class CardImageRepositoryImpl implements CardImageRepository {

    private static final String TAG = CardImageRepositoryImpl.class.getSimpleName();
    private final CardImageDao dao = OptimaBank.getInstance().appDatabase.cardImageDao();

    @Override
    public List<DigitizedCard> getAll(String phone) {
        List<DigitizedCard> digitizedCards = dao.getAll(phone);
        Log.d(TAG, "addCardImage " + digitizedCards.size());
        return digitizedCards;
    }

    @Override
    public void addCardImage(DigitizedCard digitizedCard) {
        Long id = dao.insert(digitizedCard);
        Log.d(TAG, "addCardImage " + id);

    }

    @Override
    public DigitizedCard getCardImage(String rbsNumber, String phone) {
        DigitizedCard digitizedCard = dao.getByRbsNumber(rbsNumber, phone);
        Log.d(TAG, "getCardImage " + digitizedCard);
        return digitizedCard;
    }

    @Override
    public void delete() {
        dao.delete();
    }
}
