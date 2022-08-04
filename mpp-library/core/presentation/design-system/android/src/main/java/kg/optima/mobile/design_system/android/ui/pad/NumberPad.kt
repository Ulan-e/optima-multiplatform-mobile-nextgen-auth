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
import androidx.compose.ui.unit.sp
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages
import kg.optima.mobile.resources.resId

typealias OnCellClick = (CellType) -> Unit

@Composable
fun NumberPad(
	modifier: Modifier = Modifier,
	onClick: OnCellClick = {}
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
			val cellType = CellType.Num(row[i])
			PinCell(
				cellType = cellType,
				onClick = { onClick(cellType) }
			)
		}
	}
}

@Composable
private fun BottomRow(
	modifier: Modifier = Modifier,
	onClick: OnCellClick,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		PinCell(
			cellType = CellType.Text(
				text = "Закрыть",
				color = ComposeColors.PrimaryRed
			),
			onClick = onClick,
		)
		PinCell(
			cellType = CellType.Num("0"),
			onClick = onClick,
		)
		PinCell(
			cellType = CellType.Img(
				resId = MainImages.backspace.resId()
			),
			onClick = onClick,
		)
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PinCell(
	modifier: Modifier = Modifier,
	cellType: CellType,
	onClick: OnCellClick,
) {
	if (cellType is CellType.Text) {
		Surface(
			modifier = Modifier.size(Deps.Size.pinBtnSize),
			onClick = { onClick(cellType) },
			shape = MaterialTheme.shapes.small,
			color = Color.Transparent,
		) {
			Box(
				contentAlignment = Alignment.Center
			) {
				Text(
					text = cellType.text,
					fontSize = Headings.H4.px.sp,
					color = cellType.color,
					maxLines = 1,
				)
			}
		}
	} else {
		Button(
			modifier = modifier
				.size(Deps.Size.pinBtnSize)
				.padding(Deps.Spacing.minPadding),
			onClick = { onClick(cellType) },
			shape = CircleShape,
			colors = ButtonDefaults.buttonColors(backgroundColor = ComposeColors.PrimaryWhite),
			elevation = ButtonDefaults.elevation(defaultElevation = Deps.pinBtnElevation),
		) {
			if (cellType is CellType.Num) {
				Box(modifier = Modifier.background(ComposeColors.PrimaryWhite)) {
					Text(
						text = cellType.num,
						modifier = Modifier.align(Alignment.Center),
						fontSize = Deps.TextSize.pinBtnText,
					)
				}
			} else if (cellType is CellType.Img) {
				Box(modifier = Modifier.background(ComposeColors.PrimaryWhite)) {
					Icon(
						modifier = Modifier
							.size(Deps.Size.mainButtonImageSize * 2)
							.align(Alignment.Center)
							.offset(y = 4.dp),
						painter = painterResource(id = cellType.resId),
						contentDescription = emptyString,
						tint = ComposeColors.PrimaryBlack,
					)
				}
			}
		}
	}
}