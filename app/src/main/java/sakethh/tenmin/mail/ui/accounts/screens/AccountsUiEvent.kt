package sakethh.tenmin.mail.ui.accounts.screens

sealed class AccountsUiEvent {
    data class SignIn(val emailAddress: String, val emailPassword: String) : AccountsUiEvent()
    data object OpenMail : AccountsUiEvent()
    data object GenerateANewTemporaryMailAccount : AccountsUiEvent()
    data object AddANewEmailAccount : AccountsUiEvent()
    data object SignOut : AccountsUiEvent()
    data object DeleteCurrentSessionAccountPermanently : AccountsUiEvent()
}