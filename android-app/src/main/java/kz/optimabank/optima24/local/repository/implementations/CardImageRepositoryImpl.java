package kz.optimabank.optima24.local.repository.implementations;

import android.util.Log;

import java.util.List;

import kg.optima.mobile.android.OptimaApp;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.local.repository.database.AppDatabase;
import kz.optimabank.optima24.local.daos.CardImageDao;
import kz.optimabank.optima24.local.repository.CardImageRepository;

public class CardImageRepositoryImpl implements CardImageRepository {

    private static final String TAG = "CardImageRepository";
    private final AppDatabase database = AppDatabase.getInstance(OptimaApp.Companion.getInstance());
    private final CardImageDao dao = database.cardImageDao();

    @Override
    public List<DigitizedCard> getAll(String phone) {
        List<DigitizedCard> digitizedCards = dao.getAll(phone);
        Log.d(TAG, "getAll " + digitizedCards.size());
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
