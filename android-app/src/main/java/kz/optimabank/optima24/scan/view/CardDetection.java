/*
package kz.optimabank.optima24.scan.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import kg.optima.mobile.R;

*/
/**
  Created by Тимур on 06.02.2018.
 *//*


public class CardDetection extends View {
    private Drawable mCardGradientDrawable;
    private volatile int mDetectionState;

    private static final float RECT_CORNER_PADDING_LEFT = 1;

    private static final float RECT_CORNER_PADDING_TOP = 1;

    private static final float RECT_CORNER_LINE_STROKE_WIDTH = 5f;

    private static final float RECT_CORNER_RADIUS = 8;

    private volatile String mRecognitionResultCardNumber;
    private volatile String mRecognitionResultDate;
    private volatile String mRecognitionResultHolder;

    private BitmapDrawable mCornerTopLeftDrawable, mCornerTopRightDrawable,
            mCornerBottomLeftDrawable, mCornerBottomRightDrawable;

    private BitmapDrawable mLineTopDrawable, mLineLeftDrawable,
            mLineRightDrawable, mLineBottomDrawable;

    private Paint mCardNumberPaint, mCardDatePaint, mCardHolderPaint;

    private Typeface mCardTypeface;
    private float mDisplayDensity;

    private float mCornerPaddingLeft;
    private float mCornerLineWidth;
    private float mCornerPaddingTop;
    private float mCornerRadius;

    private final Rect mCardRectInvalidation = new Rect();


    public CardDetection(Context context) {
        this(context, null);
    }

    public CardDetection(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardDetection(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private RPSCardRectCoordsMapper mCardFrame;
    private Paint mBackgroundPaint;

    @SuppressLint("PrivateResource")
    private void init(final Context context) {
        float density = getResources().getDisplayMetrics().density;
        mDisplayDensity = density;
        int mBackgroundDrawableColor = context.getResources().getColor(cards.pay.paycardsrecognizer.sdk.R.color.wocr_card_shadow_color);
        mCardGradientDrawable = context.getResources().getDrawable(cards.pay.paycardsrecognizer.sdk.R.drawable.wocr_frame_rect_gradient);
        mCardFrame = new RPSCardRectCoordsMapper();

        mCornerPaddingTop = density * RECT_CORNER_PADDING_TOP;
        mCornerPaddingLeft = density * RECT_CORNER_PADDING_LEFT;
        mCornerLineWidth = density * RECT_CORNER_LINE_STROKE_WIDTH;
        mCornerRadius = density * RECT_CORNER_RADIUS;

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(mBackgroundDrawableColor);

        initCornerDrawables(context);
        initLineDrawables(context);

        mCardTypeface = Fonts.getCardFont(context);
        mCardNumberPaint = createCardTextPaint();
        mCardDatePaint = createCardTextPaint();
        mCardHolderPaint = createCardTextPaint();

        if (isInEditMode()) {
            mDetectionState = RecognitionConstants.DETECTED_BORDER_TOP | RecognitionConstants.DETECTED_BORDER_BOTTOM
                    | RecognitionConstants.DETECTED_BORDER_LEFT | RecognitionConstants.DETECTED_BORDER_RIGHT;
            mRecognitionResultCardNumber = prettyPrintCardNumber("1234567890123456");
            mRecognitionResultDate = "05/18";
            mRecognitionResultHolder = "CARDHOLDER NAME";
        }
    }

    private Paint createCardTextPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setTypeface(mCardTypeface);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(6, 3.0f, 3.0f, Color.BLACK);
        paint.setTextSize(12 * mDisplayDensity);
        return paint;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (mCardGradientDrawable.getBounds().width() == 0) return;
        drawBackground(canvas);
        drawCorners(canvas);
//        drawRecognitionResult(canvas);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        if (DBG) Log.d("CameraActivity", "onSizeChanged w,h: " + w + "," + h);
        boolean changed = mCardFrame.setViewSize(w, h);
        if (changed) refreshCardRectCoords();
    }

    private void drawRecognitionResult(Canvas canvas) {
        final String resultDate = mRecognitionResultDate;
        final String resultNumber = mRecognitionResultCardNumber;
        final String resultHolder = mRecognitionResultHolder;

        if (!TextUtils.isEmpty(resultNumber)) {
            canvas.drawText(resultNumber,
                    mCardFrame.getCardNumberPos().x,
                    mCardFrame.getCardNumberPos().y,
                    mCardNumberPaint);
        }

        if (!TextUtils.isEmpty(resultDate)) {
            canvas.drawText(resultDate,
                    mCardFrame.getCardDatePos().x,
                    mCardFrame.getCardDatePos().y,
                    mCardDatePaint);
        }

        if (!TextUtils.isEmpty(resultHolder)) {
            canvas.drawText(resultHolder,
                    mCardFrame.getCardHolderPos().x,
                    mCardFrame.getCardHolderPos().y,
                    mCardHolderPaint);
        }
    }

    private void drawBackground(Canvas canvas) {
        Rect rect = mCardFrame.getCardRect();
        // top
        canvas.drawRect(0, 0, getWidth(), rect.top, mBackgroundPaint);
        // bottom
        canvas.drawRect(0, rect.bottom, getWidth(), getHeight(), mBackgroundPaint);
        // left
        canvas.drawRect(0, rect.top, rect.left, rect.bottom, mBackgroundPaint);
        // right
        canvas.drawRect(rect.right, rect.top, getWidth(), rect.bottom, mBackgroundPaint);
    }

    @SuppressLint("PrivateResource")
    private void initLineDrawables(Context context) {
        mLineTopDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.wocr_card_frame_rect_line_top);//cards.pay.paycardsrecognizer.sdk.
        Matrix m = new Matrix();
        Bitmap bitmap = mLineTopDrawable.getBitmap();

        m.setRotate(90);
        mLineRightDrawable = new BitmapDrawable(context.getResources(),
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true));

        m.setRotate(180);
        mLineBottomDrawable = new BitmapDrawable(context.getResources(),
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true));

        m.setRotate(270);
        mLineLeftDrawable = new BitmapDrawable(context.getResources(),
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true));
    }

    private void drawCorners(Canvas canvas) {
        final int detectionState = mDetectionState;
        mCardGradientDrawable.draw(canvas);
        mCornerTopLeftDrawable.draw(canvas);
        mCornerTopRightDrawable.draw(canvas);
        mCornerBottomLeftDrawable.draw(canvas);
        mCornerBottomRightDrawable.draw(canvas);

        mLineTopDrawable.draw(canvas);
        mLineLeftDrawable.draw(canvas);
        mLineRightDrawable.draw(canvas);
        mLineBottomDrawable.draw(canvas);
        // Detected edges
//        if (0 != (detectionState & RecognitionConstants.DETECTED_BORDER_TOP)) {
//            mLineTopDrawable.draw(canvas);
//        }
//        if (0 != (detectionState & RecognitionConstants.DETECTED_BORDER_LEFT)) {
//            mLineLeftDrawable.draw(canvas);
//        }
//        if (0 != (detectionState & RecognitionConstants.DETECTED_BORDER_RIGHT)) {
//            mLineRightDrawable.draw(canvas);
//        }
//        if (0 != (detectionState & RecognitionConstants.DETECTED_BORDER_BOTTOM)) {
//            mLineBottomDrawable.draw(canvas);
//        }
    }

    @SuppressLint("PrivateResource")
    private void initCornerDrawables(Context context) {
        mCornerTopLeftDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.wocr_card_frame_rect_corner_top_left);//cards.pay.paycardsrecognizer.sdk.

        Matrix m = new Matrix();

        Bitmap bitmap = mCornerTopLeftDrawable.getBitmap();

        m.setRotate(90);
        mCornerTopRightDrawable = new BitmapDrawable(context.getResources(),
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true));

        m.setRotate(180);
        mCornerBottomRightDrawable = new BitmapDrawable(context.getResources(),
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true));

        m.setRotate(270);
        mCornerBottomLeftDrawable = new BitmapDrawable(context.getResources(),
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true));

    }

    public String prettyPrintCardNumber(@Nullable CharSequence cardNumber) {
        if (TextUtils.isEmpty(cardNumber)) {
            return "";
        }
        final StringBuilder stringBuilder = new StringBuilder(20);
        for (int i = 0, size = cardNumber.length(); i < size; ++i) {
            if (size == 16) {
                if (i != 0 && i % 4 == 0) {
                    stringBuilder.append('\u00a0');
                }
            } else if (size == 15) {
                if (i == 4 || i == 10) {
                    stringBuilder.append('\u00a0');
                }
            }
            stringBuilder.append(cardNumber.charAt(i));
        }
        return stringBuilder.toString();
    }

    private void refreshCardRectCoords() {
        refreshCardRectInvalidation();
        refreshDrawableBounds();
        refreshTextSize();
    }

    private void refreshTextSize() {
        mCardNumberPaint.setTextSize(mCardFrame.getCardNumberFontSize());
        mCardDatePaint.setTextSize(mCardFrame.getCardDateFontSize());
        mCardHolderPaint.setTextSize(mCardFrame.getCardHolderFontSize());
    }

    private void refreshCardRectInvalidation() {
        Rect cardRect = mCardFrame.getCardRect();
        int border = (int)(0.5f + mCornerPaddingLeft) + (int)(0.5f + mCornerLineWidth / 2f);
        mCardRectInvalidation.left = cardRect.left - border;
        mCardRectInvalidation.top = cardRect.top - border;
        mCardRectInvalidation.right = cardRect.right + border;
        mCardRectInvalidation.bottom = cardRect.bottom + border;
    }

    private void refreshDrawableBounds() {
        Rect cardRect = mCardFrame.getCardRect();
        mCardGradientDrawable.setBounds(cardRect);

        int rectWidth = mCornerTopLeftDrawable.getIntrinsicWidth();
        int rectHeight = mCornerTopLeftDrawable.getIntrinsicHeight();
        int cornerStroke = (int)(0.5f + mCornerLineWidth / 2f);

        int left1 = Math.round(cardRect.left - mCornerPaddingLeft - cornerStroke);
        int left2 = Math.round(cardRect.right - rectWidth + mCornerPaddingLeft + cornerStroke);
        int top1 = Math.round(cardRect.top - mCornerPaddingTop - cornerStroke);
        int top2 = Math.round(cardRect.bottom - rectHeight + mCornerPaddingTop + cornerStroke);

        // Corners
        mCornerTopLeftDrawable.setBounds(left1, top1, left1 + rectWidth, top1 + rectHeight);
        mCornerTopRightDrawable.setBounds(left2, top1, left2 + rectWidth, top1 + rectWidth);
        mCornerBottomLeftDrawable.setBounds(left1, top2, left1 + rectWidth, top2 + rectHeight);
        mCornerBottomRightDrawable.setBounds(left2, top2, left2 + rectWidth, top2 + rectHeight);

        // Lines
        int offset = (int)mCornerRadius;
        mLineTopDrawable.setBounds(
                left1 + offset,
                top1,
                left2 + rectWidth - offset,
                top1 + mLineTopDrawable.getIntrinsicHeight());
        mLineLeftDrawable.setBounds(left1, top1 + offset,
                left1 + mLineLeftDrawable.getIntrinsicWidth(), top2 + rectHeight - offset);
        mLineRightDrawable.setBounds(
                left2 + rectWidth - mLineRightDrawable.getIntrinsicWidth(),
                top1 + offset,
                left2 + rectWidth,
                top2 + rectHeight - offset
        );
        mLineBottomDrawable.setBounds(
                left1 + offset,
                top2 + rectHeight - mLineBottomDrawable.getIntrinsicHeight(),
                left2 + rectWidth - offset,
                top2 + rectHeight
        );
    }
}
*/
