package sakethh.tenmin.mail.ui.accounts.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun DeleteAccountDialogBox(
    isVisible: MutableState<Boolean>,
    onDeleteAccountClick: (deleteAccountLocally: Boolean, deleteAccountFromCloud: Boolean) -> Unit,
    currentSessionMailAddress: String
) {
    if (isVisible.value) {
        val deleteAccountLocally = rememberSaveable {
            mutableStateOf(false)
        }
        val deleteAccountFromCloud = rememberSaveable {
            mutableStateOf(false)
        }
        AlertDialog(
            onDismissRequest = { isVisible.value = false },
            dismissButton = {
                FilledTonalButton(modifier = Modifier.fillMaxWidth(),
                    onClick = { isVisible.value = false }) {
                    Text(text = "Cancel", style = MaterialTheme.typography.titleSmall)
                }
            },
            confirmButton = {
                if (deleteAccountLocally.value || deleteAccountFromCloud.value) {
                    Button(colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ), modifier = Modifier.fillMaxWidth(), onClick = {
                        onDeleteAccountClick(
                            deleteAccountLocally.value, deleteAccountFromCloud.value
                        )
                    }) {
                        Text(
                            text = "Delete account permanently",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            },
            title = {
                Text(
                    text = "Confirm Account Deletion: Locally, in the Cloud, or Both?",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp
                )
            },
            text = {
                Box(modifier = Modifier.animateContentSize()) {
                    Column {
                        Text(text = buildAnnotatedString {
                            append("You're about to delete ")
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(currentSessionMailAddress)
                            }
                            append(".")
                        }, style = MaterialTheme.typography.titleSmall)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    deleteAccountLocally.value = !deleteAccountLocally.value
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = deleteAccountLocally.value, onCheckedChange = {
                                deleteAccountLocally.value = it
                            })
                            Text(
                                text = "Delete account locally",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        if (deleteAccountLocally.value) {
                            Text(
                                text = "After deletion, the credentials for this email address will be removed from your device. To reuse this account, you'll need to enter your email address and password again.",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    deleteAccountFromCloud.value = !deleteAccountFromCloud.value
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = deleteAccountFromCloud.value, onCheckedChange = {
                                deleteAccountFromCloud.value = it
                            })
                            Text(
                                text = "Delete Account from Cloud",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        if (deleteAccountFromCloud.value) {
                            Text(
                                text = "After deletion, the credentials for this email address will be removed from the cloud. However, they will still remain on your device, and a label will be added in the Inbox indicating that this email doesn't exist in the cloud.",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            })
    }
}