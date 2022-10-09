package kz.optimabank.optima24.utility;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import kz.optimabank.optima24.fragment.payment.PaymentFragment;
import kz.optimabank.optima24.model.manager.GeneralManager;

/**
 * Класс для маскирования поля
 */
public class MaskTextWatcher implements TextWatcher {
    private static final String TAG  = MaskTextWatcher.class.getSimpleName();
    private static final String DIGIT = "0";
    private static final String LETTER = "A";

    private char[] maskChars;
    private EditText editText;
    private CharSequence formattedText = "";
    private ValueListener valueListener;
    //private boolean isAdd = true;
    private boolean removing, isTypeMobile;
    int indexAddedSimvol;

    /**
     * @param mask входящая маска, маску преобразовываем в единый формат(для цифр - 0, для букв - А). Пример : 12(3QW4) --> 00(0AA0)
     * @param editText отслеживаемое поле
     */
    public MaskTextWatcher(String mask, boolean isTypeMobile, EditText editText, ValueListener valueListener) {
        Log.d(TAG, "original mask is:" + mask);
        StringBuilder builder = new StringBuilder();
        if (mask.length()>0 && mask.replaceAll("X","").replaceAll("x","").length() == 0){
            mask = mask.replaceAll("X","0").replaceAll("x","0");
        }

        for (char ch : mask.toCharArray()) {
            if (Character.isDigit(ch)) {
                builder.append(DIGIT);
            } else if (Character.isLetter(ch)) {
                builder.append(LETTER);
            } else {
                builder.append(ch);
            }
        }

        maskChars = builder.toString().toCharArray();
        this.editText = editText;
        this.valueListener = valueListener;
        this.isTypeMobile = isTypeMobile;
        Log.i(TAG, "maskChars =" + builder.toString());
        Log.i(TAG, "maskChars.length( =" + maskChars.length);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        Log.i(TAG, "raw =" + editable.toString());
        if(isTypeMobile){
            if(editable.length() == maskChars.length || editable.length() == maskChars.length+2){
                InputFilter[] FilterArray = new InputFilter[1];
                Log.i("afterTextChanged"," + maskChars = " + maskChars.length);
                FilterArray[0] = new InputFilter.LengthFilter(maskChars.length);
                editText.setFilters(FilterArray);
            } else {
                InputFilter[] FilterArray = new InputFilter[1];
                Log.i("afterTextChanged"," else+ maskChars = " + maskChars.length+2);
                FilterArray[0] = new InputFilter.LengthFilter(maskChars.length+2);
                editText.setFilters(FilterArray);
            }
            if(editable.toString().startsWith("0")){
                editable.delete(0, 1);
            }else if(editable.toString().startsWith(" 0")){
                editable.delete(0, 2);
            }else if(editable.toString().startsWith("996")){
                editable.delete(0, 3);
            }else if(editable.toString().startsWith("+996") || editable.toString().startsWith(" 996")){
                editable.delete(0, 4);
            }else if(editable.toString().startsWith(" +996")){
                editable.delete(0, 5);
            }
        }

        formattedText = formatMask(editable);
        Log.i(TAG, "formattedText = " + formattedText);
        editText.removeTextChangedListener(this);
        editable.clear();
        editable.append(formattedText);
        editText.addTextChangedListener(this);
        valueListener.onTextChanged(maskChars.length == editable.length());
    }

    /**
     * Форматирование входящей строки по маске
     * @param input входящая строка
     * @return отформатированная строка
     */
    private CharSequence formatMask(CharSequence input) {
        if (input.length() == 0) {
            return input;
        }
        Log.i(TAG, "formattedText = " + formattedText.length());
        Log.i(TAG, "input = " + input.length());
        if(GeneralManager.isTemplateClicked()){
            removing = false;
            GeneralManager.setTemplateClicked(false);
        }
        else
            removing = formattedText.length() > input.length();
        StringBuilder builder = new StringBuilder();
        char[] inputChars = input.toString().toCharArray(); // maskChars =(000) 00-00-00
        Log.i(TAG, "inputChars length = " + inputChars.length);
        int maskIndex = 0;
        int inputIndex = 0;
        while (inputIndex < inputChars.length) { //lengt=9
            char maskChar;
            if (maskIndex < maskChars.length) { //lengt=14
                maskChar = maskChars[maskIndex];
            } else {
                break;
            }
            if (maskChar != 'A' && maskChar != '0') {
                builder.append(maskChar);
                indexAddedSimvol = builder.length()-1;
                Log.i(TAG, "symbol = " + maskChar);
                if (maskIndex < maskChars.length - 1) { //lengt=13
                    maskIndex++;
                    maskChar = maskChars[maskIndex];
                    if (maskChar != 'A' && maskChar != '0' && !removing) {
                        builder.append(maskChar);
                        indexAddedSimvol = builder.length()-1;
                        if (maskIndex < maskChars.length - 1) {
                            maskIndex++;
                            maskChar = maskChars[maskIndex];
                        }
                    }
                }
            }
            if (Character.isDigit(maskChar) && Character.isDigit(inputChars[inputIndex]) || Character.isLetter(maskChar) && Character.isLetter(inputChars[inputIndex])) {
                builder.append(inputChars[inputIndex]);
                if (maskIndex < maskChars.length - 1) {
                    maskIndex++;
                    maskChar = maskChars[maskIndex];
                    if (maskChar != 'A' && maskChar != '0' && !removing) {
                        builder.append(maskChar);
                        indexAddedSimvol = builder.length()-1;
                        maskIndex++;
                    }
                }
                Log.i(TAG, "char = " + maskChar);
            }
            inputIndex++;
        }

        return builder;
    }

    public interface ValueListener {
        void onTextChanged(boolean maskFilled);
    }
}
