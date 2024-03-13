package sakethh.tenmin.mail.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sakethh.tenmin.mail.ui.inbox.InboxScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val topAppBarState = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = Modifier
        .padding(top = 8.dp)
        .nestedScroll(topAppBarState.nestedScrollConnection), topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.surface),
            scrollBehavior = topAppBarState,
            title = {
                SearchBar(
                    placeholder = {
                        Text(
                            text = "Search in mail",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    modifier = Modifier.padding(start = 5.dp, end = 20.dp, bottom = 15.dp),
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    active = false,
                    onActiveChange = {}) {

                }
            })
    }) {
        NavHost(
            navController = navController,
            startDestination = "start",
            modifier = Modifier.padding(it)
        ) {
            composable("start") {
                InboxScreen()
            }
        }
    }
}