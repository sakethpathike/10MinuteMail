package sakethh.tenmin.mail.ui.settings

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsScreenVM : ViewModel() {
    companion object {
        var settingsScreenType = SettingsScreenType.THEME
        const val APP_VERSION_NAME = "v0.0.1"
        const val APP_VERSION_CODE = 1
    }

    object Settings {
        val shouldFollowDynamicTheming = mutableStateOf(false)
        val shouldFollowSystemTheme = mutableStateOf(true)
        val shouldDarkThemeBeEnabled = mutableStateOf(false)
        val isInAppWebTabEnabled = mutableStateOf(true)
        val isSendCrashReportsEnabled = mutableStateOf(true)
        val isAutoCheckUpdatesEnabled = mutableStateOf(true)
        val showDescriptionForSettingsState = mutableStateOf(true)
        val isOnLatestUpdate = mutableStateOf(false)
        val didServerTimeOutErrorOccurred = mutableStateOf(false)
        val savedAppCode = mutableIntStateOf(APP_VERSION_CODE - 1)
        val autoRefreshMailsDurationSeconds = mutableIntStateOf(0)
    }
}