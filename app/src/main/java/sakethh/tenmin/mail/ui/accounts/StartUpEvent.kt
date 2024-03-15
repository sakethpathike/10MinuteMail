package sakethh.tenmin.mail.ui.accounts

sealed class StartUpEvent {
    data object ShowLoadingDataTransition : StartUpEvent()
    data object FetchingTokenAndID : StartUpEvent()
    data object FetchingMailAccountData : StartUpEvent()
    data object CheckingIfAnySessionAlreadyExists : StartUpEvent()
    data object AddingDataToLocalDatabase : StartUpEvent()
    data object UpdatingLocalDatabase : StartUpEvent()
    data class NavigateToMail(val navigationRoute: String) : StartUpEvent()
    data object None : StartUpEvent()
}