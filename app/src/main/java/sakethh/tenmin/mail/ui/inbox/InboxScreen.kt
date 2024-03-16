package sakethh.tenmin.mail.ui.inbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sakethh.tenmin.mail.ui.common.MailItem

@Composable
fun InboxScreen(inboxVM: InboxVM = hiltViewModel()) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Inbox", style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 20.dp, top = 12.dp)
            )
        }
        items(inboxVM.mails.value) {
            MailItem(
                intro = it.intro,
                createdAt = it.createdAt,
                subject = it.subject,
                fromName = it.from.name
            )
        }
    }
}