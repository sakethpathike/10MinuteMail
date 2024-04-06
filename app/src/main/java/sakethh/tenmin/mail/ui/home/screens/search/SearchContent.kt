package sakethh.tenmin.mail.ui.home.screens.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.common.AccountItem
import sakethh.tenmin.mail.ui.common.pulsateEffect
import sakethh.tenmin.mail.ui.home.NavigationDrawerModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(searchQuery: MutableState<String>, navController: NavController) {
    val selectedSearchFilters = remember {
        mutableStateListOf("")
    }
    val modalBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val shouldModalBtmSheetBeVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val isLabelsSelected = rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val dateRangePickerState = rememberDateRangePickerState()
    val isDateRangePickerVisible = rememberSaveable {
        mutableStateOf(false)
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        stickyHeader {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(SearchBarDefaults.colors().containerColor)
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 2.dp)
                ) {
                    listOf(
                        "Labels",
                        "From",
                        "Attachments",
                        "Date"
                    ).forEach {
                        /* if (it == "Saved Links" && queriedSavedLinks.isNotEmpty() || it == "Important Links" && impLinksQueriedData.isNotEmpty() ||
                             it == "Archived Links" && archiveLinksQueriedData.isNotEmpty() || it == "Folders" && queriedUnarchivedFoldersData.isNotEmpty()
                             || it == "Archived Folders" && queriedArchivedFoldersData.isNotEmpty() || it == "Links from folders" && queriedFolderLinks.isNotEmpty()
                             || it == "History" && historyLinksQueriedData.isNotEmpty()
                         ) {*/
                        Row(modifier = Modifier.animateContentSize()) {
                            Spacer(modifier = Modifier.width(10.dp))
                            androidx.compose.material3.FilterChip(
                                selected = selectedSearchFilters.contains(it),
                                onClick = {
                                    if (selectedSearchFilters.contains(it)) {
                                        selectedSearchFilters.remove(it)
                                    } else {
                                        selectedSearchFilters.add(it)
                                    }
                                    isLabelsSelected.value = it == "Labels"
                                    if (it == "Labels" || it == "From") {
                                        shouldModalBtmSheetBeVisible.value = true
                                    }
                                    if (it == "Date") {
                                        isDateRangePickerVisible.value = true
                                    }
                                },
                                label = {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }, leadingIcon = {
                                    Icon(
                                        imageVector =
                                        if (selectedSearchFilters.contains(it)) {
                                            Icons.Default.Check
                                        } else {
                                            when (it) {
                                                "Attachments" -> Icons.Default.Attachment
                                                "Date" -> Icons.Default.CalendarMonth
                                                else -> Icons.Default.ArrowDropDown
                                            }
                                        },
                                        contentDescription = null
                                    )
                                })
                        }
                        /* }*/
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
        }
    }
    if (shouldModalBtmSheetBeVisible.value) {
        ModalBottomSheet(
            dragHandle = {
                if (isLabelsSelected.value) {
                    BottomSheetDefaults.DragHandle()
                }
            },
            sheetState = modalBtmSheetState,
            modifier = Modifier
                .fillMaxWidth(),
            onDismissRequest = {
                coroutineScope.launch {
                    modalBtmSheetState.hide()
                }.invokeOnCompletion {
                    shouldModalBtmSheetBeVisible.value = false
                }
            }) {
            if (isLabelsSelected.value) {
                Text(
                    text = "Select a Label",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
            if (isLabelsSelected.value) {
                Spacer(modifier = Modifier.height(15.dp))
                currentSessionList.plus(overAllList).forEach {
                    if (it.itemName == "All Inboxes") {
                        Spacer(modifier = Modifier.height(15.dp))
                        HorizontalDivider()
                        Text(
                            text = "All Sessions",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 15.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            }
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = it.nonSelectedIcon, contentDescription = null)
                            Spacer(modifier = Modifier.width(25.dp))
                            Text(
                                text = it.itemName,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Checkbox(checked = false, onCheckedChange = {

                        })
                    }
                }
            } else {
                Scaffold(floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(25.dp))
                                .fillMaxWidth()
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {})
                                .background(MaterialTheme.colorScheme.surfaceBright)
                                .align(Alignment.TopCenter)
                        ) {
                            Text(
                                modifier = Modifier.padding(15.dp),
                                text = "Apply Filter",
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(ButtonDefaults.MinHeight)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                        ) {
                            Spacer(modifier = Modifier.height(45.dp))
                            Button(modifier = Modifier.fillMaxWidth(), onClick = { }) {
                                Text(
                                    text = "Apply Filter",
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                }) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        item {
                            SearchBar(trailingIcon = {
                                IconButton(onClick = {
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = Icons.Default.Cancel.name + " icon to clear the search query or to disable search mode if query is empty."
                                    )
                                }
                            },
                                placeholder = {
                                    Text(
                                        text = "Search Sender(s)",
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.basicMarquee(),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                                leadingIcon = {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = Icons.Default.AccountCircle.name + " Icon"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                    ),
                                query = "",
                                onQueryChange = {
                                    searchQuery.value = it
                                },
                                onSearch = {},
                                active = false,
                                onActiveChange = {

                                },
                                content = {

                                })
                        }
                        items(20) {
                            AccountItem(
                                emailAddress = it.toString(),
                                emailId = it.toString(),
                                onAccountClick = {}, selected = mutableStateOf(true)
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(200.dp))
                        }
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 15.dp)
                    .pulsateEffect { }, onClick = { }) {
                Text(
                    text = "Apply Changes",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
    if (isDateRangePickerVisible.value) {
        DatePickerDialog(
            modifier = Modifier.scale(0.9f),
            onDismissRequest = { isDateRangePickerVisible.value = false },
            dismissButton = {
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .pulsateEffect { }, onClick = { }) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            },
            confirmButton = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .pulsateEffect { }, onClick = { }) {
                    Text(
                        text = "Apply Changes",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }) {
            DateRangePicker(state = dateRangePickerState, title = {
                Text(
                    text = "By Date",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp),
                    fontSize = 14.sp
                )
            }, headline = {
                Text(
                    text = "To find emails you received within a specific timeframe, choose the date range you want to search.",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(
                        bottom = 20.dp,
                        start = 20.dp,
                        end = 20.dp,
                        top = 5.dp
                    ),
                    fontSize = 16.sp
                )
            })
        }
    }
}