package sakethh.tenmin.mail.ui.accounts.screens

import sakethh.tenmin.mail.data.local.model.Accounts

sealed class AccountsUiEvent {
    data class SignIn(val emailAddress: String, val emailPassword: String) : AccountsUiEvent()
    data class SwitchAccount(val account: Accounts) : AccountsUiEvent()
    data object OpenMail : AccountsUiEvent()
    data object GenerateANewTemporaryMailAccount : AccountsUiEvent()
    data object AddANewEmailAccount : AccountsUiEvent()
    data class LoginUsingExistingAccount(val account: Accounts) : AccountsUiEvent()
    data object SignOut : AccountsUiEvent()
    data object DeleteCurrentSessionAccountPermanently : AccountsUiEvent()
}