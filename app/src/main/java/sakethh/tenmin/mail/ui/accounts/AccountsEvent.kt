package sakethh.tenmin.mail.ui.accounts

sealed class AccountsEvent {
    data object GenerateANewTemporaryAccount : AccountsEvent()
    data object SignIn : AccountsEvent()
    data object FAQ : AccountsEvent()
    data object Help : AccountsEvent()
}