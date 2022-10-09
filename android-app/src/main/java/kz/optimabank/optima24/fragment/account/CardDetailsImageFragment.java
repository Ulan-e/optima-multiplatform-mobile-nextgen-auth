package kz.optimabank.optima24.fragment.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.db.controllers.DigitizedCardController;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

/**
  Created by Timur on 05.06.2017.
 */

public class CardDetailsImageFragment extends ATFFragment {
    @BindView(R.id.cardView) CardView cardView;
    @BindView(R.id.tvAmount) TextView tvAmount;
    @BindView(R.id.background) View background;

    @BindView(R.id.cvMulti) CardView cvMulti;
    @BindView(R.id.cvDefaultIcon) CardView cvDefaultIcon;
    @BindView(R.id.tvCardNumber) TextView tvCardNumber;
    @BindView(R.id.tvCardType) TextView tvCardType;
    @BindView(R.id.ivBrandCardLogo) ImageView ivBrandCardLogo;
    @BindView(R.id.imgStar) ImageView imgStar;
    UserAccounts.Cards card;
    DigitizedCard digitizedCard;
    boolean isContactlessHistory, isPayment;

    public static CardDetailsImageFragment instantiateWithArgs(final Context context, final UserAccounts.Cards card) {
        final CardDetailsImageFragment fragment = (CardDetailsImageFragment) instantiate(context, CardDetailsImageFragment.class.getName());
        final Bundle args = new Bundle();
        args.putSerializable("userCard",card);
        fragment.setArguments(args);
        return fragment;
    }

    public static CardDetailsImageFragment instantiateWithDigitizedCardsArgs(final Context context,
                                                                             final DigitizedCard card) {
        final CardDetailsImageFragment fragment = (CardDetailsImageFragment) instantiate(context, CardDetailsImageFragment.class.getName());
        final Bundle args = new Bundle();
        args.putString("rbsDigitizedCard", card.getRbsNumber());
        args.putBoolean("isContactlessHistory", true);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_details_image_fragment, container, false);
        ButterKnife.bind(this, view);
        ViewCompat.setElevation(cardView, 0);
        getBundle();
        if(card!=null) {
            Bitmap bitmap;
            /*try {
                bitmap = card.readObject(true);
            } catch (IOException | ClassNotFoundException e) {
                bitmap = null;
                e.printStackTrace();
            }*/
            byte[] full = card.getByteArrayFullImg();
            if (full!=null)
                bitmap = BitmapFactory.decodeByteArray(full, 0, full.length);
            else
                bitmap = null;
            initCardView(card.number, card.isMultiBalance, card.productName, bitmap, card.getCardExpireDate());
        } else if (digitizedCard != null) {
            byte[] byteArrayFullImg  = digitizedCard.getByteArrayFullImg();
            Bitmap bitmap = null;
            if (byteArrayFullImg != null) {
                bitmap = BitmapFactory.decodeByteArray(byteArrayFullImg, 0, byteArrayFullImg.length);
            }
            initCardView(digitizedCard.getNumber(), digitizedCard.isMultiBalance(),
                    digitizedCard.getProductName(), bitmap, digitizedCard.getExpireDate());
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(card!=null) {
            if (card.rbsNumber.equals(GeneralManager.getInstance().getRbsDefaultCard())) {
                cvDefaultIcon.setVisibility(View.VISIBLE);
            } else {
                cvDefaultIcon.setVisibility(View.GONE);
            }
            //tvAmount.setText((card.getFormattedBalance(getActivity())).replaceAll("-",""));
        } else if (digitizedCard != null) {
            if (digitizedCard.isDefault()) {
                cvDefaultIcon.setVisibility(View.VISIBLE);
                if (isContactlessHistory) {
                    cvDefaultIcon.setVisibility(View.GONE);
                }
            } else {
                cvDefaultIcon.setVisibility(View.GONE);
//                if (isContactlessHistory) {
//                    cvDefaultIcon.setVisibility(View.GONE);
//                    imgStar.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_default_card_star_inactive));
//                    cvDefaultIcon.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            showConfirmDialog();
//                        }
//                    });
//                }
            }
        }
        if (isPayment) {
            cvDefaultIcon.setVisibility(View.GONE);
        }
    }

    private void getBundle() {
        if(getArguments()!=null) {
            card = (UserAccounts.Cards) getArguments().getSerializable("userCard");
            String rbsNumber = getArguments().getString("rbsDigitizedCard");
            isContactlessHistory = getArguments().getBoolean("isContactlessHistory");
            isPayment = getArguments().getBoolean("is_payment");
            if (rbsNumber != null) {
                DigitizedCardController digitizedCardController = DigitizedCardController.getController();
                digitizedCard = digitizedCardController.getDigitizedCardByRbs(rbsNumber,
                        Utilities.getPreferences(getActivity()).getString(Constants.userPhone, ""));
                digitizedCardController.close();
            }
        }
    }

    private void initCardView(String number, boolean isMultiBalance, String productName, Bitmap bitmap, Date expireDate) {
        tvCardNumber.setText(number);
        tvCardType.setText(getCardDate(expireDate).toUpperCase());
        //setBrand();
        if(isMultiBalance) {
            cvMulti.setVisibility(View.VISIBLE);
        }
        //if (card.fullIm==null) {
        /*if (bitmap==null) {
            switch (productName.toLowerCase().replace(" ", "")) {
                case "elkart":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.elkart_large));
                    break;
                case "unionpay":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.unionpay_large));
                    break;
                case "unionpaydiamond":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.unionpay_diamond_large));
                    break;
                case "unionpaygold":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.unionpay_gold_large));
                    break;
                case "visabusiness":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_business_large));
                    break;
                case "visaclassic":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_classic_large));
                    break;
                case "visaclassicred":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_classic_red_large));
                    break;
                case "visaelectron":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_electron_large));
                    break;
                case "visagold":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_gold_large));
                    break;
                case "visagoldpaywave":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_gold_paywave_large));
                    break;
                case "visainfiniteprivatebankin":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_infinite_privatebankin_large));
                    break;
                case "visainstant":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_instant_large));
                    break;
                case "visaplatinum":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_platinum_large));
                    break;
                case "visaplatinumgrey":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_platinum_grey_large));
                    break;
                default:
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.elkart_large));
                    break;
                case "VisaClassic":
                    if (digitizedCard != null || isPayment) {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_classic_large_new));
                    } else {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_classic_large));
                    }
                    break;
                case "VisaGold":
                    if (digitizedCard != null || isPayment) {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_gold_large_new));
                    } else {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_gold_large));
                    }
                    break;
                case "VisaInfinite":
                    if (digitizedCard != null || isPayment) {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_infinite_large_new));
                    } else {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_infinite_large));
                    }
                    break;
                case "VisaPlatinum":
                    if (digitizedCard != null || isPayment) {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_platinum_large_new));
                    } else {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_platinum_large));
                    }
                    tvCardNumber.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    tvCardType.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    break;
                case "VisaElectron":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_electron_large));
                    tvCardNumber.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    tvCardType.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    break;
                case "VisaVirtuon":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_virtuon_large));
                    break;
                //�� ����� ��� ��������
                case "VirtuonVisa":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_virtuon_large));
                    break;
                case "VisaBusinessPlatinum":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_business_platinum_large));
                    break;
                case "VisaInstantElectron":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_instante_electron_large));
                    break;
                case "VisaBusiness":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_business_large));
                    tvCardNumber.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    tvCardType.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    break;
                case "VisaPremium":
                    if (digitizedCard != null || isPayment) {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_premium_black_new));
                    } else {
                        background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.visa_premium_black_large));
                    }
                    break;
                case "MastercardStandard":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.mastercard_standard_large));
                    break;
                case "MastercardBlackEdition":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.mastercard_be_large));
                    break;
                case "MastercardGold":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.mastercard_gold_large));
                    break;
                case "UnionPayClassic":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.union_pay_large));
                    break;
                case "MastercardMaestro":
                    background.setBackground(ContextCompat.getDrawable(parentActivity, R.drawable.mastercard_maestro_large));
                    tvCardNumber.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    tvCardType.setTextColor(ContextCompat.getColor(parentActivity, R.color.gray_atf));
                    break;*/
//            }
//        } else {
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);//card.fullIm
            background.setBackground(drawable);
//        }

//        if(card.brand.contains("Gold")) {
//            cardView.setCardBackgroundColor(getResources().getColor(R.color.card_gold));
//        } else if(card.brand.contains("Infinite") || card.brand.contains("MastercardBE")) {
//            cardView.setCardBackgroundColor(getResources().getColor(R.color.black));
//        } else if(card.brand.contains("Platinum")) {
//            cardView.setCardBackgroundColor(getResources().getColor(R.color.card_platinum));
//        } else if(card.brand.contains("UnionPay")) {
//            cardView.setCardBackgroundColor(getResources().getColor(R.color.card_union_pay));
//        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int roundPixelSize) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, (float) roundPixelSize, (float) roundPixelSize, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    private String getCardDate(Date expireDate) {
        String type = String.valueOf(expireDate);
        String date = "";

        if (type.contains("Jan")) {
            date += "01";
        } else {
            if (type.contains("Feb")) {
                date += "02";
            } else {
                if (type.contains("Mar")) {
                    date += "03";
                } else {
                    if (type.contains("Apr")) {
                        date += "04";
                    } else {
                        if (type.contains("May")) {
                            date += "05";
                        } else {
                            if (type.contains("Jun")) {
                                date += "06";
                            } else {
                                if (type.contains("Jul")) {
                                    date += "07";
                                } else {
                                    if (type.contains("Aug")) {
                                        date += "08";
                                    } else {
                                        if (type.contains("Sept")) {
                                            date += "09";
                                        } else {
                                            if (type.contains("Oct")) {
                                                date += "10";
                                            } else {
                                                if (type.contains("Nov")) {
                                                    date += "11";
                                                } else {
                                                    if (type.contains("Dec")) {
                                                        date += "12";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        char[] mass = type.toCharArray();
        date+="/" + mass[type.length()-2] + mass[type.length()-1];
        return date;
    }

    private String getCardType() {
        String type;
        if(card.brand.contains("Gold")){
            type = "Gold";
        } else if(card.brand.contains("Infinite")) {
            type = "Infinite";
        } else if(card.brand.contains("MastercardBE")) {
            type = "Black Edition";
        } else if(card.brand.contains("UnionPay")) {
            type = "UnionPay";
        } else if(card.brand.contains("Platinum")) {
            type = "Platinum";
        } else {
            type = "";
        }
        return type;
    }

    private void setBrand() {
        if(card.brand.contains("Mastercard")) {
            ivBrandCardLogo.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_mastercard_logo));
        } else if(card.brand.contains("UnionPay")) {
            ivBrandCardLogo.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_unionpay_logo));
        } else if(card.brand.contains("Maestro")) {
            ivBrandCardLogo.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_maestro_logo));
        }
    }
}
