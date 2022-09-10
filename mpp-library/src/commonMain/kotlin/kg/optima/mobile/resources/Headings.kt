package kg.optima.mobile.resources

enum class Headings(val px: Double) {
	H1(24.0),
	H2(20.0),
	H3(18.0),
	H4(16.0),
	H5(14.0),
	H6(12.0),
	H7(10.0);

	companion object {
		val Headings.pix: Int
			get() = this.px.toInt()
	}
}