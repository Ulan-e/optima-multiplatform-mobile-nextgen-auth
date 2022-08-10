package kg.optima.mobile.design_system.android.utils

import android.graphics.Bitmap
import id.zelory.compressor.loadBitmap
import kg.optima.mobile.base.data.model.Either
import java.io.File

object BitmapUtils {
	fun getBitmapFromPath(path: String): Either<Throwable, Bitmap> {
		return try {
			val file = File(path)
			Either.Right(loadBitmap(file))
		} catch (e: Exception) {
			Either.Left(e)
		}
	}
}