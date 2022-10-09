package kg.optima.mobile.android.ui.features.registration.create_password

import java.io.Serializable

class CreatePasswordModel(
	val hash: String,
	val questionId: String,
	val answer: String,
) : Serializable