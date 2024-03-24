package sakethh.tenmin.mail.ui.accounts.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import sakethh.tenmin.mail.ui.accounts.AccountsEvent
import sakethh.tenmin.mail.ui.accounts.viewmodels.SignInVM
import sakethh.tenmin.mail.ui.theme.fonts

@Composable
fun SignInScreen(signInVM: SignInVM = hiltViewModel(), navController: NavController) {
    val uiEvent = signInVM.uiEvent.collectAsState(initial = AccountsEvent.None)
    LaunchedEffect(key1 = true) {
        signInVM.uiEvent.collect {
            when (it) {
                is AccountsEvent.Navigate -> navController.navigate(it.navigationRoute) {
                    popUpTo(0)
                }

                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .navigationBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val emailAddress = rememberSaveable {
            mutableStateOf("")
        }
        val emailPassword = rememberSaveable {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp)) {
                        append("Welcome back!")
                    }
                }, style = MaterialTheme.typography.titleMedium, fontSize = 16.sp
            )
            OutlinedTextField(
                readOnly = uiEvent.value != AccountsEvent.None && uiEvent.value != AccountsEvent.HttpResponse.Invalid401 && uiEvent.value != AccountsEvent.MailAlreadyExists,
                textStyle = TextStyle(
                    fontFamily = fonts, fontWeight = FontWeight.Normal
                ),
                value = emailAddress.value,
                onValueChange = {
                    emailAddress.value = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = "Email address", style = MaterialTheme.typography.titleSmall
                    )
                })
            OutlinedTextField(
                readOnly = uiEvent.value != AccountsEvent.None && uiEvent.value != AccountsEvent.HttpResponse.Invalid401 && uiEvent.value != AccountsEvent.MailAlreadyExists,
                textStyle = TextStyle(
                    fontFamily = fonts, fontWeight = FontWeight.Normal
                ),
                value = emailPassword.value,
                onValueChange = {
                    emailPassword.value = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = "Email password", style = MaterialTheme.typography.titleSmall
                    )
                })
            if (uiEvent.value == AccountsEvent.None || uiEvent.value == AccountsEvent.HttpResponse.Invalid401 || uiEvent.value == AccountsEvent.MailAlreadyExists) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    signInVM.onUiClickEvent(
                        AccountsUiEvent.SignIn(
                            emailAddress.value,
                            emailPassword.value
                        )
                    )
                }) {
                    Text(
                        text = "Sign in", style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            if (uiEvent.value != AccountsEvent.None) {
                if (uiEvent.value != AccountsEvent.HttpResponse.Invalid401 && uiEvent.value != AccountsEvent.MailAlreadyExists) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
                Text(
                    text = "Status", style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = uiEvent.value.toString(), style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier)
        }
    }
}