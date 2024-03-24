package sakethh.tenmin.mail.ui.accounts

sealed class AccountsEvent {
    data object FetchingTokenAndID : AccountsEvent()
    data object FetchingMailAccountData : AccountsEvent()
    data object CheckingIfAnySessionAlreadyExists : AccountsEvent()
    data object AddingDataToLocalDatabase : AccountsEvent()
    data object MailAlreadyExists : AccountsEvent()
    data object UpdatingLocalDatabase : AccountsEvent()
    data class Navigate(val navigationRoute: String) : AccountsEvent()
    data object RelaunchTheApp : AccountsEvent()
    data object None : AccountsEvent()
    sealed class Domains : AccountsEvent() {
        data object DomainsNotFound : Domains()
        data object FetchingDomains : Domains()
    }

    data object GeneratingMailAddressAndPassword : AccountsEvent()
    data object CreatingANewAccount : AccountsEvent()
    sealed class HttpResponse : AccountsEvent() {
        data object BadRequest400 : HttpResponse()
        data object Invalid401 : HttpResponse()
        data object NotFound404 : HttpResponse()
        data object UnProcessable422 : HttpResponse()
        data object TooManyRequests429 : HttpResponse()
    }
}