package kz.optimabank.optima24.utility;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CustomEditText extends AppCompatEditText {
    SumTextWatcher textWatcher = new SumTextWatcher(this);

    public CustomEditText(@NonNull Context context) {
        super(context);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            this.addTextChangedListener(textWatcher);
        }
    }

    private static class SumTextWatcher implements TextWatcher {

        private EditText editText;
        private int previousCleanString;

        SumTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        previousCleanString = start;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            boolean isReverce = false;
            StringBuffer stringBuffer = new StringBuffer();
            String string = editText.getText().toString().replaceAll(" ", "");
            string = Utilities.dotAndComma(string);
            if (editText.getText().length() > 3) {
                int pointPosition = string.indexOf(".");
                if (pointPosition == 0 || pointPosition == -1) {
                    pointPosition = string.indexOf(",");
                }

                if (pointPosition == -1) {
                    pointPosition = string.length();
                    if (editText.getText().length() == 4) {
                        isReverce = true;
                        stringBuffer.delete(0, stringBuffer.length());
                        stringBuffer.append(string);
                        stringBuffer.reverse();
                        string = String.valueOf(stringBuffer);
                        string = string.substring(0, pointPosition - 1) + " " + string.substring(pointPosition - 1);
                    } else {
                        isReverce = false;
                        string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                    }
                } else {
                    if (pointPosition > 3)
                        string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                }
                for (int i = 0; i < pointPosition / 3; i++) {
                    int lastSpacePosition = string.indexOf(" ");
                    if ((lastSpacePosition - 3) > 0) {
                        string = string.substring(0, lastSpacePosition - 3) + " " + string.substring(lastSpacePosition - 3);
                    } else {
                        break;
                    }
                }
            }

            if (string.length() != 0) {
                if (isReverce) {
                    stringBuffer.delete(0, stringBuffer.length());
                    stringBuffer.append(string);
                    stringBuffer.reverse();
                    string = String.valueOf(stringBuffer);

                }
                int selection = editText.getSelectionStart();
                if ((editText.getText().toString().length() + 2) == string.length()) {
                    selection = selection + 2;
                } else if ((editText.getText().toString().length() + 1) == string.length()) {
                    char a = string.charAt(previousCleanString);
                    if(a == ' '){
                        selection = selection - 1;
                    }
                    selection = selection + 1;
                } else if ((editText.getText().toString().length() - 1) == string.length()) {
                    selection = selection - 1;
                } else if ((editText.getText().toString().length() - 2) == string.length()) {
                    selection = selection - 2;
                } else {
                    selection = editText.getSelectionStart();
                }

                if(string.contains(".")){
                    int pointPosition = string.indexOf(".");
                    StringBuilder myName = new StringBuilder(string);
                    myName.setCharAt(pointPosition, ',');
                    string = myName.toString();
                }

                editText.removeTextChangedListener(this);
                if(string.length() == 4 && string.contains(" ")){
                    int pointPosition = string.indexOf(" ");
                    StringBuffer format = new StringBuffer(string);
                    format.deleteCharAt(pointPosition);
                    string = format.toString();
                    selection = selection - 1;
                }
                editText.setText(string);
                if(selection < 0){
                    editText.setSelection(0);
                }else{
                    editText.setSelection(selection);
                }
                editText.addTextChangedListener(this);
            }
        }
    }
}
