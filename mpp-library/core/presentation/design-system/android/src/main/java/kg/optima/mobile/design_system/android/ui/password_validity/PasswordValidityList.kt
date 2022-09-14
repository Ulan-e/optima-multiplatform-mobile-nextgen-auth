package kg.optima.mobile.design_system.android.ui.password_validity

import androidx.compose.runtime.Composable
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel

@Composable
fun PasswordValidityList(list: List<PasswordValidityModel>) {
	list.forEach {
		PasswordValidityItem(it)
	}
}