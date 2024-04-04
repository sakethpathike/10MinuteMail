package sakethh.tenmin.mail.ui.home.screens.childHomeScreen

sealed class ChildHomeScreenEvent {
    data object None : ChildHomeScreenEvent()
    data class MoveToArchive(val mailId: String) : ChildHomeScreenEvent()
    data class RemoveFromInbox(val mailId: String) : ChildHomeScreenEvent()
    data class RemoveFromArchive(val mailId: String) : ChildHomeScreenEvent()
    data class RemoveFromTrash(val mailId: String) : ChildHomeScreenEvent()
    data class OnStarIconClick(val mailId: String) : ChildHomeScreenEvent()
    data class UnMarkStarredMail(val mailId: String) : ChildHomeScreenEvent()
    data class MoveToTrash(val mailId: String) : ChildHomeScreenEvent()
}