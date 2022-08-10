package kg.optima.mobile.design_system.android.ui.image

data class MediaSizeType internal constructor(
	val type: String
) {
	companion object {
		val AVATAR = MediaSizeType("avatar")              //Original
		val AVATAR_XL = MediaSizeType("avatar-XL")        //1080x1080
		val AVATAR_L = MediaSizeType("avatar-L")          //540x540
		val AVATAR_M = MediaSizeType("avatar-M")          //270x270
		val AVATAR_S = MediaSizeType("avatar-S")          //135x135
		val AVATAR_XS = MediaSizeType("avatar-XS")        //68x68

		val ESTATE = MediaSizeType("estate")              //Original
		val ESTATE_XL = MediaSizeType("estate-XL")        //1920x1080
		val ESTATE_M = MediaSizeType("estate-M")          //480x270
		val ESTATE_XS = MediaSizeType("estate-XS")        //120x68

		val PANORAMA = MediaSizeType("panorama")          //Original
		val PANORAMA_XL = MediaSizeType("panorama-XL")    //1080px - height
		val PANORAMA_M = MediaSizeType("panorama-M")      //270px
		val PANORAMA_XS = MediaSizeType("panorama-XS")    //68px

		fun createSource(type: String) = MediaSizeType(type = type)
	}
}