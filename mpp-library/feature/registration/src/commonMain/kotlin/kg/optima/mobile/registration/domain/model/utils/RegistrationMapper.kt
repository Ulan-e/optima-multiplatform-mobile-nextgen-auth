package kg.optima.mobile.registration.domain.model.utils

import kg.optima.mobile.registration.data.model.otp_tries.OtpTriesModelList
import kg.optima.mobile.registration.domain.model.OtpTriesEntity

fun OtpTriesModelList.toOtpTriesEntity() : List<OtpTriesEntity> {
	val result = mutableListOf<OtpTriesEntity>()
	this.list.map {
		result.add(OtpTriesEntity(
			phoneNumber = it.phoneNumber,
			tryCount =it.tryCount,
			tryTime= it.tryTime,
		))
	}
	return result
}
