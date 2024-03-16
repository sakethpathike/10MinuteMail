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
import androidx.compose.runtime.State
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
import sakethh.tenmin.mail.ui.accounts.StartUpEvent
import sakethh.tenmin.mail.ui.accounts.viewmodels.SignInVM
import sakethh.tenmin.mail.ui.theme.fonts

@Composable
fun SignInScreen(signInVM: SignInVM = hiltViewModel(), navController: NavController) {
    val uiEvent = signInVM.uiEvent.collectAsState(initial = StartUpEvent.None)
    LaunchedEffect(key1 = true) {
        signInVM.uiEvent.collect {
            when (it) {
                is StartUpEvent.Navigate -> navController.navigate(it.navigationRoute)

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
        SignInComponent(uiEvent = uiEvent, onSignInClick = { emailAddress, emailPassword ->
            signInVM.onUiClickEvent(AccountsUiEvent.SignIn(emailAddress, emailPassword))
        })

    }
}

@Composable
private fun SignInComponent(
    uiEvent: State<StartUpEvent>,
    onSignInClick: (emailAddress: String, emailPassword: String) -> Unit
) {
    val emailAddress = rememberSaveable {
        mutableStateOf("ptardk@miteon.com")
    }
    val emailPassword = rememberSaveable {
        mutableStateOf("&iwb0oRI")
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
            readOnly = uiEvent.value != StartUpEvent.None && uiEvent.value != StartUpEvent.HttpResponse.Invalid401,
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
            readOnly = uiEvent.value != StartUpEvent.None && uiEvent.value != StartUpEvent.HttpResponse.Invalid401,
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
        if (uiEvent.value == StartUpEvent.None || uiEvent.value == StartUpEvent.HttpResponse.Invalid401) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                onSignInClick(emailAddress.value, emailPassword.value)
            }) {
                Text(
                    text = "Sign in", style = MaterialTheme.typography.titleSmall
                )
            }
        }
        if (uiEvent.value != StartUpEvent.None) {
            if (uiEvent.value != StartUpEvent.HttpResponse.Invalid401) {
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