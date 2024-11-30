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
    totalSpans: Int = 12,
    content: @Composable SpanLayoutScope.() -> Unit
) {
    val scope = SpanLayoutScopeImpl(windowWidthSizeClass)

    Layout(
        modifier = modifier.fillMaxWidth(),
        content = { scope.content() }
    ) { measurables, constraints ->
        val totalWidth = constraints.maxWidth
        val eachSpanWidth = totalWidth / totalSpans

        // Measure children and enforce .span usage
        val placeables = measurables.map { measurable ->
            var span = measurable.layoutId as? Int
            if (span == null && measurables.size > 1) {
                throw IllegalStateException("Every child in SpanLayout must use the .span modifier.")
            }
            if (span!! < 1) {
                throw IllegalStateException("span can't be less than 1")
            }
            if (span > 12) {
                throw IllegalStateException("span can't be greater than 12")
            }
            val childWidth = eachSpanWidth * span
            val childConstraints = constraints.copy(
                minWidth = childWidth,
                maxWidth = childWidth
            )
            measurable.measure(childConstraints)
        }

        // Arrange children in rows
        var currentX = 0
        var currentY = 0
        var usedSpans = 0
        val rowHeights = mutableListOf<Int>()

        val positions = mutableListOf<Triple<Int, Int, Placeable>>()

        placeables.forEachIndexed { index, placeable ->
            val span = measurables[index].layoutId as Int
            if (usedSpans + span > totalSpans) {
                // Move to the next row
                currentX = 0
                currentY += rowHeights.maxOrNull()?.plus(interRowSpacing.toPx().toInt()) ?: 0
                rowHeights.clear()
                usedSpans = 0
            }

            positions.add(Triple(currentX, currentY, placeable))

            currentX += placeable.width
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
