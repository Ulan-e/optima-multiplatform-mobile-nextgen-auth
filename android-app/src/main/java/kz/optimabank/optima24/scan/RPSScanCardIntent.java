package kz.optimabank.optima24.scan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cards.pay.paycardsrecognizer.sdk.ui.ScanCardRequest;

/**
  Created by Тимур on 02.02.2018.
 */

public class RPSScanCardIntent {
    public static final int RESULT_CODE_ERROR = Activity.RESULT_FIRST_USER;

    public static final String RESULT_PAYCARDS_CARD = "RESULT_PAYCARDS_CARD";
    public static final String RESULT_CARD_IMAGE = "RESULT_CARD_IMAGE";
    public static final String RESULT_CANCEL_REASON = "RESULT_CANCEL_REASON";

    public static final int BACK_PRESSED = 1;
    public static final int ADD_MANUALLY_PRESSED = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {BACK_PRESSED, ADD_MANUALLY_PRESSED})
    public @interface CancelReason {}

    @RestrictTo(RestrictTo.Scope.LIBRARY)

    public static final String KEY_SCAN_CARD_REQUEST = "cards.pay.paycardsrecognizer.sdk.ui.ScanCardActivity.SCAN_CARD_REQUEST";

    private RPSScanCardIntent() {
    }

    public final static class Builder {

        private final Context mContext;

        private boolean mEnableSound = true;

        private boolean mScanExpirationDate = true;

        private boolean mScanCardHolder = true;

        private boolean mGrabCardImage = false;


        public Builder(Context context) {
            mContext = context;
        }

        /**
         * Scan expiration date. Default: <b>true</b>
         */
        public RPSScanCardIntent.Builder setScanExpirationDate(boolean scanExpirationDate) {
            mScanExpirationDate = scanExpirationDate;
            return this;
        }

        /**
         * Scan expiration date. Default: <b>true</b>
         */
        public RPSScanCardIntent.Builder setScanCardHolder(boolean scanCardHolder) {
            mScanCardHolder = scanCardHolder;
            return this;
        }


        /**
         * Enables or disables sounds in the library.<Br>
         * Default: <b>enabled</b>
         */
        public RPSScanCardIntent.Builder setSoundEnabled(boolean enableSound) {
            mEnableSound = enableSound;
            return this;
        }


        /**
         * Defines if the card image will be captured.
         * @param enable Defines if the card image will be captured. Default: <b>false</b>
         */
        public RPSScanCardIntent.Builder setSaveCard(boolean enable) {
            mGrabCardImage = enable;
            return this;
        }

        public Intent build() {
            Intent intent = new Intent(mContext, RPSScanCardActivity.class);
            @SuppressLint("RestrictedApi")
            ScanCardRequest request = new ScanCardRequest(mEnableSound, mScanExpirationDate,
                    mScanCardHolder, mGrabCardImage);
            intent.putExtra(KEY_SCAN_CARD_REQUEST, request);
            return intent;
        }
    }
}
