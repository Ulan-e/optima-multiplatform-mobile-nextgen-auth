package kg.optima.mobile.resources.images

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.getImageByFileName
import kg.optima.mobile.MR

interface Images {
	/**
	 * Use [name] without resource format and density
	 **/
	fun byFileName(name: String): ImageResource? {
		return MR.images.getImageByFileName(name)
	}
}