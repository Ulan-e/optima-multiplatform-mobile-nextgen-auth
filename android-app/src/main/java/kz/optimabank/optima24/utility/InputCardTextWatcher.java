package kz.optimabank.optima24.utility;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

public class InputCardTextWatcher implements TextWatcher {

    private static final char SPACE = ' ';
    private static final String ElCART_PREFIX = "9";
    private final OnErrorInputListener onErrorInputListener;

    public InputCardTextWatcher(OnErrorInputListener onErrorInputListener) {
        this.onErrorInputListener = onErrorInputListener;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        onErrorInputListener.onEnableInput(s.toString().startsWith(ElCART_PREFIX));
        onErrorInputListener.onEndInputCard(s.length() == 19);

        if (s.length() > 0 && (s.length() % 5) == 0) {
            final char c = s.charAt(s.length() - 1);
            if (SPACE == c) {
                s.delete(s.length() - 1, s.length());
            }
        }
        if (s.length() > 0 && (s.length() % 5) == 0) {
            char c = s.charAt(s.length() - 1);

            if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(SPACE)).length <= 3) {
                s.insert(s.length() - 1, String.valueOf(SPACE));
            }
        }
    }

    public interface OnErrorInputListener {
        void onEnableInput(Boolean isEnabled);

        void onEndInputCard(Boolean isEnd);
    }
}