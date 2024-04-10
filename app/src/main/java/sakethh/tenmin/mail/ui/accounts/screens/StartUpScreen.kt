package sakethh.tenmin.mail.ui.accounts.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.channels.consumeEach
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.accounts.AccountsEvent
import sakethh.tenmin.mail.ui.accounts.viewmodels.StartUpVM
import sakethh.tenmin.mail.ui.common.AccountItem
import sakethh.tenmin.mail.ui.settings.SettingsScreenVM

@Composable
fun StartUpScreen(
    navController: NavController,
    startUpVM: StartUpVM = hiltViewModel()
) {
    val checkingForActiveSession = rememberSaveable {
        mutableStateOf(true)
    }
    val existingAccountsData = startUpVM.existingAccountsData.collectAsState().value
    val isAccountsExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        startUpVM.uiEvent.consumeEach {
            when (it) {
                is AccountsEvent.CheckingIfAnySessionAlreadyExists -> checkingForActiveSession.value =
                    true

                is AccountsEvent.Navigate -> navController.navigate(it.navigationRoute) {
                    popUpTo(0)
                }

                is AccountsEvent.None -> checkingForActiveSession.value = false
                else -> Unit
            }
        }
    }
    Box(
        modifier = Modifier
            .background(if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn {
            if (!checkingForActiveSession.value) {
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StartUpComponent(
                            uiEvent = startUpVM.uiEventAsFlow.collectAsState(initial = AccountsEvent.None),
                            navController = navController,
                            onGenerateANewAccountClick = {
                                startUpVM.onUiClickEvent(AccountsUiEvent.GenerateANewTemporaryMailAccount)
                            })
                    }
                }
            } else {
                item {
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

            if (existingAccountsData.isNotEmpty() && !checkingForActiveSession.value) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isAccountsExpanded.value = !isAccountsExpanded.value },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sign in with previously logged-in accounts",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth(0.75f)
                        )
                        IconButton(onClick = {
                            isAccountsExpanded.value = !isAccountsExpanded.value
                        }) {
                            Icon(
                                imageVector = if (isAccountsExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Icons for expanding accounts that are either existing or previously used but not deleted."
                            )
                        }
                    }
                }
                items(existingAccountsData) {
                    Box(modifier = Modifier.animateContentSize()) {
                        if (isAccountsExpanded.value) {
                            AccountItem(it.accountAddress, it.accountId, onAccountClick = {
                                startUpVM.onUiClickEvent(
                                    AccountsUiEvent.LoginUsingALocallyExistingAccount(
                                        it
                                    )
                                )
                            })
                        }
                    }
                }
            }

        }
        val context = LocalContext.current
        BackHandler {
            if (!StartUpVM.isNavigatingFromAccountsScreenForANewAccountCreation) {
                navController.backQueue.removeIf {
                    true
                }
                val activity = context as Activity
                activity.moveTaskToBack(false)
            } else {
                navController.popBackStack()
            }
        }
    }
}

@Composable
private fun StartUpComponent(
    uiEvent: State<AccountsEvent>,
    navController: NavController,
    onGenerateANewAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .animateContentSize(),
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
        if (uiEvent.value == AccountsEvent.None || uiEvent.value == AccountsEvent.HttpResponse.Invalid401) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                onGenerateANewAccountClick()
            }) {
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
        }
        if (uiEvent.value != AccountsEvent.None) {
            if (uiEvent.value != AccountsEvent.HttpResponse.Invalid401) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            Text(
                text = "Status", style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = uiEvent.value.toString(),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(modifier = Modifier)
    }
}