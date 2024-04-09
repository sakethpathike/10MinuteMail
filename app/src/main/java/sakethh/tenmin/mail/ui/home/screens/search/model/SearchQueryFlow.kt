package sakethh.tenmin.mail.ui.home.screens.search.model

import sakethh.tenmin.mail.data.remote.api.model.mail.From

data class SearchQueryFlow(
    val searchQuery: String,
    val hasAttachments: Boolean,
    val selectedLabelsFilter: List<String>,
    val selectedFromAccountsFilter: List<From>
)
