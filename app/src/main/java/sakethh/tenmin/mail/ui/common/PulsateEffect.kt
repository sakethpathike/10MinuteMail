package sakethh.tenmin.mail.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import sakethh.tenmin.mail.ui.info.ItemState

fun Modifier.pulsateEffect(onClick: () -> Unit) = composed {
    val itemState = remember { mutableStateOf(ItemState.Idle) }
    val scale = animateFloatAsState(
        if (itemState.value == ItemState.Pressed) 0.9f else 1f,
        label = ""
    )
    this
        .clickable(interactionSource = remember {
            MutableInteractionSource()
        }, indication = null, onClick = {
            onClick()
        })
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .pointerInput(itemState.value) {
            awaitPointerEventScope {
                itemState.value = if (itemState.value == ItemState.Pressed) {
                    waitForUpOrCancellation()
                    ItemState.Idle
                } else {
                    awaitFirstDown(false)
                    ItemState.Pressed
                }
            }
        }
}