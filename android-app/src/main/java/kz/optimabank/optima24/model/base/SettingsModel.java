package kz.optimabank.optima24.model.base;

/**
  Created by Timur on 05.07.2017.
 */

public class SettingsModel {
    private int code;
    private String name;

    public SettingsModel(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
