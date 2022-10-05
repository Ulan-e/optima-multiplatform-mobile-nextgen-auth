package kz.optimabank.optima24.model.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SecretQuestionResponse implements Serializable {
    @SerializedName("Question")
    public String question;
    @SerializedName("QuestionId")
    public String questionId;

    @Override
    public String toString() {
        return question;
    }
}
