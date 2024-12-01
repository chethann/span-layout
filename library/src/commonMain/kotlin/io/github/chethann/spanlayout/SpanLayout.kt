package io.github.chethann.spanlayout

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@LayoutScopeMarker
@Immutable
interface SpanLayoutScope {
    @Stable
    fun Modifier.span(
        compactSpan: Int,
        mediumSpan: Int,
        expandedSpan: Int
    ): Modifier
}

private class SpanLayoutScopeImpl(
    private val windowWidthSizeClass: WindowWidthSizeClass
) : SpanLayoutScope {

    override fun Modifier.span(
        compactSpan: Int,
        mediumSpan: Int,
        expandedSpan: Int
    ): Modifier {
        val span = when (windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> compactSpan
            WindowWidthSizeClass.MEDIUM -> mediumSpan
            WindowWidthSizeClass.EXPANDED -> expandedSpan
            else -> compactSpan // Default to compact
        }
        return this.then(Modifier.layoutId(span))
    }
}

@Composable
fun SpanLayout(
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    interRowSpacing: Dp = 0.dp,
    gutterSpace: Dp = 0.dp,
    totalSpans: Int = 12,
    stretchToFillRow: Boolean = false,
    content: @Composable SpanLayoutScope.() -> Unit
) {
    val scope = SpanLayoutScopeImpl(windowWidthSizeClass)

    Layout(
        modifier = modifier.fillMaxWidth(),
        content = { scope.content() }
    ) { measurables, constraints ->
        val totalWidth = constraints.maxWidth
        val gutterSpacePx = gutterSpace.toPx().toInt()
        val interRowSpacingPx = interRowSpacing.toPx().toInt()

        // Adjust total available width to account for gutters
        val availableWidth = totalWidth - (gutterSpacePx * (totalSpans + 1))
        val eachSpanWidth = availableWidth / totalSpans

        // Arrange children in rows
        var currentX = gutterSpacePx // Start with gutter space
        var currentY = 0
        var usedSpans = 0
        var numberOfIterations = 0
        val rowHeights = mutableListOf<Int>()

        // Measure children and enforce .span usage
        val placeables = measurables.mapIndexed { index, measurable ->
            val span = measurable.layoutId as? Int
                ?: throw IllegalStateException("Every child in SpanLayout must use the .span modifier.")
            if (span < 1 || span > totalSpans) {
                throw IllegalStateException("Span must be between 1 and $totalSpans.")
            }

            val childWidth = eachSpanWidth * span + gutterSpacePx * (span - 1)
            val childConstraints = constraints.copy(
                minWidth = childWidth,
                maxWidth = childWidth,
            )
            if (stretchToFillRow) {
                if (numberOfIterations == 0) {
                    rowHeights.clear()
                    usedSpans = 0
                    var _index = index

                    while (usedSpans + span <= totalSpans && _index <= measurables.size - 1){
                        rowHeights.add(measurables[_index].maxIntrinsicHeight(childWidth))
                        usedSpans += span
                        _index++
                        numberOfIterations++
                    }
                }
                numberOfIterations--
                val rowHeight = rowHeights.maxOrNull() ?: 0
                return@mapIndexed measurable.measure(childConstraints.copy(
                    minHeight = rowHeight,
                    maxHeight = rowHeight
                ))
            }
            else {
                return@mapIndexed measurable.measure(childConstraints)
            }
        }

        rowHeights.clear()
        currentY = 0
        usedSpans = 0

        val positions = mutableListOf<Triple<Int, Int, Placeable>>()

        placeables.forEachIndexed { index, placeable ->
            val span = measurables[index].layoutId as Int
            if (usedSpans + span > totalSpans) {
                // Move to the next row
                currentX = gutterSpacePx // Reset to gutter space at row start
                currentY += rowHeights.maxOrNull()?.plus(interRowSpacingPx) ?: 0
                rowHeights.clear()
                usedSpans = 0
            }

            positions.add(Triple(currentX, currentY, placeable))

            currentX += placeable.width + gutterSpacePx // Add gutter space
            usedSpans += span
            rowHeights.add(placeable.height)
        }

        val totalHeight = currentY + (rowHeights.maxOrNull() ?: 0)

        layout(constraints.maxWidth, totalHeight) {
            positions.forEach { (x, y, placeable) ->
                placeable.placeRelative(x, y)
            }
        }
    }
}

