package io.github.chethann.spanlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        //ResponsiveSpanLayout(windowSizeClass.windowWidthSizeClass)
        //ResponsiveSpanLayoutTwo(windowSizeClass.windowWidthSizeClass)

        Calender(windowSizeClass.windowWidthSizeClass)
    }
}

@Composable
fun Calender(windowWidthSizeClass: WindowWidthSizeClass) {
    val daysInMonth = remember { listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31) }
    val monthNames = remember { listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December") }
    val dayOfTheWeekInitial = remember { listOf("S", "M", "T", "W", "T", "F", "S") }
    val daysAndMonthNames = daysInMonth.zip(monthNames)
    var skips = 0

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        SpanLayout(
            windowWidthSizeClass = windowWidthSizeClass,
            interRowSpacing = 8.dp,
            gutterSpace = 8.dp,
            stretchToFillRow = true
        ) {
            daysAndMonthNames.forEachIndexed { index, daysAndMonthName ->
                Column(modifier = Modifier
                    .background(Color.Gray)
                    .span(
                        compactSpan = 12,
                        mediumSpan = 6,
                        expandedSpan = 4
                    )
                ) {

                    Text(daysAndMonthName.second, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    SpanLayout(
                        windowWidthSizeClass = windowWidthSizeClass,
                        interRowSpacing = 4.dp,
                        gutterSpace = 4.dp,
                        totalSpans = 7
                    ) {

                        dayOfTheWeekInitial.forEach {
                            Text(
                                it,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .span(
                                        compactSpan = 1,
                                        mediumSpan = 1,
                                        expandedSpan = 1
                                    )
                            )
                        }

                        repeat(skips) {
                            Box(modifier = Modifier
                                .span(
                                    compactSpan = 1,
                                    mediumSpan = 1,
                                    expandedSpan = 1
                                ))
                        }

                        skips = (skips + daysAndMonthName.first) % 7

                        repeat(daysAndMonthName.first) { index ->
                            Text(
                                (index + 1).toString(),
                                modifier = Modifier
                                    .span(
                                    compactSpan = 1,
                                    mediumSpan = 1,
                                    expandedSpan = 1
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResponsiveSpanLayoutTwo(windowWidthSizeClass: WindowWidthSizeClass) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        SpanLayout(
            windowWidthSizeClass = windowWidthSizeClass,
            interRowSpacing = 4.dp,
            gutterSpace = 4.dp
        ) {
            Box(
                modifier = Modifier.background(Color.Blue).height(100.dp)
                    .span(compactSpan = 12, mediumSpan = 6, expandedSpan = 4)
            )
            Box(
                modifier = Modifier.background(Color.Red).height(100.dp)
                    .span(compactSpan = 12, mediumSpan = 12, expandedSpan = 8)
            )

            Box(
                modifier = Modifier.background(Color.Red).height(100.dp)
                    .span(compactSpan = 12, mediumSpan = 12, expandedSpan = 8)
            )
            Box(
                modifier = Modifier.background(Color.Blue).height(100.dp)
                    .span(compactSpan = 12, mediumSpan = 6, expandedSpan = 4)
            )

            repeat(12) { index ->
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .background(Color.Red)
                        .span(
                            compactSpan = 6,
                            mediumSpan = 2,
                            expandedSpan = 1
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Item $index", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ResponsiveSpanLayout(windowWidthSizeClass: WindowWidthSizeClass) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        SpanLayout(
            windowWidthSizeClass = windowWidthSizeClass, modifier = Modifier.padding(horizontal = 4.dp),
            interRowSpacing = 8.dp,
            totalSpans = 24
        ) {
            repeat(12) { index ->

                if (index % 5 == 0) {
                    SpanLayout(
                        windowWidthSizeClass = windowWidthSizeClass,
                        modifier = Modifier.span(
                            compactSpan = 12,
                            mediumSpan = 3,
                            expandedSpan = 2
                        )
                            .background(Color.Cyan)
                    ) {
                        repeat(12) { index ->
                            Text("$index", color = Color.White, modifier = Modifier.span(
                                compactSpan = 12,
                                mediumSpan = 3,
                                expandedSpan = 2
                            ))
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(100.dp)
                            .background(Color.Red)
                            .span(
                                compactSpan = 12,
                                mediumSpan = 3,
                                expandedSpan = 2
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Item $index", color = Color.White)
                    }
                }
            }
        }
    }
}