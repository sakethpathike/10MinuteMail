package sakethh.tenmin.mail.ui.accounts

sealed class StartUpEvent {
    data object FetchingTokenAndID : StartUpEvent()
    data object FetchingMailAccountData : StartUpEvent()
    data object CheckingIfAnySessionAlreadyExists : StartUpEvent()
    data object AddingDataToLocalDatabase : StartUpEvent()
    data object UpdatingLocalDatabase : StartUpEvent()
    data class Navigate(val navigationRoute: String) :
        StartUpEvent()

    data object None : StartUpEvent()
    data object DomainsNotFound : HttpResponse()

    sealed class HttpResponse : StartUpEvent() {
        data object BadRequest400 : HttpResponse()
        data object Invalid401 : HttpResponse()
        data object NotFound404 : HttpResponse()
        data object UnProcessable422 : HttpResponse()
        data object TooManyRequests429 : HttpResponse()
    }
}