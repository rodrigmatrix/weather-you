package com.rodrigmatrix.weatheryou.wearos.presentation.components.pager

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.HierarchicalFocusCoordinator
import androidx.wear.compose.foundation.OnFocusChange
import androidx.wear.compose.material.HorizontalPageIndicator
import androidx.wear.compose.material.PageIndicatorState
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText

internal val LocalScaffoldState = compositionLocalOf { ScaffoldState() }


internal class ScaffoldState {
    fun removeScreen(key: Any) {
        screenContent.removeIf { it.key === key }
    }

    fun addScreen(
        key: Any,
        timeText: @Composable (() -> Unit)?,
        scrollState: ScrollableState?,
    ) {
        screenContent.add(ScreenContent(key, scrollState, timeText))
    }

    internal val appTimeText: MutableState<(@Composable (() -> Unit))> =
        mutableStateOf({ TimeText() })
    internal val screenContent = mutableStateListOf<ScreenContent>()

    val timeText: @Composable (() -> Unit)
        get() = {
            val (scrollState, timeText) = currentContent()

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                timeText()
            }
        }

    private fun currentContent(): Pair<ScrollableState?, @Composable (() -> Unit)> {
        var resultTimeText: @Composable (() -> Unit)? = null
        var resultState: ScrollableState? = null
        screenContent.forEach {
            if (it.timeText != null) {
                resultTimeText = it.timeText
            }
            if (it.scrollState != null) {
                resultState = it.scrollState
            }
        }
        return Pair(resultState, resultTimeText ?: appTimeText.value)
    }

    internal data class ScreenContent(
        val key: Any,
        val scrollState: ScrollableState? = null,
        val timeText: (@Composable () -> Unit)? = null,
    )
}


@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun PagerScaffold(
    modifier: Modifier = Modifier,
    timeText: (@Composable () -> Unit)? = null,
    pagerState: PagerState? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val scaffoldState = LocalScaffoldState.current

    val key = remember { Any() }

    DisposableEffect(key) {
        onDispose {
            scaffoldState.removeScreen(key)
        }
    }

    OnFocusChange { focused ->
        if (focused) {
            scaffoldState.addScreen(key, timeText, null)
        } else {
            scaffoldState.removeScreen(key)
        }
    }

    Scaffold(
        modifier = modifier,
        timeText = timeText,
        pageIndicator = {
            if (pagerState != null) {
                val pageIndicatorState = remember(pagerState) { PageScreenIndicatorState(pagerState) }

                HorizontalPageIndicator(
                    modifier = Modifier.padding(6.dp),
                    pageIndicatorState = pageIndicatorState,
                )
            }
        },
        content = { Box { content() } },
    )
}

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun PagerScreen(
    modifier: Modifier = Modifier,
    state: PagerState,
    timeText: (@Composable () -> Unit)? = null,
    content: @Composable ((Int) -> Unit),
) {
    PagerScaffold(
        modifier = Modifier.fillMaxSize(),
        timeText = timeText,
        pagerState = state,
    ) {
        HorizontalPager(
            modifier = modifier,
            state = state,
        ) { page ->
            ClippedBox(state) {
                HierarchicalFocusCoordinator(requiresFocus = { page == state.currentPage }) {
                    content(page)
                }
            }
        }
    }
}

@Composable
internal fun ClippedBox(pagerState: PagerState, content: @Composable () -> Unit) {
    val shape = rememberClipWhenScrolling(pagerState)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .optionalClip(shape),
    ) {
        content()
    }
}

@Composable
private fun rememberClipWhenScrolling(state: PagerState): androidx.compose.runtime.State<RoundedCornerShape?> {
    val shape = if (LocalConfiguration.current.isScreenRound) CircleShape else null
    return remember(state) {
        derivedStateOf {
            if (shape != null && state.currentPageOffsetFraction != 0f) {
                shape
            } else {
                null
            }
        }
    }
}

private fun Modifier.optionalClip(shapeState: androidx.compose.runtime.State<RoundedCornerShape?>): Modifier {
    val shape = shapeState.value

    return if (shape != null) {
        clip(shape)
    } else {
        this
    }
}

/**
 * Bridge between Foundation PagerState and the Wear Compose PageIndicatorState.
 */
class PageScreenIndicatorState(
    private val state: PagerState,
) : PageIndicatorState {
    override val pageCount: Int
        get() = state.pageCount

    override val pageOffset: Float
        get() = state.currentPageOffsetFraction.takeIf { it.isFinite() } ?: 0f

    override val selectedPage: Int
        get() = state.currentPage
}