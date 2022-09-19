package kg.optima.mobile.registration.domain.model.utils

import kg.optima.mobile.registration.data.model.otp_tries.OtpTriesModel
import kg.optima.mobile.registration.domain.model.OtpTriesEntity

fun OtpTriesModel.toOtpTriesEntity() =
    OtpTriesEntity(
        phoneNumber = this.phoneNumber,
        tryCount = this.tryCount,
        tryTime =  this.tryTime
    )