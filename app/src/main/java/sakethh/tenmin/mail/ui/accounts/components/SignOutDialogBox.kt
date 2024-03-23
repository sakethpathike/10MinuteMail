package sakethh.tenmin.mail.ui.accounts.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun SignOutDialogBox(
    isVisible: MutableState<Boolean>, onSignOutClick: () -> Unit, currentSessionMailAddress: String
) {
    if (isVisible.value) {
        AlertDialog(onDismissRequest = { isVisible.value = false }, dismissButton = {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { isVisible.value = false }) {
                Text(text = "Cancel", style = MaterialTheme.typography.titleSmall)
            }
        }, confirmButton = {
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onSignOutClick() }) {
                Text(text = "Sign Out", style = MaterialTheme.typography.titleSmall)
            }
        }, title = {
            Text(
                text = "Are you sure you want to sign out?",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp
            )
        }, text = {
            Text(text = buildAnnotatedString {
                append("You're about to sign out from ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(currentSessionMailAddress)
                }
                append(".\n\nThis email address credentials will be saved locally for your next login.")
            }, style = MaterialTheme.typography.titleSmall)
        })
    }
}