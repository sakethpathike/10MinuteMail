package sakethh.tenmin.mail.ui.accounts.screens

import sakethh.tenmin.mail.data.local.model.LocalMailAccount

sealed class AccountsUiEvent {
    data class SignIn(val emailAddress: String, val emailPassword: String) : AccountsUiEvent()
    data class SwitchAccount(val account: LocalMailAccount) : AccountsUiEvent()
    data object OpenMail : AccountsUiEvent()
    data object GenerateANewTemporaryMailAccount : AccountsUiEvent()
    data object AddANewEmailAccount : AccountsUiEvent()
    data class LoginUsingALocallyExistingAccount(val account: LocalMailAccount) : AccountsUiEvent()
    data object SignOut : AccountsUiEvent()
    data class DeleteCurrentSessionAccountPermanently(
        val deleteAccountLocally: Boolean,
        val deleteAccountFromCloud: Boolean
    ) : AccountsUiEvent()
}