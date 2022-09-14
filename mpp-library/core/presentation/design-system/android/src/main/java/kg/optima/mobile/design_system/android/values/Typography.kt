package kg.optima.mobile.design_system.android.values

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Headings.Companion.pix
import kg.optima.mobile.resources.Typography

val LocalTypography = staticCompositionLocalOf { ApplicationTypographyData }

private val TT_Norms_Pro_FontFamily = FontFamily(
	listOf(
		Font(resId = Typography.Fonts.TTNormsPro.regular.resId(), weight = FontWeight.Normal),
		Font(resId = Typography.Fonts.TTNormsPro.medium.resId(), weight = FontWeight.Medium),
		Font(resId = Typography.Fonts.TTNormsPro.bold.resId(), weight = FontWeight.Bold)
	)
)

// TODO refactor
val ApplicationTypographyData = TypographyData(
	typography = androidx.compose.material.Typography(
		defaultFontFamily = TT_Norms_Pro_FontFamily,
		h1 = TextStyle(
			fontSize = Headings.H1.sp,
			letterSpacing = 0.sp,
		),
		h2 = TextStyle(
			fontSize = Headings.H2.sp,
			letterSpacing = 0.sp,
		),
		h3 = TextStyle(
			fontSize = Headings.H3.sp,
			letterSpacing = 0.sp,
		),
		h4 = TextStyle(
			fontSize = Headings.H4.sp,
			letterSpacing = 0.sp,
		),
		h5 = TextStyle(
			fontSize = Headings.H5.sp,
			letterSpacing = 0.sp,
		),
		h6 = TextStyle(
			fontSize = Headings.H6.sp,
			letterSpacing = 0.sp,
		),
		subtitle1 = TextStyle(
			fontWeight = FontWeight.W500,
			fontSize = 16.sp,
			lineHeight = 24.sp,
			letterSpacing = 0.sp
		),
		subtitle2 = TextStyle(
			fontWeight = FontWeight.W400,
			fontSize = 14.sp,
			lineHeight = 20.sp,
			letterSpacing = 0.sp
		),
		body1 = TextStyle(
			fontWeight = FontWeight.W400,
			fontSize = 16.sp,
			lineHeight = 24.sp,
			letterSpacing = 0.sp
		),
		body2 = TextStyle(
			fontWeight = FontWeight.W400,
			fontSize = 14.sp,
			lineHeight = 20.sp,
			letterSpacing = 0.sp
		),
		button = TextStyle(
			fontWeight = FontWeight.W500,
			fontSize = 16.sp,
			lineHeight = 20.sp,
			letterSpacing = 0.sp
		),
		caption = TextStyle(
			fontWeight = FontWeight.W400,
			fontSize = 12.sp,
			lineHeight = 16.sp,
			letterSpacing = 0.sp
		),
	),
)

