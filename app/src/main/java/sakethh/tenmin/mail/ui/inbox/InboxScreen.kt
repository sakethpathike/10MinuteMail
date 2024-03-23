package sakethh.tenmin.mail.ui.inbox

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sakethh.tenmin.mail.ui.common.MailItem

@Composable
fun InboxScreen(inboxVM: InboxVM = hiltViewModel()) {
    val currentSessionData = inboxVM.currentSessionData.collectAsState().value
    val isCurrentSessionMailExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    val inboxMails = inboxVM.mails.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .clickable {
                    isCurrentSessionMailExpanded.value = !isCurrentSessionMailExpanded.value
                }
                .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Inbox", style = MaterialTheme.typography.titleSmall, modifier = Modifier
                )
                IconButton(onClick = {
                    isCurrentSessionMailExpanded.value = !isCurrentSessionMailExpanded.value
                }) {
                    Icon(
                        imageVector = if (isCurrentSessionMailExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Icons for expanding or hiding the details of the current session's mail address.."
                    )
                }
            }
            Box(modifier = Modifier.animateContentSize()) {
                if (isCurrentSessionMailExpanded.value) {
                    Text(
                        text = "Logged in as ${currentSessionData.mailAddress}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 20.dp, top = 12.dp)
                    )
                }
            }
        }
        items(inboxMails) {
            MailItem(
                intro = it.intro,
                createdAt = it.createdAt,
                subject = it.subject,
                fromName = it.from.name
            )
        }
    }
}