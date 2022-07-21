package kg.optima.mobile.resources

import dev.icerock.moko.resources.FontResource
import kg.optima.mobile.MR

public object Typography {
	public object Fonts {
		public object TTNormsPro {
			public val regular: FontResource get() = MR.fonts.TTNormsPro.regular
			public val medium: FontResource get() = MR.fonts.TTNormsPro.medium
			public val bold: FontResource get() = MR.fonts.TTNormsPro.bold
		}
	}
}