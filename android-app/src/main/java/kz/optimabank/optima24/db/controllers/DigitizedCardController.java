package kz.optimabank.optima24.db.controllers;

import android.util.Log;

import java.util.ArrayList;

import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.room_db.repository.CardImageRepository;
import kz.optimabank.optima24.room_db.repository.impl.CardImageRepositoryImpl;

public class DigitizedCardController {
    private static DigitizedCardController controller;
    private CardImageRepository cardImageRepository;

    private DigitizedCardController() {
        super();
        cardImageRepository = new CardImageRepositoryImpl();
    }

    public static DigitizedCardController getController() {
        if (controller == null) {
            return new DigitizedCardController();
        } else {
            return controller;
        }
    }

    public DigitizedCard isExistDigitizedCard(String phone) {
        return null;
//        return mRealm.where(DigitizedCard.class).equalTo("userPhone", phone)
//                .equalTo("cardSuspended",false).equalTo("cardInactive",false).findFirst();
    }

    public boolean saveDigitizedCard(DigitizedCard card) {
        return false;
//        try {
//            mRealm.beginTransaction();
//            mRealm.copyToRealm(card);
//            mRealm.commitTransaction();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            if(mRealm.isInTransaction()) {
//                mRealm.commitTransaction();
//            }
//            return false;
//        }
    }

    public void setDefaultDigitizedCard(DigitizedCard digitizedCard, boolean isDefault) {
//        if (digitizedCard != null) {
//            try {
//                mRealm.beginTransaction();
//                digitizedCard.setDefault(isDefault);
//                mRealm.commitTransaction();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void setSuspendDigitizedCard(DigitizedCard digitizedCard, boolean isSuspend) {
//        if (digitizedCard != null) {
//            try {
//                mRealm.beginTransaction();
//                digitizedCard.setCardSuspended(isSuspend);
//                mRealm.commitTransaction();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void clearDefaultDigitizedCard(String userPhone) {
//        DigitizedCard digitizedCard = getDefaultDigitizedCard(userPhone);
//        if (digitizedCard != null) {
//            setDefaultDigitizedCard(digitizedCard, false);
//        }
    }

    public DigitizedCard getDigitizedCardByRbs(String rbsNumber, String userPhone) {
//        return mRealm.where(DigitizedCard.class).equalTo("rbsNumber", rbsNumber)
//                .equalTo("userPhone", userPhone).findFirst();
        return cardImageRepository.getCardImage(rbsNumber, userPhone);
    }

    public DigitizedCard getDigitizedCardByRbs(String rbsNumber) {
        return null;
//        return mRealm.where(DigitizedCard.class).equalTo("rbsNumber", rbsNumber).findFirst();
    }

    public DigitizedCard getDefaultNotSuspendDigitizedCard(String userPhone) {
//        return mRealm.where(DigitizedCard.class).equalTo("isDefault", true)
//                .equalTo("userPhone", userPhone).equalTo("cardSuspended",false)
//                .equalTo("cardInactive",false).equalTo("isBlocked",false)
//                .equalTo("isClosed",false).equalTo("isVisible",true).findFirst();
        return null;
    }

    public DigitizedCard getDefaultDigitizedCard(String userPhone) {
        return null;
        //DigitizedCard digitizedCard = mRealm.where(DigitizedCard.class).equalTo("isDefault", true).findFirst();
        //DigitizedCard digitizedCard2 = mRealm.where(DigitizedCard.class).equalTo("isDefault", true)
        //        .equalTo("userPhone", userPhone).findFirst();
        //Log.i("AppLifeCycle"," mRealm.where(DigitizedCard.class).equalTo(isDefault, true) = " + digitizedCard);
        //Log.i("AppLifeCycle"," mRealm.where(DigitizedCard.class).equalTo(isDefault, true)" +
        //        ".equalTo(\"userPhone\", userPhone).findFirst() = " + digitizedCard2);
        //Log.i("AppLifeCycle"," mRealm = " + mRealm);
//        return mRealm.where(DigitizedCard.class).equalTo("isDefault", true)
//                .equalTo("userPhone", userPhone).findFirst();
    }

    public ArrayList<DigitizedCard> getAllCards(String userPhone) {
        return (ArrayList<DigitizedCard>) cardImageRepository.getAll(userPhone);
//        return new ArrayList<>(mRealm.where(DigitizedCard.class).equalTo("userPhone", userPhone).findAll());
    }

    public ArrayList<DigitizedCard> getAllNeedReplenishCards() {
        return null;
//        return new ArrayList<>(mRealm.where(DigitizedCard.class).equalTo("needReplenish", true).findAll());
    }

    public ArrayList<DigitizedCard> getWorkingCard(String userPhone) {
        return null;
//        return new ArrayList<>(mRealm.where(DigitizedCard.class).equalTo("userPhone", userPhone)
//                .equalTo("cardSuspended",false).equalTo("isBlocked", false)
//                .equalTo("isClosed",false).equalTo("isVisible",true)
//                .equalTo("cardInactive",false).findAll());
    }

    public void updateDigitizedCard(DigitizedCard digitizedCard, boolean cardSuspended, boolean cardInactive, boolean isBlocked,
                                    boolean isVisible, boolean isClosed, byte[] byteArrayFullImg, byte[] byteArrayMiniatureImg) {
//        if (digitizedCard != null) {
//            try {
//                mRealm.beginTransaction();
//                digitizedCard.setClosed(isClosed);
//                digitizedCard.setBlocked(isBlocked);
//                digitizedCard.setCardSuspended(cardSuspended);
//                digitizedCard.setCardInactive(cardInactive);
//                digitizedCard.setVisible(isVisible);
//                //if (byteArrayFullImg!=null)
//                //    digitizedCard.setByteArrayFullImg(byteArrayFullImg);
//                //if (byteArrayMiniatureImg!=null)
//                //    digitizedCard.setByteArrayMiniatureImg(byteArrayMiniatureImg);
//                mRealm.commitTransaction();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void updateImagesDigitizedCardFull(DigitizedCard digitizedCard, byte[] byteArrayFullImg) {
        if (byteArrayFullImg != null) {
            digitizedCard.setByteArrayFullImg(byteArrayFullImg);
            cardImageRepository.addCardImage(digitizedCard);
        }
    }

    public void updateImagesDigitizedCardMiniature(DigitizedCard digitizedCard, byte[] byteArrayMiniatureImg) {
        if (digitizedCard != null) {
            if (byteArrayMiniatureImg != null) {
                digitizedCard.setByteArrayFullImg(byteArrayMiniatureImg);
                cardImageRepository.addCardImage(digitizedCard);
            }
        }
//        }
//            try {
//                //Log.i("byteArrayFullImgUPDATE","DCC byteArrayMiniatureImg = "+byteArrayMiniatureImg);
//                //Log.i("byteArrayFullImgUPDATE","DCC digitizedCard.getByteArrayMiniatureImg() = "+digitizedCard.getByteArrayMiniatureImg());
//                mRealm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        if (byteArrayMiniatureImg!=null)
//                            digitizedCard.setByteArrayMiniatureImg(byteArrayMiniatureImg);
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void updateStatusCard(DigitizedCard digitizedCard, boolean cardSuspended, boolean cardInactive) {
//        if (digitizedCard != null) {
//            try {
//                mRealm.beginTransaction();
//                digitizedCard.setCardSuspended(cardSuspended);
//                digitizedCard.setCardInactive(cardInactive);
//                mRealm.commitTransaction();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void updateStatusCard(DigitizedCard digitizedCard, boolean isBlock) {
//        if (digitizedCard != null) {
//            try {
//                mRealm.beginTransaction();
//                digitizedCard.setBlocked(isBlock);
//                mRealm.commitTransaction();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void updateReplenish(DigitizedCard digitizedCard, boolean needReplenish) {
//        if (digitizedCard != null) {
//            try {
//                mRealm.beginTransaction();
//                digitizedCard.setNeedReplenish(needReplenish);
//                mRealm.commitTransaction();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void deleteDigitizedCard(DigitizedCard digitizedCard) {
//        if (digitizedCard != null) {
//            try {
//                mRealm.beginTransaction();
//                digitizedCard.deleteFromRealm();
//                mRealm.commitTransaction();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void close(){
        cardImageRepository.delete();
    }
}
