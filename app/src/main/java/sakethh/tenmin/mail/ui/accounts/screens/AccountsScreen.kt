package sakethh.tenmin.mail.ui.accounts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sakethh.tenmin.mail.ui.common.AccountItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen() {
    val lazyListState = rememberLazyListState()
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(expanded = remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }.value != 0 && !lazyListState.isScrollInProgress,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Generate a new email address icon"
                )
            },
            text = {
                Text(
                    text = "Generate a new email address",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            onClick = { })
    }, floatingActionButtonPosition = FabPosition.End, topBar = {
        CenterAlignedTopAppBar(navigationIcon = {
            IconButton(onClick = { }) {
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
                .padding(it)
                .padding(start = 15.dp, end = 15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            state = lazyListState
        ) {
            item {
                Text(
                    text = "Current Session", style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            item {
                AccountItem()
            }
            item {
                FilledTonalButton(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Text(
                        text = "Delete Account", style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
            item {
                Text(text = "Other Accounts", style = MaterialTheme.typography.titleSmall)
            }

            items(20) {
                AccountItem()
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}