package sakethh.tenmin.mail.ui.accounts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.accounts.StartUpEvent
import sakethh.tenmin.mail.ui.accounts.viewmodels.StartUpVM

@Composable
fun StartUpScreen(navController: NavController, startUpVM: StartUpVM = hiltViewModel()) {
    val checkingForActiveSession = rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = true) {
        startUpVM.uiEvent.collect {
            when (it) {
                is StartUpEvent.CheckingIfAnySessionAlreadyExists -> checkingForActiveSession.value =
                    true

                is StartUpEvent.Navigate -> navController.navigate(it.navigationRoute)
                is StartUpEvent.None -> checkingForActiveSession.value = false
                else -> Unit
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        if (!checkingForActiveSession.value) {
            StartUpComponent(navController = navController)
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp)
            ) {
                Text(
                    text = "10 Minute Mail",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    fontSize = 26.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Checking for an existing local active session",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
private fun StartUpComponent(navController: NavController) {
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
                    text = "Generate a temporary email account",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            FilledTonalButton(modifier = Modifier.fillMaxWidth(), onClick = {
                navController.navigate(NavigationRoutes.SIGN_IN.name)
            }) {
                Text(
                    text = "Sign in", style = MaterialTheme.typography.titleSmall
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