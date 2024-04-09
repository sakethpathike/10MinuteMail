package sakethh.tenmin.mail.ui.settings.common.timePicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sakethh.tenmin.mail.ui.settings.SettingsScreenVM

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> AutoRefreshMailsIntervalPicker(
    autoRefreshMailsIntervalPickerState: AutoRefreshMailsIntervalPickerState,
    pickerList: List<T>,
    topDividerPaddingValues: PaddingValues,
    bottomDividerPaddingValues: PaddingValues,
) {
    val textComponentHeight = remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current
    val lazyListState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }.collect {
            autoRefreshMailsIntervalPickerState.firstVisibleIndexItem.intValue = it
        }
    }
    Box(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(0.5f)
            .background(if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.Center)
                .height(textComponentHeight.value * 3)
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Transparent, 0.5f to Color.Black, 1f to Color.Transparent
                        ), blendMode = BlendMode.DstIn
                    )
                }, flingBehavior = flingBehavior, state = lazyListState
        ) {
            item {
                Spacer(modifier = Modifier.height(textComponentHeight.value))
            }
            items(pickerList) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = it.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .onGloballyPositioned {
                                with(density) {
                                    textComponentHeight.value = it.size.height.toDp()
                                }
                            }
                            .padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(textComponentHeight.value))
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .offset(y = textComponentHeight.value * (3 / 2))
                .padding(topDividerPaddingValues)
        )
        HorizontalDivider(
            modifier = Modifier
                .offset(y = textComponentHeight.value * ((3 / 2) + 1))
                .padding(bottomDividerPaddingValues)
        )
    }
}