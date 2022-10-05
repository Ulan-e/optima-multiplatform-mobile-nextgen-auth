package kz.optimabank.optima24.model.gson.response;

import androidx.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("code")
    public int code;
    @Nullable
    @SerializedName("data")
    public T data;
    @SerializedName("success")
    public boolean isSuccess;
    @SerializedName("message")
    public String message;
}
