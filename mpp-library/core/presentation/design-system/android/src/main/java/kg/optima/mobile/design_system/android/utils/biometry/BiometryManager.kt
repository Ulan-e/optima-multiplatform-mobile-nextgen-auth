package kg.optima.mobile.design_system.android.utils.biometry

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

// TODO localize
object BiometryManager {
	private val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
		.setTitle("Вход по отпечатку пальца")
		.setSubtitle("Приложите палец, чтобы войти в приложение")
		.setNegativeButtonText("Использовать код доступа")
		.build()

	fun authorize(activity: FragmentActivity, doOnSuccess: () -> Unit, doOnFailure: () -> Unit) {
		biometricPrompt(activity, doOnSuccess, doOnFailure).authenticate(promptInfo)
	}

	private fun executor(activity: FragmentActivity): Executor =
		ContextCompat.getMainExecutor(activity)

	private fun biometricPrompt(
		activity: FragmentActivity,
		doOnSuccess: () -> Unit,
		doOnFailure: () -> Unit
	): BiometricPrompt {
		return BiometricPrompt(activity, executor(activity), object  : BiometricPrompt.AuthenticationCallback() {
			override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
				super.onAuthenticationSucceeded(result)
				doOnSuccess()
			}

			override fun onAuthenticationFailed() {
				super.onAuthenticationFailed()
				doOnFailure()
			}
		})
	}
}