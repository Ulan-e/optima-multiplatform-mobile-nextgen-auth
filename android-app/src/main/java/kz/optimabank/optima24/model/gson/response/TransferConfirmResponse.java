package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
  Created by Timur on 21.05.2017.
 */

public class TransferConfirmResponse implements Serializable {
    @SerializedName("docId")
    public int documentId;
}
