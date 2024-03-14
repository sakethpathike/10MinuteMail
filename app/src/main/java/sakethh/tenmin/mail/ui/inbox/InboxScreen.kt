package sakethh.tenmin.mail.ui.inbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sakethh.tenmin.mail.ui.common.MailItem

@Composable
fun InboxScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Inbox", style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 20.dp, top = 12.dp)
            )
        }
        items(25) {
            MailItem()
        }
    }
}