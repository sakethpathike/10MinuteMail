package sakethh.tenmin.mail.ui.home.screens.search

sealed class SearchUiEvent {
    data class AddANewLabelFilter(val filterName: String) : SearchUiEvent()
    data class RemoveALabelFilter(val filterName: String) : SearchUiEvent()
    data class ChangeQuery(val query: String) : SearchUiEvent()
    data class ChangeAttachmentsSelectionState(val isSelected: Boolean) : SearchUiEvent()

    data class ChangeDateRange(val startingDate: String?, val endingDate: String?) :
        SearchUiEvent()
}