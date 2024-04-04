package sakethh.tenmin.mail.ui.home.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.accounts.components.AccountDeletedFromTheCloudCard
import sakethh.tenmin.mail.ui.common.MailItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildHomeScreen(
    childHomeScreenVM: ChildHomeScreenVM = hiltViewModel()
) {
    val currentSessionData = childHomeScreenVM.currentSessionData.collectAsState().value
    val isCurrentSessionMailExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    val currentSessionStarred = childHomeScreenVM.currentSessionStarred.collectAsState().value
    val currentSessionInbox = childHomeScreenVM.currentSessionInbox.collectAsState().value
    val currentSessionArchive = childHomeScreenVM.currentSessionArchive.collectAsState().value
    val currentSessionTrash = childHomeScreenVM.currentSessionTrash.collectAsState().value
    val allSessionsInbox = childHomeScreenVM.allSessionsInbox.collectAsState().value
    val allSessionsStarred = childHomeScreenVM.allSessionsStarred.collectAsState().value
    val allSessionsArchive = childHomeScreenVM.allSessionsArchive.collectAsState().value
    val allSessionsTrash = childHomeScreenVM.allSessionsTrash.collectAsState().value
    val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(key1 = pullRefreshState.isRefreshing) {
        if (currentSessionData.isDeletedFromTheCloud) {
            pullRefreshState.endRefresh()
            return@LaunchedEffect
        }
        if (pullRefreshState.isRefreshing) {
            childHomeScreenVM.loadMailsFromTheCloud(isRefreshing = true, onLoadingComplete = {
                pullRefreshState.endRefresh()
            })
        }
    }
    val draggedLeft = remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .clickable {
                        isCurrentSessionMailExpanded.value = !isCurrentSessionMailExpanded.value
                    }
                    .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = ChildHomeScreenVM.currentChildHomeScreenType.value.name.replace(
                            "_",
                            " "
                        ).split(" ").joinToString {
                            it.substring(0, 1).toUpperCase()
                                .plus(it.substring(1, it.length).toLowerCase())
                        }.replace(",", ""),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                    )
                    IconButton(onClick = {
                        isCurrentSessionMailExpanded.value = !isCurrentSessionMailExpanded.value
                    }) {
                        Icon(
                            imageVector = if (isCurrentSessionMailExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Icons for expanding or hiding the details of the current session's mail address."
                        )
                    }
                }
                Box(modifier = Modifier.animateContentSize()) {
                    if (isCurrentSessionMailExpanded.value) {
                        Text(
                            text = buildAnnotatedString {
                                append("Logged in as ")
                                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                    append(currentSessionData.accountAddress)
                                }
                            },
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            softWrap = true
                        )
                    }
                }
                if (currentSessionData.isDeletedFromTheCloud) {
                    AccountDeletedFromTheCloudCard(inInboxScreen = true)
                }
            }
            items(
                items = when (ChildHomeScreenVM.currentChildHomeScreenType.value) {
                    NavigationRoutes.INBOX -> currentSessionInbox
                    NavigationRoutes.STARRED -> currentSessionStarred
                    NavigationRoutes.ARCHIVE -> currentSessionArchive
                    NavigationRoutes.TRASH -> currentSessionTrash
                    NavigationRoutes.ALL_INBOXES -> allSessionsInbox
                    NavigationRoutes.ALL_STARRED -> allSessionsStarred
                    NavigationRoutes.ALL_ARCHIVES -> allSessionsArchive
                    NavigationRoutes.ALL_TRASHED -> allSessionsTrash
                    else -> emptyList()
                }, key = {
                it.id
            }) {
                AnimatedContent(transitionSpec = {
                    ContentTransform(
                        targetContentEnter = EnterTransition.None,
                        initialContentExit = slideOutHorizontally()
                    )
                }, targetState = it, label = "") {
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
                        }, onStarClick = {
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
        }
        PullToRefreshContainer(
            state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}