package sakethh.tenmin.mail.ui.home.screens.search

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.data.local.model.LocalMail
import sakethh.tenmin.mail.data.local.repo.accounts.LocalAccountsRepo
import sakethh.tenmin.mail.data.local.repo.mail.LocalMailRepo
import sakethh.tenmin.mail.ui.home.screens.search.model.SearchFlow
import javax.inject.Inject

@HiltViewModel
class SearchContentVM @Inject constructor(
    private val localMailRepo: LocalMailRepo, private val localAccountsRepo: LocalAccountsRepo
) : ViewModel() {
    private val _selectedLabelsFilter = MutableStateFlow(mutableListOf<String>())
    val selectedLabelsFilter = _selectedLabelsFilter.value.toMutableStateList()

    val selectedFromAccountsFilter = mutableStateListOf("")

    private val _hasAttachments = MutableStateFlow(false)
    val hasAttachments = _hasAttachments.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow(emptyList<LocalMail>())
    val searchResults = _searchResults.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _searchQuery, hasAttachments, _selectedLabelsFilter
            ) { searchQuery, hasAttachments, selectedLabelsFilter ->
                Log.d("10MinMail", "Triggered")
                SearchFlow(searchQuery, hasAttachments, selectedLabelsFilter)
            }.collectLatest { searchFlow ->
                merge(
                    localMailRepo.queryCurrentSessionMails(
                        query = searchFlow.searchQuery,
                        hasAttachments = searchFlow.hasAttachments,
                        inInbox = searchFlow.selectedLabelsFilter.contains("Inbox"),
                        inStarred = searchFlow.selectedLabelsFilter.contains("Starred"),
                        inArchive = searchFlow.selectedLabelsFilter.contains("Archive"),
                        inTrash = searchFlow.selectedLabelsFilter.contains("Trash")
                    ), localMailRepo.queryAllSessionMails(
                        query = searchFlow.searchQuery,
                        hasAttachments = hasAttachments.value,
                        inInbox = searchFlow.selectedLabelsFilter.contains("All Inboxes"),
                        inStarred = searchFlow.selectedLabelsFilter.contains("All Starred"),
                        inArchive = searchFlow.selectedLabelsFilter.contains("All Archives"),
                        inTrash = searchFlow.selectedLabelsFilter.contains("All Trashed")
                    )
                ).collectLatest {
                    _searchResults.emit(it)
                    Log.d("10MinMail", it.toString())
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
                _selectedLabelsFilter.value.add(searchUiEvent.filterName)
                viewModelScope.launch {
                    _selectedLabelsFilter.emit(_selectedLabelsFilter.value)
                }
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
                _selectedLabelsFilter.value.remove(searchUiEvent.filterName)
                viewModelScope.launch {
                    _selectedLabelsFilter.emit(_selectedLabelsFilter.value)
                }
            }
        }
    }
}