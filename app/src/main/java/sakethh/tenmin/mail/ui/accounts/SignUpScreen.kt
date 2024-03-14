package sakethh.tenmin.mail.ui.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp)) {
                        append("Welcome to\n")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Black, fontSize = 26.sp)) {
                        append("10 Minute Mail ")
                    }
                    append("\n")
                    append("A ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                        append("temporary email")
                    }
                    append(" client for Android, built on the secure ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                        append("mail.gw")
                    }
                    append(" API. Free, open-source, and user-friendly.")

                }, style = MaterialTheme.typography.titleMedium, fontSize = 16.sp
            )
            Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Text(
                    text = "Sign in", style = MaterialTheme.typography.titleSmall
                )
            }
            FilledTonalButton(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                Text(
                    text = "Generate a temporary email account",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row {
                TextButton(onClick = {}) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.QuestionAnswer,
                                contentDescription = "FAQ Icon"
                            )
                            Text(
                                text = " FAQ", style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
                TextButton(onClick = {}) {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Help, contentDescription = "Help Icon")
                            Text(
                                text = " Help",
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }

                }
            }
            Spacer(modifier = Modifier)
        }
    }
}