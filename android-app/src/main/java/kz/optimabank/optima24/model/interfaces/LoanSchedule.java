package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.LoanScheduleImpl;

/**
  Created by Timur on 27.06.2017.
 */

public interface LoanSchedule {
    void getLoanSchedule(Context context, int code);
    void registerCallBack(LoanScheduleImpl.Callback callback);
}
