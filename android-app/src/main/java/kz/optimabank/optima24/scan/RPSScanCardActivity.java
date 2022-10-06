/*
package kz.optimabank.optima24.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.view.WindowManager;

import cards.pay.paycardsrecognizer.sdk.Card;
import cards.pay.paycardsrecognizer.sdk.camera.RecognitionAvailabilityChecker;
import cards.pay.paycardsrecognizer.sdk.camera.RecognitionCoreUtils;
import cards.pay.paycardsrecognizer.sdk.camera.RecognitionUnavailableException;
import cards.pay.paycardsrecognizer.sdk.ui.ScanCardRequest;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.scan.fragments.RPSInitLibraryFragment;
import kz.optimabank.optima24.scan.fragments.RPSScanCardFragment;

*/
/**
  Created by Тимур on 02.02.2018.
 *//*


public class RPSScanCardActivity extends OptimaActivity implements RPSScanCardFragment.InteractionListener,
        RPSInitLibraryFragment.InteractionListener {
    private static final String TAG = "ScanCardActivity";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().onPostCreate(null);

        if (savedInstanceState == null) {

            RecognitionAvailabilityChecker.Result checkResult = RecognitionAvailabilityChecker.doCheck(this);
            if (checkResult.isFailed()
                    && !checkResult.isFailedOnCameraPermission()) {
                onScanCardFailed(new RecognitionUnavailableException(checkResult.getMessage()));
            } else {
                if (RecognitionCoreUtils.isRecognitionCoreDeployRequired(this)
                        || checkResult.isFailedOnCameraPermission()) {
                    showInitLibrary();
                } else {
                    showScanCard();
                }
            }
        }
    }

    private void showInitLibrary() {
        @SuppressLint("RestrictedApi")
        Fragment fragment = new RPSInitLibraryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, "CardDetailsActivity")
                .setCustomAnimations(0, 0)
                .commitNow();
    }

    private void showScanCard() {
        @SuppressLint("RestrictedApi")
        Fragment fragment = new RPSScanCardFragment();
        Bundle args = new Bundle(1);
        args.putParcelable(RPSScanCardIntent.KEY_SCAN_CARD_REQUEST, getScanRequest());
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, "CardDetailsActivity")
                .setCustomAnimations(0, 0)
                .commitNow();

        ViewCompat.requestApplyInsets(findViewById(android.R.id.content));
    }

    @Override
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void onScanCardFailed(Exception e) {
        Log.e(TAG, "Scan card failed", new RuntimeException("onScanCardFinishedWithError()", e));
        setResult(RPSScanCardIntent.RESULT_CODE_ERROR);
        finish();
    }

    @Override
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void onScanCardFinished(Card card, @Nullable byte[] cardImage) {
        Intent intent = new Intent();
        intent.putExtra(RPSScanCardIntent.RESULT_PAYCARDS_CARD, (Parcelable) card);
        if (cardImage != null) intent.putExtra(RPSScanCardIntent.RESULT_CARD_IMAGE, cardImage);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onInitLibraryFailed(Throwable e) {
        Log.e(TAG, "Init library failed", new RuntimeException("onInitLibraryFailed()", e));
        setResult(RPSScanCardIntent.RESULT_CODE_ERROR);
        finish();
    }

    @Override
    public void onScanCardCanceled(@RPSScanCardIntent.CancelReason int actionId) {
        Intent intent = new Intent();
        intent.putExtra(RPSScanCardIntent.RESULT_CANCEL_REASON, actionId);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void onInitLibraryComplete() {
        if (isFinishing()) return;
        showScanCard();
    }

    @SuppressLint("RestrictedApi")
    private ScanCardRequest getScanRequest() {
        ScanCardRequest request = getIntent().getParcelableExtra(RPSScanCardIntent.KEY_SCAN_CARD_REQUEST);
        if (request == null) {
            request = ScanCardRequest.getDefault();
        }
        return request;
    }
}
*/
