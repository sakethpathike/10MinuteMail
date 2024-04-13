package sakethh.tenmin.mail.ui.home.screens.search

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
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
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.common.AccountItem
import sakethh.tenmin.mail.ui.common.MailItem
import sakethh.tenmin.mail.ui.common.pulsateEffect
import sakethh.tenmin.mail.ui.home.NavigationDrawerModel
import sakethh.tenmin.mail.ui.home.screens.childHomeScreen.ChildHomeScreenEvent
import sakethh.tenmin.mail.ui.home.screens.childHomeScreen.ChildHomeScreenVM
import sakethh.tenmin.mail.ui.settings.SettingsScreenVM

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SimpleDateFormat")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    searchQuery: MutableState<String>,
    navController: NavController,
    searchContentVM: SearchContentVM,
    childHomeScreenVM: ChildHomeScreenVM
) {
    val selectedLabelsFilter = searchContentVM.selectedLabelsFilter
    val modalBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val shouldModalBtmSheetBeVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val isLabelsSelected = rememberSaveable {
        mutableStateOf(false)
    }
    val queriedMails = searchContentVM.searchResults.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val dateRangePickerState = rememberDateRangePickerState()
    val isDateRangePickerVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val receivedMailsSenders = searchContentVM.receivedMailsSenders.collectAsState()
    val sendersSearchQuery = rememberSaveable {
        mutableStateOf("")
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
    val draggedLeft = remember {
        mutableStateOf(false)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface)
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
                        Row(modifier = Modifier.animateContentSize()) {
                            Spacer(modifier = Modifier.width(10.dp))
                            androidx.compose.material3.FilterChip(
                                selected = when (it) {
                                    "Attachments" -> searchContentVM.hasAttachments.collectAsState().value
                                    "Labels" -> searchContentVM.selectedLabelsFilter.isNotEmpty()
                                    "From" -> searchContentVM.selectedFromAccountsFilter.isNotEmpty()
                                    "Date" -> dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
                                    else -> false
                                },
                                onClick = {
                                    if (it == "Attachments") {
                                        searchContentVM.onUiEvent(
                                            SearchUiEvent.ChangeAttachmentsSelectionState(
                                                !searchContentVM.hasAttachments.value
                                            )
                                        )
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
                                        if (selectedLabelsFilter.contains(it)) {
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
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            val totalAppliedFilters = rememberSaveable(
                dateRangePickerState.selectedStartDateMillis,
                dateRangePickerState.selectedEndDateMillis,
                searchContentVM.hasAttachments.value,
                searchContentVM.selectedLabelsFilter.size,
                searchContentVM.selectedFromAccountsFilter.size
            ) {
                mutableStateListOf(
                    searchContentVM.selectedLabelsFilter.size > 0,
                    searchContentVM.selectedFromAccountsFilter.size > 0,
                    searchContentVM.hasAttachments.value,
                    dateRangePickerState.selectedEndDateMillis != null && dateRangePickerState.selectedStartDateMillis != null
                ).filter {
                    it
                }.toList().size
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Applied ")
                        append("$totalAppliedFilters ")
                        if (totalAppliedFilters == 1) append("Filter") else append("Filters")
                    }
                    if (totalAppliedFilters > 0) append(": ")
                    if (searchContentVM.selectedLabelsFilter.size > 0) withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Labels")
                    }
                    if (searchContentVM.selectedLabelsFilter.size > 0 && searchContentVM.selectedFromAccountsFilter.size > 0) append(
                        ", "
                    )
                    if (searchContentVM.selectedFromAccountsFilter.size > 0) withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("From")
                    }
                    if (searchContentVM.hasAttachments.value && (searchContentVM.selectedLabelsFilter.size > 0 || (searchContentVM.selectedFromAccountsFilter.size > 0))) append(
                        ", "
                    )
                    if (searchContentVM.hasAttachments.value) withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Attachments")
                    }
                    if ((searchContentVM.hasAttachments.value || searchContentVM.selectedLabelsFilter.size > 0 || (searchContentVM.selectedFromAccountsFilter.size > 0)) && (dateRangePickerState.selectedEndDateMillis != null && dateRangePickerState.selectedStartDateMillis != null)) append(
                        ", "
                    )

                    if (dateRangePickerState.selectedEndDateMillis != null && dateRangePickerState.selectedStartDateMillis != null) withStyle(
                        SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append("Date Range")
                    }
                },
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(start = 10.dp, end = 15.dp)
                    .fillMaxWidth()
            )
        }
        items(queriedMails.value) {
            MailItem(
                intro = it.intro,
                createdAt = it.createdAt,
                subject = it.subject,
                fromName = it.from.name,
                onDragRight = {
                    draggedLeft.value = false
                    when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                        NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name -> {
                            childHomeScreenVM.onUiEvent(
                                ChildHomeScreenEvent.MoveToArchive(it.mailId)
                            )
                            childHomeScreenVM.onUiEvent(
                                ChildHomeScreenEvent.RemoveFromInbox(
                                    it.mailId
                                )
                            )
                        }

                        NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name -> childHomeScreenVM.onUiEvent(
                            ChildHomeScreenEvent.UnMarkStarredMail(it.mailId)
                        )

                        NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name -> childHomeScreenVM.onUiEvent(
                            ChildHomeScreenEvent.RemoveFromArchive(it.mailId)
                        )

                        NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> childHomeScreenVM.onUiEvent(
                            ChildHomeScreenEvent.RemoveFromTrash(it.mailId)
                        )

                        else -> ChildHomeScreenEvent.None
                    }
                },
                onDragLeft = {
                    draggedLeft.value = true
                    when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                        NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name -> {
                            childHomeScreenVM.onUiEvent(
                                ChildHomeScreenEvent.MoveToTrash(it.mailId)
                            )
                            childHomeScreenVM.onUiEvent(
                                ChildHomeScreenEvent.RemoveFromInbox(
                                    it.mailId
                                )
                            )
                        }

                        NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name -> childHomeScreenVM.onUiEvent(
                            ChildHomeScreenEvent.UnMarkStarredMail(it.mailId)
                        )

                        NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name -> childHomeScreenVM.onUiEvent(
                            ChildHomeScreenEvent.RemoveFromArchive(it.mailId)
                        )

                        NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> childHomeScreenVM.onUiEvent(
                            ChildHomeScreenEvent.RemoveFromTrash(it.mailId)
                        )

                        else -> ChildHomeScreenEvent.None
                    }
                },
                isStarred = rememberSaveable(it.isStarred) {
                    mutableStateOf(it.isStarred)
                },
                onStarClick = {
                    childHomeScreenVM.onUiEvent(ChildHomeScreenEvent.OnStarIconClick(it.mailId))
                },
                draggedLeftColor = when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                    NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name -> MaterialTheme.colorScheme.primaryContainer
                    NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name, NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name -> MaterialTheme.colorScheme.outlineVariant
                    NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> MaterialTheme.colorScheme.errorContainer
                    else -> Color.Transparent
                },
                draggedRightColor = when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                    NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name -> MaterialTheme.colorScheme.errorContainer
                    NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name, NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name -> MaterialTheme.colorScheme.outlineVariant
                    NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> MaterialTheme.colorScheme.errorContainer
                    else -> Color.Transparent
                },
                draggedRightIcon = when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                    NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name -> Icons.Default.StarBorder
                    NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name, NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name, NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> Icons.Default.Delete
                    else -> Icons.Default.Abc
                },
                draggedRightText = when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                    NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name -> " Move to\nTrash"
                    NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name -> "Remove from\nStarred"
                    NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name -> "Remove from\nArchive"
                    NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> "Delete\npermanently"
                    else -> ""
                },
                draggedLeftIcon = when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                    NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name -> Icons.Default.Archive
                    NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name -> Icons.Default.StarBorder
                    NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name, NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> Icons.Default.Delete
                    else -> Icons.Default.Abc

                },
                draggedLeftText = when (ChildHomeScreenVM.currentChildHomeScreenType.value.name) {
                    NavigationRoutes.INBOX.name, NavigationRoutes.ALL_INBOXES.name -> "Move to\nArchive"
                    NavigationRoutes.STARRED.name, NavigationRoutes.ALL_STARRED.name -> "Remove from\nStarred"
                    NavigationRoutes.ARCHIVE.name, NavigationRoutes.ALL_ARCHIVES.name -> "Remove from\nArchive"
                    NavigationRoutes.TRASH.name, NavigationRoutes.ALL_TRASHED.name -> "Delete\npermanently"
                    else -> ""
                },
                shouldStarIconVisible = ChildHomeScreenVM.currentChildHomeScreenType.value != NavigationRoutes.TRASH || ChildHomeScreenVM.currentChildHomeScreenType.value != NavigationRoutes.ALL_TRASHED,
            )
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
                currentSessionList.plus(overAllList).forEach { labelElement ->
                    if (labelElement.itemName == "All Inboxes") {
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
                                if (selectedLabelsFilter.contains(labelElement.itemName)) {
                                    searchContentVM.onUiEvent(
                                        SearchUiEvent.RemoveALabelFilter(
                                            labelElement.itemName
                                        )
                                    )
                                } else {
                                    searchContentVM.onUiEvent(
                                        SearchUiEvent.AddANewLabelFilter(
                                            labelElement.itemName
                                        )
                                    )
                                }
                            }
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = labelElement.nonSelectedIcon,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(25.dp))
                            Text(
                                text = labelElement.itemName,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Checkbox(
                            checked = selectedLabelsFilter.contains(labelElement.itemName),
                            onCheckedChange = {
                                if (!it) {
                                    searchContentVM.onUiEvent(
                                        SearchUiEvent.RemoveALabelFilter(
                                            labelElement.itemName
                                        )
                                    )
                                } else {
                                    searchContentVM.onUiEvent(
                                        SearchUiEvent.AddANewLabelFilter(
                                            labelElement.itemName
                                        )
                                    )
                                }
                        })
                    }
                }
            } else {
                Scaffold(floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(25.dp))
                                .fillMaxWidth()
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {})
                                .background(MaterialTheme.colorScheme.surfaceBright)
                        ) {
                            Text(
                                modifier = Modifier.padding(15.dp),
                                text = buildAnnotatedString {
                                    append("Results are filtered based on your ")
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("${searchContentVM.selectedFromAccountsFilter.size} selected accounts")
                                    }
                                    append(".")
                                },
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        FilledTonalButton(modifier = Modifier.fillMaxWidth(), onClick = {
                            coroutineScope.launch {
                                modalBtmSheetState.hide()
                            }.invokeOnCompletion {
                                shouldModalBtmSheetBeVisible.value = false
                            }
                        }) {
                                Text(
                                    text = "Close",
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                    }
                }) {
                    LazyColumn(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxSize()
                            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.Black, Color.Black, Color.Transparent
                                        )
                                    ), blendMode = BlendMode.DstIn
                                )
                            }
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(15.dp))
                            SearchBar(trailingIcon = {
                                if (sendersSearchQuery.value.isNotEmpty()) {
                                    IconButton(onClick = {
                                        sendersSearchQuery.value = ""
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Cancel,
                                            contentDescription = Icons.Default.Cancel.name + " icon to clear the search query or to disable search mode if query is empty."
                                        )
                                    }
                                }
                            },
                                placeholder = {
                                    Text(
                                        text = "Search Sender",
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
                                query = sendersSearchQuery.value,
                                onQueryChange = {
                                    sendersSearchQuery.value = it
                                },
                                onSearch = {},
                                active = false,
                                onActiveChange = {

                                },
                                content = {

                                })
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                        items(if (sendersSearchQuery.value.isEmpty()) receivedMailsSenders.value else receivedMailsSenders.value.filter {
                            it.name.lowercase().contains(
                                sendersSearchQuery.value.trim().lowercase()
                            )
                        }) {
                            AccountItem(
                                emailAddress = it.address,
                                emailId = it.name,
                                onAccountClick = {
                                    if (searchContentVM.selectedFromAccountsFilter.contains(it)) {
                                        searchContentVM.selectedFromAccountsFilter.remove(it)
                                    } else {
                                        searchContentVM.selectedFromAccountsFilter.add(it)
                                    }
                                },
                                selected = mutableStateOf(
                                    searchContentVM.selectedFromAccountsFilter.contains(
                                        it
                                    )
                                )
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
                    .pulsateEffect { }, onClick = {
                    coroutineScope.launch {
                        modalBtmSheetState.hide()
                    }.invokeOnCompletion {
                        shouldModalBtmSheetBeVisible.value = false
                    }
                }) {
                Text(
                    text = "Close",
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
                        .pulsateEffect { }, onClick = {
                        isDateRangePickerVisible.value = false
                    }) {
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
                        .pulsateEffect { }, onClick = {
                        if (dateRangePickerState.selectedStartDateMillis == null || dateRangePickerState.selectedEndDateMillis == null) {
                            Toast.makeText(
                                context,
                                "Start date and End date should not be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        searchContentVM.onUiEvent(
                            SearchUiEvent.ChangeDateRange(
                                startingDateUTC = dateRangePickerState.selectedStartDateMillis,
                                endingDateUTC = dateRangePickerState.selectedEndDateMillis
                            )
                        )
                    }) {
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
        LaunchedEffect(
            dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis
        ) {
            if (dateRangePickerState.displayMode == DisplayMode.Picker) {
                searchContentVM.onUiEvent(
                    SearchUiEvent.ChangeDateRange(
                        startingDateUTC = dateRangePickerState.selectedStartDateMillis,
                        endingDateUTC = dateRangePickerState.selectedEndDateMillis
                    )
                )
            }
        }
    LaunchedEffect(dateRangePickerState.selectedEndDateMillis) {
        if (dateRangePickerState.selectedEndDateMillis != null && dateRangePickerState.selectedStartDateMillis != null) {
            isDateRangePickerVisible.value = false
        }
    }
}