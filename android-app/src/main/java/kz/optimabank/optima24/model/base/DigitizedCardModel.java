package kz.optimabank.optima24.model.base;

import android.graphics.drawable.Drawable;

public class DigitizedCardModel {
    private final String number;
    private final String brand;
    private final String rbsNumber;
    private final Drawable background;
    private final boolean isEmpty;

    public DigitizedCardModel(String number, String brand, String rbsNumber, Drawable background, boolean isEmpty) {
        this.number = number;
        this.brand = brand;
        this.background = background;
        this.isEmpty = isEmpty;
        this.rbsNumber = rbsNumber;
    }

    public String getNumber() {
        return number;
    }

    public String getBrand() {
        return brand;
    }

    public Drawable getBackground() {
        return background;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public String getRbsNumber() {
        return rbsNumber;
    }
}
