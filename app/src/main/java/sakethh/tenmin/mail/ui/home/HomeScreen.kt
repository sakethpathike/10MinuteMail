package sakethh.tenmin.mail.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.accounts.screens.AccountsScreen
import sakethh.tenmin.mail.ui.home.screens.childHomeScreen.ChildHomeScreen
import sakethh.tenmin.mail.ui.home.screens.childHomeScreen.ChildHomeScreenVM
import sakethh.tenmin.mail.ui.home.screens.search.SearchContent
import sakethh.tenmin.mail.ui.home.screens.search.SearchContentVM
import sakethh.tenmin.mail.ui.settings.SettingsScreen
import sakethh.tenmin.mail.ui.settings.SpecificSettingsScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    mainNavController: NavController,
    childHomeScreenVM: ChildHomeScreenVM = hiltViewModel()
) {
    val navController = rememberNavController()
    val modalNavigationBarState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination
    val nonHomeScreens = remember {
        listOf(
            NavigationRoutes.INFO.name,
            NavigationRoutes.SETTINGS.name,
            NavigationRoutes.ACCOUNTS.name,
            NavigationRoutes.SPECIFIC_SETTINGS.name
        )
    }
    val currentSessionData = childHomeScreenVM.currentSessionData.collectAsState().value
    val pullRefreshState = rememberPullToRefreshState()
    val searchQuery = rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
        if (currentSessionData.isDeletedFromTheCloud) {
            pullRefreshState.endRefresh()
            return@LaunchedEffect
        }
        if (/*pullRefreshState.isRefreshing*/false) {
            childHomeScreenVM.loadMailsFromTheCloud(isRefreshing = true, onLoadingComplete = {
                pullRefreshState.endRefresh()
            })
        }
    }
    NavigationDrawer(navController, modalNavigationBarState = modalNavigationBarState) {
        Box(modifier = Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)) {
            PullToRefreshContainer(
                state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter)
            )
            Column {
                Box(modifier = Modifier.animateContentSize()) {
                    if (!nonHomeScreens.contains(currentRoute?.route)) {
                        SearchBar(trailingIcon = {
                            IconButton(onClick = {
                                if (SearchContentVM.isSearchEnabled.value) {
                                    if (searchQuery.value.isNotEmpty()) {
                                        searchQuery.value = ""
                                    } else {
                                        SearchContentVM.isSearchEnabled.value = false
                                    }
                                } else {
                                    navController.navigate(NavigationRoutes.ACCOUNTS.name)
                                }
                            }) {
                                Icon(
                                    imageVector = if (!SearchContentVM.isSearchEnabled.value) Icons.Default.AccountCircle else Icons.Default.Cancel,
                                    contentDescription = if (!SearchContentVM.isSearchEnabled.value) Icons.Default.AccountCircle.name + " icon for navigating to accounts screen" else Icons.Default.Cancel.name + " icon to clear the search query or to disable search mode if query is empty."
                                )
                            }
                        },
                            placeholder = {
                                Text(
                                    text = "Search in 10MinuteMail",
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.basicMarquee(),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            },
                            leadingIcon = {
                                IconButton(onClick = {
                                    if (!SearchContentVM.isSearchEnabled.value) {
                                        coroutineScope.launch {
                                            modalNavigationBarState.open()
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (SearchContentVM.isSearchEnabled.value) Icons.Default.Search else Icons.Default.Menu,
                                        contentDescription = if (SearchContentVM.isSearchEnabled.value) Icons.Default.Search.name + " Icon for representing search mode is enabled" else Icons.Default.Menu.name + " Icon which will open the navigation drawer from the left side"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = if (SearchContentVM.isSearchEnabled.value) 0.dp else 15.dp,
                                    end = if (SearchContentVM.isSearchEnabled.value) 0.dp else 15.dp,
                                    bottom = if (SearchContentVM.isSearchEnabled.value) 0.dp else 15.dp,
                                    top = if (SearchContentVM.isSearchEnabled.value) 0.dp else 5.dp
                                ),
                            query = searchQuery.value,
                            onQueryChange = {
                                searchQuery.value = it
                            },
                            onSearch = {},
                            active = SearchContentVM.isSearchEnabled.value,
                            onActiveChange = {
                                SearchContentVM.isSearchEnabled.value =
                                    !SearchContentVM.isSearchEnabled.value
                            },
                            content = {
                                SearchContent(navController = navController)
                            })
                            }
                }

                NavHost(
                    navController = navController, startDestination = NavigationRoutes.INBOX.name,
                ) {
                    composable(NavigationRoutes.INBOX.name) {
                        ChildHomeScreen(childHomeScreenVM)
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
                    composable(NavigationRoutes.SPECIFIC_SETTINGS.name) {
                        SpecificSettingsScreen(navController)
                    }
                }
            }
        }
    }
}