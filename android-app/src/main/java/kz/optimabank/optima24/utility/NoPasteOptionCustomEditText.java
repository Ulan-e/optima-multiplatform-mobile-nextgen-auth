package kz.optimabank.optima24.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

public class NoPasteOptionCustomEditText extends AppCompatEditText {

    private static final String EDITTEXT_ATTRIBUTE_COPY_AND_PASTE = "isCopyPasteDisabled";
    private static final String PACKAGE_NAME = "kz.optimabank.optima24.utility;";
    private static final String CAN_CHANGE = "canPaste";

    public NoPasteOptionCustomEditText(
            @NonNull Context context
    ) {
        super(context);
    }

    public NoPasteOptionCustomEditText(
            @NonNull Context context,
            @Nullable AttributeSet attrs
    ) {
        super(context, attrs);
        EnableDisableCopyAndPaste(context, attrs);
    }

    public NoPasteOptionCustomEditText(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void EnableDisableCopyAndPaste(
            Context context,
            AttributeSet attributeSet
    ) {
        boolean isDisableCopyAndPaste =
                attributeSet.getAttributeBooleanValue(PACKAGE_NAME,
                        EDITTEXT_ATTRIBUTE_COPY_AND_PASTE, false);
        if (isDisableCopyAndPaste && !isInEditMode()) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            this.setLongClickable(false);
            this.setOnTouchListener(new BlockContextMenuTouchListener
                    (inputMethodManager));
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        CharSequence text = getText();
        if (text != null) {
            if (selStart != text.length() || selEnd != text.length()) {
                setSelection(text.length(), text.length());
                return;
            }
        }
        super.onSelectionChanged(selStart, selEnd);
    }

    @Override
    public boolean isSuggestionsEnabled() {
        return false;
    }

    @Override
    public int getSelectionStart() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getMethodName().equals(CAN_CHANGE)) {
                return -1;
            }
        }
        return super.getSelectionStart();
    }

    @Override
    public boolean isLongClickable() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getAutofillType() {
        return AUTOFILL_TYPE_NONE;
    }

    private void performHandlerAction(
            final InputMethodManager inputMethodManager
    ) {
        int postDelayedIntervalTime = 25;
        new Handler().postDelayed(() -> {
            NoPasteOptionCustomEditText.this.setSelected(true);
            NoPasteOptionCustomEditText.this.requestFocusFromTouch();
            inputMethodManager.showSoftInput(NoPasteOptionCustomEditText.this,
                    InputMethodManager.RESULT_SHOWN);
        }, postDelayedIntervalTime);
    }

    private class BlockContextMenuTouchListener implements View.OnTouchListener {
        private static final int TIME_INTERVAL_BETWEEN_DOUBLE_TAP = 30;
        private final InputMethodManager inputMethodManager;
        private long lastTapTime = 0;

        BlockContextMenuTouchListener(InputMethodManager inputMethodManager) {
            this.inputMethodManager = inputMethodManager;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                long currentTapTime = System.currentTimeMillis();
                if (lastTapTime != 0 && (currentTapTime - lastTapTime)
                        < TIME_INTERVAL_BETWEEN_DOUBLE_TAP) {
                    NoPasteOptionCustomEditText.this.setSelected(false);
                    performHandlerAction(inputMethodManager);
                    return true;
                } else {
                    if (lastTapTime == 0) {
                        lastTapTime = currentTapTime;
                    } else {
                        lastTapTime = 0;
                    }
                    performHandlerAction(inputMethodManager);
                    return true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                NoPasteOptionCustomEditText.this.setSelected(false);
                performHandlerAction(inputMethodManager);
            }
            return false;
        }
    }
}
