package sakethh.tenmin.mail.ui.accounts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import sakethh.tenmin.mail.ui.accounts.viewmodels.AccountsVM
import sakethh.tenmin.mail.ui.theme.fonts

@Composable
fun SignInScreen(accountsVM: AccountsVM = hiltViewModel()) {
    val emailAddress = rememberSaveable {
        mutableStateOf("")
    }
    val emailPassword = rememberSaveable {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp)) {
                        append("Welcome back!")
                    }
                }, style = MaterialTheme.typography.titleMedium, fontSize = 16.sp
            )
            OutlinedTextField(textStyle = TextStyle(
                fontFamily = fonts, fontWeight = FontWeight.Normal
            ), value = emailAddress.value, onValueChange = {
                emailAddress.value = it
            }, modifier = Modifier.fillMaxWidth(), label = {
                Text(
                    text = "Email address", style = MaterialTheme.typography.titleSmall
                )
            })
            OutlinedTextField(textStyle = TextStyle(
                fontFamily = fonts, fontWeight = FontWeight.Normal
            ), value = emailPassword.value, onValueChange = {
                emailPassword.value = it
            }, modifier = Modifier.fillMaxWidth(), label = {
                Text(
                    text = "Email password", style = MaterialTheme.typography.titleSmall
                )
            })
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                accountsVM.onUiClickEvent(
                    accountsUiEvent = AccountsUiEvent.SignIn(
                        emailAddress = emailAddress.value, emailPassword = emailPassword.value
                    )
                )
            }) {
                Text(
                    text = "Sign in", style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}