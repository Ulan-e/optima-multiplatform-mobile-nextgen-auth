package kz.optimabank.optima24.model.base;

import java.io.Serializable;
import kz.optimabank.optima24.utility.Constants;

/**
  Created by Timur on 08.04.2017.
 */

public class TransferModel implements Serializable {
    public int code = Constants.ITEM_ID;
    public String name;

    public TransferModel(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
