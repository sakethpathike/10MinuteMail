package sakethh.tenmin.mail.ui.accounts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CurrentSessionItem(emailAddress: String, emailPassword: String) {
    val isPasswordHidden = rememberSaveable {
        mutableStateOf(true)
    }
    val localClipBoardManager = LocalClipboardManager.current
    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emailAddress,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                        .fillMaxWidth(0.65f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row {
                    IconButton(onClick = {}) {

                    }
                    IconButton(onClick = {
                        localClipBoardManager.setText(AnnotatedString(emailAddress))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "copy Mail Address Icon Button"
                        )
                    }
                    Spacer(modifier = Modifier.padding(end = 20.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        if (isPasswordHidden.value) repeat((5..10).random()) { append("*") } else append(
                            emailPassword
                        )
                    },
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                        .fillMaxWidth(0.65f),
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    IconButton(onClick = {
                        isPasswordHidden.value = !isPasswordHidden.value
                    }) {
                        Icon(
                            imageVector = if (isPasswordHidden.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Reveal password Icon Button"
                        )
                    }
                    IconButton(onClick = {
                        localClipBoardManager.setText(AnnotatedString(emailPassword))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "copy Mail Password Icon Button"
                        )
                    }
                    Spacer(modifier = Modifier.padding(end = 20.dp))
                }
            }
        }

    }
}