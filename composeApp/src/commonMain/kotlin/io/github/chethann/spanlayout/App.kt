package io.github.chethann.spanlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        //ResponsiveSpanLayout(windowSizeClass.windowWidthSizeClass)
        ResponsiveSpanLayoutTwo(windowSizeClass.windowWidthSizeClass)
    }
}

@Composable
fun ResponsiveSpanLayoutTwo(windowWidthSizeClass: WindowWidthSizeClass) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        SpanLayout(
            windowWidthSizeClass = windowWidthSizeClass,
            modifier = Modifier.padding(horizontal = 4.dp),
            interRowSpacing = 16.dp
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
                        .padding(horizontal = 4.dp)
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