package sakethh.tenmin.mail.ui.common

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DeleteForever
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("AutoboxingStateValueProperty", "AutoboxingStateCreation")
@Composable
fun MailItem(intro: String, createdAt: String, subject: String, fromName: String) {
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
    val draggingTowardsRight = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isDragging.value) {
        if (!isDragging.value) {
            itemOffSetX.value = 0f
        }
    }
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier
            .background(if (draggingTowardsRight.value) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer)
    ) {
        if (draggingTowardsRight.value) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = "Delete Forever"
                )
                Text(text = "Delete", style = MaterialTheme.typography.titleSmall)
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Archive, contentDescription = "Archive"
                )
                Text(text = "Archive", style = MaterialTheme.typography.titleSmall)
            }
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
                        draggingTowardsRight.value = itemOffSetX.value < 0
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
                .background(MaterialTheme.colorScheme.surface)
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
                        text = subject,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.65f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (!isChecked.value) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp,
                        color = if (!isChecked.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                            0.75f
                        )
                    )

                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US)
                    val date = inputFormat.parse(createdAt)
                    val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
                    val formattedTimestamp = outputFormat.format(date)

                    Text(
                        text = formattedTimestamp,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!isChecked.value) FontWeight.Bold else FontWeight.Normal,
                        color = if (!isChecked.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                            0.75f
                        ), maxLines = 1
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
                            text = intro,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 14.sp,
                            fontWeight = if (!isChecked.value) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isChecked.value) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                                0.75f
                            )
                        )
                        Text(
                            text = "From: $fromName",
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