package sakethh.tenmin.mail.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.home.screens.childHomeScreen.ChildHomeScreenVM

@Composable
fun NavigationDrawer(
    navController: NavController,
    modalNavigationBarState: DrawerState,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val selectedItem = rememberSaveable {
        mutableStateOf("Inbox")
    }
    val currentSessionList = remember {
        listOf(
            NavigationDrawerModel(
                itemName = "Inbox",
                selectedIcon = Icons.Filled.Inbox,
                nonSelectedIcon = Icons.Outlined.Inbox,
                navigationRoute = NavigationRoutes.INBOX.name
            ),
            NavigationDrawerModel(
                itemName = "Starred",
                selectedIcon = Icons.Filled.Star,
                nonSelectedIcon = Icons.Outlined.StarOutline,
                navigationRoute = NavigationRoutes.STARRED.name
            ),
            NavigationDrawerModel(
                itemName = "Archive",
                selectedIcon = Icons.Filled.Archive,
                nonSelectedIcon = Icons.Outlined.Archive,
                navigationRoute = NavigationRoutes.ARCHIVE.name
            ),
            NavigationDrawerModel(
                itemName = "Trash",
                selectedIcon = Icons.Filled.Delete,
                nonSelectedIcon = Icons.Outlined.Delete,
                navigationRoute = NavigationRoutes.TRASH.name
            ),
        )
    }
    val overAllList = remember {
        listOf(
            NavigationDrawerModel(
                itemName = "All Inboxes",
                selectedIcon = Icons.Filled.AllInbox,
                nonSelectedIcon = Icons.Outlined.AllInbox,
                navigationRoute = NavigationRoutes.ALL_INBOXES.name
            ),
            NavigationDrawerModel(
                itemName = "All Starred",
                selectedIcon = Icons.Filled.Grade,
                nonSelectedIcon = Icons.Outlined.Grade,
                navigationRoute = NavigationRoutes.ALL_STARRED.name
            ),
            NavigationDrawerModel(
                itemName = "All Archives",
                selectedIcon = Icons.Filled.Archive,
                nonSelectedIcon = Icons.Outlined.Archive,
                navigationRoute = NavigationRoutes.ALL_ARCHIVES.name
            ),
            NavigationDrawerModel(
                itemName = "All Trashed",
                selectedIcon = Icons.Filled.Delete,
                nonSelectedIcon = Icons.Outlined.Delete,
                navigationRoute = NavigationRoutes.ALL_TRASHED.name
            ),
        )
    }
    ModalNavigationDrawer(gesturesEnabled = true,
        drawerState = modalNavigationBarState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "10 Minute Mail",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(15.dp)
                )
                Divider()
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Current Session",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 15.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                currentSessionList.forEach {
                    NavigationDrawerItem(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 25.dp,
                            bottomEnd = 25.dp
                        ), icon = {
                            Icon(
                                imageVector = if (selectedItem.value == it.itemName) it.selectedIcon else it.nonSelectedIcon,
                                contentDescription = "${it.itemName} item to navigate to its screen"
                            )
                        }, label = {
                            Text(
                                text = it.itemName,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 15.dp)
                            )
                        }, selected = selectedItem.value == it.itemName, onClick = {
                            selectedItem.value = it.itemName
                            coroutineScope.launch {
                                awaitAll(
                                    async {
                                        ChildHomeScreenVM.currentChildHomeScreenType.value =
                                            NavigationRoutes.valueOf(it.navigationRoute)
                                    },
                                    async {
                                        modalNavigationBarState.close()
                                    })
                            }
                        })
                }
                Spacer(modifier = Modifier.height(15.dp))
                Divider()
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "All Sessions",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 15.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                overAllList.forEach {
                    NavigationDrawerItem(modifier = Modifier.fillMaxWidth(0.95f),
                        shape = RoundedCornerShape(
                            topStart = 0.dp, bottomStart = 0.dp, topEnd = 25.dp, bottomEnd = 25.dp
                        ),
                        icon = {
                            Icon(
                                imageVector = if (selectedItem.value == it.itemName) it.selectedIcon else it.nonSelectedIcon,
                                contentDescription = "${it.itemName} item to navigate to its screen"
                            )
                        },
                        label = {
                            Text(
                                text = it.itemName,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 15.dp)
                            )
                        },
                        selected = selectedItem.value == it.itemName,
                        onClick = {
                            selectedItem.value = it.itemName
                            coroutineScope.launch {
                                awaitAll(
                                    async {
                                        ChildHomeScreenVM.currentChildHomeScreenType.value =
                                            NavigationRoutes.valueOf(it.navigationRoute)
                                    },
                                    async {
                                        modalNavigationBarState.close()
                                    })
                            }
                        })
                }
                Spacer(modifier = Modifier.height(15.dp))
                Divider()
                Spacer(modifier = Modifier.height(15.dp))
                NavigationDrawerItem(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(
                        topStart = 0.dp, bottomStart = 0.dp, topEnd = 25.dp, bottomEnd = 25.dp
                    ), icon = {
                        Icon(
                            imageVector = if (selectedItem.value != "Settings") Icons.Outlined.Settings else Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }, label = {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }, selected = selectedItem.value == "Settings", onClick = {
                        selectedItem.value = "Settings"
                        coroutineScope.launch {
                            awaitAll(async { navController.navigate(NavigationRoutes.SETTINGS.name) },
                                async {
                                    modalNavigationBarState.close()
                                })
                        }
                    })
                NavigationDrawerItem(modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 25.dp,
                        bottomEnd = 25.dp
                    ), icon = {
                        Icon(
                            imageVector = if (selectedItem.value != "Info") Icons.Outlined.Info else Icons.Filled.Info,
                            contentDescription = "Info"
                        )
                    }, label = {
                        Text(
                            text = "Info",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }, selected = selectedItem.value == "Info", onClick = {
                        selectedItem.value = "Info"
                        coroutineScope.launch {
                            awaitAll(
                                async { navController.navigate(NavigationRoutes.INFO.name) },
                                async {
                                    modalNavigationBarState.close()
                                })
                        }
                    })
                Spacer(modifier = Modifier.height(15.dp))
            }
        }) {
        content()
    }
}