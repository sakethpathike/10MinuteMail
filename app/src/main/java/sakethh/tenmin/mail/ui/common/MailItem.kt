package sakethh.tenmin.mail.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MailItem() {
    val isChecked = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .clickable {
                isChecked.value = !isChecked.value
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
                            append("Title ")
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