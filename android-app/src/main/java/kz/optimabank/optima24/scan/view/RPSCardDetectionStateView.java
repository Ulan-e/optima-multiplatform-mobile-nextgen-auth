package kz.optimabank.optima24.scan.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;

import cards.pay.paycardsrecognizer.sdk.camera.widget.CardDetectionStateView;
import cards.pay.paycardsrecognizer.sdk.ndk.RecognitionResult;

/**
  Created by Тимур on 04.02.2018.
 */

public class RPSCardDetectionStateView extends CardDetectionStateView {
    private final Context context;
    private boolean isVibrated = false;

    public RPSCardDetectionStateView(Context context) {
        this(context, null);
        Log.d("TAG","RPSCardDetectionStateView 1");
    }

    public RPSCardDetectionStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d("TAG","RPSCardDetectionStateView 2");
    }

    public RPSCardDetectionStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Log.d("TAG","RPSCardDetectionStateView 3");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public synchronized void setRecognitionResult(RecognitionResult result) {
        super.setRecognitionResult(result);

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator!=null && !isVibrated) {
            vibrator.vibrate(500);
            isVibrated = true;
        }
    }
}
