package sakethh.tenmin.mail.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import sakethh.tenmin.mail.ui.settings.SettingsScreenVM

@Composable
fun AccountItem(
    emailAddress: String,
    emailId: String,
    onAccountClick: () -> Unit,
    selected: MutableState<Boolean> = rememberSaveable {
        mutableStateOf(false)
    }
) {
    Row(
        modifier = Modifier
            .background(if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .clickable {
                onAccountClick()
            }
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 15.dp)
        ) {
            Icon(
                imageVector = if (selected.value) Icons.Default.Check else Icons.Default.AccountCircle,
                contentDescription = "Mail Sender Image",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = emailAddress,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                }
                SelectionContainer {
                    Text(
                        text = emailId,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}