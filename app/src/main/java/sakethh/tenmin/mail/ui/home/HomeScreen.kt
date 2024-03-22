package sakethh.tenmin.mail.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.accounts.screens.AccountsScreen
import sakethh.tenmin.mail.ui.inbox.InboxScreen
import sakethh.tenmin.mail.ui.info.InfoScreen
import sakethh.tenmin.mail.ui.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(mainNavController: NavController) {
    val navController = rememberNavController()
    val topAppBarState = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val modalNavigationBarState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination
    NavigationDrawer(navController, modalNavigationBarState = modalNavigationBarState) {
        Scaffold(modifier = Modifier
            .padding(top = 8.dp)
            .nestedScroll(topAppBarState.nestedScrollConnection), topBar = {
            Box(modifier = Modifier.animateContentSize()) {
                if (currentRoute?.route == NavigationRoutes.INBOX.name) {
                    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.surface),
                        scrollBehavior = topAppBarState,
                        title = {
                            SearchBar(trailingIcon = {
                                IconButton(onClick = {
                                    navController.navigate(NavigationRoutes.ACCOUNTS.name)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Accounts"
                                    )
                                }
                            },
                                placeholder = {
                                    Text(
                                        text = "Search in mail",
                                        textAlign = TextAlign.End,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                                leadingIcon = {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            modalNavigationBarState.animateTo(
                                                DrawerValue.Open, tween(300)
                                            )
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .padding(
                                        start = 5.dp, end = 20.dp, bottom = 15.dp
                                    )
                                    .fillMaxWidth(),
                                query = "",
                                onQueryChange = {},
                                onSearch = {},
                                active = false,
                                onActiveChange = {}) {

                            }
                        })
                }
            }
        }) {
            NavHost(
                navController = navController, startDestination = NavigationRoutes.INBOX.name,
                modifier = Modifier.padding(it)
            ) {
                composable(NavigationRoutes.INBOX.name) {
                    InboxScreen()
                }
                composable(NavigationRoutes.ABOUT.name) {
                    InfoScreen(navController)
                }
                composable(NavigationRoutes.ACCOUNTS.name) {
                    AccountsScreen(
                        navController = navController,
                        mainNavController = mainNavController
                    )
                }
                composable(NavigationRoutes.SETTINGS.name) {
                    SettingsScreen(navController)
                }
            }
        }
    }
}