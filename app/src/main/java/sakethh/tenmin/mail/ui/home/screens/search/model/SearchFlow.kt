package sakethh.tenmin.mail.ui.home.screens.search.model

data class SearchFlow(
    val searchQuery: String,
    val hasAttachments: Boolean,
    val selectedLabelsFilter: List<String>
)
