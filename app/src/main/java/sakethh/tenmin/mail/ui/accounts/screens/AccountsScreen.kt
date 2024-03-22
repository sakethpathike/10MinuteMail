package sakethh.tenmin.mail.ui.accounts.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sakethh.tenmin.mail.ui.accounts.StartUpEvent
import sakethh.tenmin.mail.ui.accounts.viewmodels.AccountVM
import sakethh.tenmin.mail.ui.common.AccountItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    accountVM: AccountVM = hiltViewModel(),
    navController: NavController,
    mainNavController: NavController
) {
    val lazyListState = rememberLazyListState()
    val currentSessionData = accountVM.currentSessionData.collectAsState().value
    val allAccountsExcludingCurrentSessionData =
        accountVM.allAccountsExcludingCurrentSessionData.collectAsState().value
    LaunchedEffect(key1 = true) {
        accountVM.uiEvent.collect {
            when (it) {
                is StartUpEvent.Navigate -> mainNavController.navigate(it.navigationRoute)

                else -> Unit
            }
        }
    }
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(expanded = remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }.value != 0 && !lazyListState.isScrollInProgress,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a new email address icon"
                )
            },
            text = {
                Text(
                    text = "Add a new email account",
                    style = MaterialTheme.typography.titleSmall
                )
            }, onClick = {
                accountVM.onUIEvent(AccountsUiEvent.AddANewEmailAccount)
            })
    }, floatingActionButtonPosition = FabPosition.End, topBar = {
        CenterAlignedTopAppBar(navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigation Icon To Home Screen"
                )
            }
        }, title = {
            Text(
                text = "10 Minute Mail",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )
        })
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = lazyListState
        ) {
            item {
                Text(
                    text = "Current Session", style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(start = 15.dp, end = 15.dp)
                )
            }
            item {
                CurrentSessionItem(currentSessionData.mailAddress, currentSessionData.mailPassword)
                Spacer(modifier = Modifier.height(5.dp))
                FilledTonalButton(onClick = {
                    accountVM.onUIEvent(AccountsUiEvent.SignOut)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        text = "Sign out",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ), onClick = {
                        accountVM.onUIEvent(AccountsUiEvent.DeleteCurrentSessionAccountPermanently)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        text = "Delete account permanently",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1
                    )
                }
            }
            item {
                Text(
                    text = "Other Accounts",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 20.dp, start = 15.dp, end = 15.dp)
                )
            }

            items(allAccountsExcludingCurrentSessionData) {
                AccountItem(it.mailAddress, it.mailId, onAccountClick = {
                    accountVM.onUIEvent(AccountsUiEvent.SwitchAccount(it))
                })
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}