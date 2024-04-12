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
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.local.repo.mail.LocalMailRepo
import sakethh.tenmin.mail.data.remote.api.model.mail.From
import sakethh.tenmin.mail.ui.home.screens.search.model.SearchQueryFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class SearchContentVM @Inject constructor(
    private val localMailRepo: LocalMailRepo
) : ViewModel() {
    private val _selectedLabelsFilter = mutableStateListOf<String>()
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

    private val _selectedDateRange = MutableStateFlow(Pair<String?, String?>(null, null))

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
                }, snapshotFlow { _selectedFromAccountsFilter.toList() }, _selectedDateRange
            ) { searchQuery, hasAttachments, selectedLabelsFilter, selectedFromAccountsFilter, selectedDateRange ->
                SearchQueryFlow(
                    searchQuery,
                    hasAttachments,
                    selectedLabelsFilter,
                    selectedFromAccountsFilter,
                    selectedDateRange
                )
            }.collectLatest { searchFlow ->
                localMailRepo.queryMails(
                    onlyCurrentSession = !searchFlow.selectedLabelsFilter.all { it.startsWith("All") },
                    senders = searchFlow.selectedFromAccountsFilter,
                    sendersCount = searchFlow.selectedFromAccountsFilter.size,
                    labelsCount = searchFlow.selectedLabelsFilter.size,
                    query = searchFlow.searchQuery,
                    hasAttachments = hasAttachments.value,
                    inInbox = searchFlow.selectedLabelsFilter.contains("All Inboxes"),
                    inStarred = searchFlow.selectedLabelsFilter.contains("All Starred"),
                    inArchive = searchFlow.selectedLabelsFilter.contains("All Archives"),
                    inTrash = searchFlow.selectedLabelsFilter.contains("All Trashed"),
                    isDateRangeSelected = searchFlow.selectedDateRange.first != null && searchFlow.selectedDateRange.second != null,
                    startDate = searchFlow.selectedDateRange.first.toString(),
                    endDate = searchFlow.selectedDateRange.second.toString()
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

            is SearchUiEvent.ChangeDateRange -> {
                viewModelScope.launch {
                    val dateFormat = SimpleDateFormat("yyyyMMdd")
                    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                    val startingDate = searchUiEvent.startingDateUTC?.let { Date(it) }?.let {
                        dateFormat.format(it)
                    }
                    val endingDate = searchUiEvent.endingDateUTC?.let { Date(it) }?.let {
                        dateFormat.format(it)
                    }
                    _selectedDateRange.emit(
                        Pair(
                            startingDate, endingDate
                        )
                    )
                }
            }
        }
    }
}