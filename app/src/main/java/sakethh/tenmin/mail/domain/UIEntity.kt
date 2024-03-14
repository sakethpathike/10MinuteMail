package sakethh.tenmin.mail.domain

import androidx.compose.ui.graphics.vector.ImageVector

sealed class UIEntity {
    data class NavigationDrawer(
        val itemName: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val itemOnClick: () -> Unit
    ) : UIEntity()
}
