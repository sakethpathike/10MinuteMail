package sakethh.tenmin.mail.ui.accounts

sealed class StartUpEvent {
    data object FetchingTokenAndID : StartUpEvent()
    data object FetchingMailAccountData : StartUpEvent()
    data object CheckingIfAnySessionAlreadyExists : StartUpEvent()
    data object AddingDataToLocalDatabase : StartUpEvent()
    data object MailAlreadyExists : StartUpEvent()
    data object UpdatingLocalDatabase : StartUpEvent()
    data class Navigate(val navigationRoute: String) : StartUpEvent()

    data object None : StartUpEvent()
    sealed class Domains : StartUpEvent() {
        data object DomainsNotFound : Domains()
        data object FetchingDomains : Domains()
    }

    data object GeneratingMailAddressAndPassword : StartUpEvent()
    data object CreatingANewAccount : StartUpEvent()
    sealed class HttpResponse : StartUpEvent() {
        data object BadRequest400 : HttpResponse()
        data object Invalid401 : HttpResponse()
        data object NotFound404 : HttpResponse()
        data object UnProcessable422 : HttpResponse()
        data object TooManyRequests429 : HttpResponse()
    }
}