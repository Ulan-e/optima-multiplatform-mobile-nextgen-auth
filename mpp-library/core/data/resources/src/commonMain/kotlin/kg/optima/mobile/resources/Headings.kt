package kg.optima.mobile.resources

public enum class Headings(public val px: Double) {
	H1(24.0),
	H2(20.0),
	H3(18.0),
	H4(16.0),
	H5(14.0),
	H6(12.0),
	H7(10.0);

	public companion object {
		public fun Headings.px(): Int = this.px.toInt()
	}
}