package kg.optima.mobile.design_system.android.ui.text_fields

import android.text.Layout
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView


class DefaultLinkMovementMethod(
	private val onLinkClicked: OnLinkClicked
) : LinkMovementMethod() {
	override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
		when (event.action) {
			MotionEvent.ACTION_UP -> {
				var x = event.x.toInt()
				var y = event.y.toInt()
				x -= widget.totalPaddingLeft
				y -= widget.totalPaddingTop
				x += widget.scrollX
				y += widget.scrollY

				val layout: Layout = widget.layout
				val line: Int = layout.getLineForVertical(y)
				val off: Int = layout.getOffsetForHorizontal(line, x.toFloat())
				val link = buffer.getSpans(off, off, URLSpan::class.java)

				onLinkClicked(link.firstOrNull()?.url)
			}
		}

		return super.onTouchEvent(widget, buffer, event)
	}

}

typealias OnLinkClicked = (url: String?) -> Boolean