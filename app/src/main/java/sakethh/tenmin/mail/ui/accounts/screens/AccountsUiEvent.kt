package sakethh.tenmin.mail.ui.accounts.screens

sealed class AccountsUiEvent {
    data class SignIn(val emailAddress: String, val emailPassword: String) : AccountsUiEvent()
}