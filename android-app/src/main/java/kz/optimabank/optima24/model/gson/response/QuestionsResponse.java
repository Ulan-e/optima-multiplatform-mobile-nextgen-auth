package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import kz.optimabank.optima24.model.base.SecretQuestionResponse;

public class QuestionsResponse {

    @SerializedName("data")
    @Expose
    public List<SecretQuestionResponse> data = null;
    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("code")
    @Expose
    public Integer code;
}
