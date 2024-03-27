package sakethh.tenmin.mail.ui.home

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationDrawerModel(
    val itemName: String,
    val selectedIcon: ImageVector,
    val nonSelectedIcon: ImageVector,
    val navigationRoute: String
)
