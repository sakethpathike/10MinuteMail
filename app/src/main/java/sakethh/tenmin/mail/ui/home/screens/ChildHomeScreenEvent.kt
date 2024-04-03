package sakethh.tenmin.mail.ui.home.screens

sealed class ChildHomeScreenEvent {
    data class MoveToArchive(val mailId: String) : ChildHomeScreenEvent()
    data class OnStarIconClick(val mailId: String) : ChildHomeScreenEvent()
    data class MoveToTrash(val mailId: String) : ChildHomeScreenEvent()
    data class DeletePermanently(val mailId: String) : ChildHomeScreenEvent()
}