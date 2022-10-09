package kz.optimabank.optima24.model.gson;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

/**
  Created by Timur on 23.02.2017.
 */

public class APIError {
    @SerializedName("Message")
    public String message;
    @SerializedName("Error")
    public Error error;

    public String message() {
        return message;
    }

    public class Error {
        @SerializedName("Code")
        public Integer code;
    }
}
