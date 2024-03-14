package sakethh.tenmin.mail.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@SuppressLint("AutoboxingStateValueProperty", "AutoboxingStateCreation")
@Composable
fun MailItem() {
    val isChecked = remember {
        mutableStateOf(false)
    }
    val itemOffSetX = remember {
        mutableFloatStateOf(0f)
    }
    val isDragging = remember {
        mutableStateOf(false)
    }
    val (totalHeight, totalWidth) = remember {
        Pair(mutableStateOf(0), mutableStateOf(0))
    }
    LaunchedEffect(key1 = isDragging.value) {
        if (!isDragging.value) {
            itemOffSetX.value = 0f
        }

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .matchParentSize()
        ) {
            Text(text = "sample")
        }
        Row(
            modifier = Modifier
                .clickable {
                    isChecked.value = !isChecked.value
                }
                .onGloballyPositioned {
                    totalWidth.value = it.size.width
                    totalHeight.value = it.size.height
                }
                .offset {
                    IntOffset(itemOffSetX.value.roundToInt(), 0)
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        isDragging.value = true
                        itemOffSetX.value += dragAmount
                        if (itemOffSetX.value / totalWidth.value >= 0.5) {

                        }
                        if (itemOffSetX.value / totalWidth.value <= -0.5) {

                        }
                    }, onDragStart = {
                        isDragging.value = true
                    }, onDragEnd = {
                        isDragging.value = false
                    }, onDragCancel = {
                        isDragging.value = false
                    })
                }
                .padding(15.dp)
                .fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Mail Sender Image",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = buildString {
                            repeat(20) {
                                append(itemOffSetX.value)
                                append(" ")
                            }
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.75f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (!isChecked.value) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp,
                        color = if (!isChecked.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                            0.75f
                        )
                    )
                    Text(
                        text = "7:32 PM",
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!isChecked.value) FontWeight.Bold else FontWeight.Normal,
                        color = if (!isChecked.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                            0.75f
                        )
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.90f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = buildString {
                                repeat(20) {
                                    append("CC ")
                                }
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 12.sp,
                            fontWeight = if (!isChecked.value) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isChecked.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                                0.75f
                            )
                        )
                        Text(
                            text = buildString {
                                repeat(20) {
                                    append("Body ")
                                }
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (!isChecked.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                                0.75f
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = "Star State Icon"
                    )
                }
            }
        }
    }
}