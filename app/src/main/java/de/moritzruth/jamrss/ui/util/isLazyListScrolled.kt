package de.moritzruth.jamrss.ui.util

import androidx.compose.foundation.lazy.LazyListState

fun isLazyListScrolled(lazyListState: LazyListState) =
    lazyListState.firstVisibleItemIndex != 0 || lazyListState.firstVisibleItemScrollOffset != 0