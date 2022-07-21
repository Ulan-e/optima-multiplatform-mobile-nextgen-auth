package kg.optima.mobile.resources

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.getImageByFileName
import org.example.library.MR

public object Images {
	/**
	 * Use [name] without resource format and density
	 **/
	public fun byFileName(name: String): ImageResource? {
		return MR.images.getImageByFileName(name)
	}
}