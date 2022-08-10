package kg.optima.mobile.design_system.android.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import kg.optima.mobile.design_system.android.R
import kg.optima.mobile.design_system.android.utils.BitmapUtils


//todo add state for ImagePainter.State.Loading, State.Error
@Composable
fun CoilImage(
	modifier: Modifier,
	data: MediaResource?,
	size: MediaSizeType = MediaSizeType.ESTATE_M,
	contentDescription: String? = null,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Fit,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null,
) {
	if (data == null) {
		Image(
			painter = painterResource(id = R.drawable.ic_avatar_placeholder),
			contentDescription = null,
		)
	} else {
		val image = openCurrentImage(data, size)
		val painter = rememberImagePainter(data = image)

		Box(modifier) {
			Image(
				painter = painter,
				contentDescription = contentDescription,
				alignment = alignment,
				contentScale = contentScale,
				alpha = alpha,
				colorFilter = colorFilter,
				modifier = modifier.matchParentSize()
			)
		}
	}
}

private fun openCurrentImage(data: MediaResource, size: MediaSizeType): Any {
	return when (data) {
		is MediaResource.Remote -> when (size) {
			MediaSizeType.ESTATE_M -> data.imagesM
			MediaSizeType.ESTATE_XS -> data.imagesXS
			MediaSizeType.ESTATE_XL -> data.imagesXL
			MediaSizeType.AVATAR_M -> data.imagesM
			MediaSizeType.AVATAR_L -> data.imagesXL
			else -> data.imagesM
		}
		is MediaResource.Local -> BitmapUtils.getBitmapFromPath(data.path).fold(
			fnL = {

			},
			fnR = { it }
		)
		is MediaResource.Video -> data.urlImage
	}
}