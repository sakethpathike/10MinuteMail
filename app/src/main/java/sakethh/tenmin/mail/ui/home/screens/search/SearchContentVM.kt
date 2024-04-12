package sakethh.tenmin.mail.ui.home.screens.search

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.local.repo.accounts.LocalAccountsRepo
import sakethh.tenmin.mail.data.local.repo.mail.LocalMailRepo
import sakethh.tenmin.mail.data.remote.api.model.mail.From
import sakethh.tenmin.mail.ui.home.screens.search.model.SearchQueryFlow
import javax.inject.Inject

@HiltViewModel
class SearchContentVM @Inject constructor(
    private val localMailRepo: LocalMailRepo, private val localAccountsRepo: LocalAccountsRepo
) : ViewModel() {
    private val _selectedLabelsFilter = mutableStateListOf(
        "Inbox",
        "Starred",
        "Archive",
        "Trash",
        "All Inboxes",
        "All Starred",
        "All Archives",
        "All Trashed"
    )
    val selectedLabelsFilter = _selectedLabelsFilter

    private val _selectedFromAccountsFilter = mutableStateListOf<From>()
    val selectedFromAccountsFilter = _selectedFromAccountsFilter

    private val _hasAttachments = MutableStateFlow(false)
    val hasAttachments = _hasAttachments.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow(emptyList<LocalMail>())
    val searchResults = _searchResults.asStateFlow()

    private val _receivedMailsSenders = MutableStateFlow(emptyList<From>())
    val receivedMailsSenders = _receivedMailsSenders.asStateFlow()

    init {
        viewModelScope.launch {
            localMailRepo.getAllReceivedMailsSenders().collectLatest {
                _receivedMailsSenders.emit(it)
            }
        }
        viewModelScope.launch {
            combine(
                _searchQuery, hasAttachments, snapshotFlow {
                    _selectedLabelsFilter.toList()
                }, snapshotFlow { _selectedFromAccountsFilter.toList() }
            ) { searchQuery, hasAttachments, selectedLabelsFilter, selectedFromAccountsFilter ->
                SearchQueryFlow(
                    searchQuery,
                    hasAttachments,
                    selectedLabelsFilter,
                    selectedFromAccountsFilter
                )
            }.collectLatest { searchFlow ->
                merge(
                    localMailRepo.queryCurrentSessionMails(
                        senders = searchFlow.selectedFromAccountsFilter,
                        sendersCount = searchFlow.selectedFromAccountsFilter.size,
                        query = searchFlow.searchQuery,
                        hasAttachments = searchFlow.hasAttachments,
                        inInbox = searchFlow.selectedLabelsFilter.contains("Inbox"),
                        inStarred = searchFlow.selectedLabelsFilter.contains("Starred"),
                        inArchive = searchFlow.selectedLabelsFilter.contains("Archive"),
                        inTrash = searchFlow.selectedLabelsFilter.contains("Trash")
                    ), localMailRepo.queryAllSessionMails(
                        senders = searchFlow.selectedFromAccountsFilter,
                        sendersCount = searchFlow.selectedFromAccountsFilter.size,
                        query = searchFlow.searchQuery,
                        hasAttachments = hasAttachments.value,
                        inInbox = searchFlow.selectedLabelsFilter.contains("All Inboxes"),
                        inStarred = searchFlow.selectedLabelsFilter.contains("All Starred"),
                        inArchive = searchFlow.selectedLabelsFilter.contains("All Archives"),
                        inTrash = searchFlow.selectedLabelsFilter.contains("All Trashed")
                    )
                ).distinctUntilChanged().collectLatest {
                    _searchResults.emit(it)
                }
            }
        }
    }
    companion object {
        val isSearchEnabled = mutableStateOf(false)
    }

    fun onUiEvent(searchUiEvent: SearchUiEvent) {
        when (searchUiEvent) {
            is SearchUiEvent.AddANewLabelFilter -> {
                _selectedLabelsFilter.add(searchUiEvent.filterName)
            }

            is SearchUiEvent.ChangeAttachmentsSelectionState -> {
                viewModelScope.launch {
                    _hasAttachments.emit(searchUiEvent.isSelected)
                }
            }

            is SearchUiEvent.ChangeQuery -> {
                viewModelScope.launch {
                    _searchQuery.emit(searchUiEvent.query)
                }
            }

            is SearchUiEvent.RemoveALabelFilter -> {
                _selectedLabelsFilter.remove(searchUiEvent.filterName)
            }
        }
    }
}