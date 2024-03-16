package sakethh.tenmin.mail.ui.accounts

import sakethh.tenmin.mail.NavigationRoutes

sealed class StartUpEvent {
    data object ShowLoadingDataTransition : StartUpEvent()
    data object FetchingTokenAndID : StartUpEvent()
    data object FetchingMailAccountData : StartUpEvent()
    data object CheckingIfAnySessionAlreadyExists : StartUpEvent()
    data object AddingDataToLocalDatabase : StartUpEvent()
    data object UpdatingLocalDatabase : StartUpEvent()
    data class Navigate(val navigationRoute: String = NavigationRoutes.HOME.name) :
        StartUpEvent()
    data object None : StartUpEvent()
}