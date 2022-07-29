package kg.optima.mobile.resources

import dev.icerock.moko.resources.FontResource
import kg.optima.mobile.MR

object Typography {
	object Fonts {
		object TTNormsPro {
			val regular: FontResource get() = MR.fonts.TTNormsPro.regular
			val medium: FontResource get() = MR.fonts.TTNormsPro.medium
			val bold: FontResource get() = MR.fonts.TTNormsPro.bold
		}
	}
}