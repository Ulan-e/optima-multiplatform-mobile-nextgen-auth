package kg.optima.mobile.design_system.android.ui.pad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages

@Composable
fun NumberPad(
	modifier: Modifier = Modifier,
	onClick: OnCellClick = {},
	actionCell: Cell,
) {
	val rows = listOf(
		listOf("1", "2", "3"),
		listOf("4", "5", "6"),
		listOf("7", "8", "9"),
	)
	Column(modifier = modifier) {
		rows.forEachIndexed { i, row ->
			NumberRow(
				modifier = when {
					i == rows.size / 2 ->
						Modifier.padding(vertical = Deps.Spacing.pinBtnYMargin)
					rows.size % 2 == 0 && i == rows.size / 2 - 1 ->
						Modifier.padding(top = Deps.Spacing.pinBtnYMargin)
					else -> Modifier
				},
				row = row,
				onClick = onClick
			)
		}
		BottomRow(
			modifier = Modifier.padding(top = Deps.Spacing.pinBtnYMargin),
			onClick = onClick,
			startCell = actionCell,
		)
	}
}

@Composable
private fun NumberRow(
	modifier: Modifier = Modifier,
	row: List<String>,
	onClick: OnCellClick,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		repeat(row.size) { i ->
			val cell = Cell.Num(row[i], onClick)
			PinCell(cell = cell)
		}
	}
}

@Composable
private fun BottomRow(
	modifier: Modifier = Modifier,
	startCell: Cell,
	onClick: OnCellClick,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		PinCell(
			cell = startCell,
		)
		PinCell(
			cell = Cell.Num("0", onClick = onClick),
		)
		PinCell(
			cell = Cell.Img(
				resId = MainImages.backspace.resId(),
				onClick = onClick,
			)
		)
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PinCell(
	modifier: Modifier = Modifier,
	cell: Cell,
) {
	if (cell is Cell.Text) {
		Surface(
			modifier = Modifier.size(Deps.Size.pinBtnSize),
			onClick = { cell.onClick(cell) },
			shape = MaterialTheme.shapes.small,
			color = Color.Transparent,
		) {
			Box(
				contentAlignment = Alignment.Center
			) {
				Text(
					text = cell.text,
					fontSize = Headings.H4.sp,
					color = cell.color,
					maxLines = 1,
				)
			}
		}
	} else {
		val background = if (cell.withBackground) ComposeColors.PrimaryWhite else Color.Transparent
		val elevation = if (cell.withBackground) ButtonDefaults.elevation() else null

		Button(
			modifier = modifier
				.size(Deps.Size.pinBtnSize)
				.padding(Deps.Spacing.minPadding),
			onClick = { cell.onClick(cell) },
			shape = CircleShape,
			colors = ButtonDefaults.buttonColors(backgroundColor = background),
			elevation = elevation,
		) {
			Box(modifier = Modifier.background(background)) {
				when (cell) {
					is Cell.Num -> Text(
						text = cell.num,
						modifier = Modifier.align(Alignment.Center),
						fontSize = Deps.TextSize.pinBtnText,
					)
					is Cell.Img -> Icon(
						modifier = Modifier
							.size(Deps.Size.mainButtonImageSize * 2)
							.align(Alignment.Center)
							.offset(y = 3.dp),
						painter = painterResource(id = cell.resId),
						contentDescription = emptyString,
					)
					else -> Unit
				}
			}
		}
	}
}