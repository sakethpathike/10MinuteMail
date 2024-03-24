package sakethh.tenmin.mail.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShortText
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.SystemUpdateAlt
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.ui.settings.model.SettingsComponentState

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
        val isAutoCheckUpdatesEnabled = mutableStateOf(false)
        val showDescriptionForSettingsState = mutableStateOf(false)
        val isOnLatestUpdate = mutableStateOf(false)
        val didServerTimeOutErrorOccurred = mutableStateOf(false)
        val savedAppCode = mutableIntStateOf(APP_VERSION_CODE - 1)
        val autoRefreshMailsDurationSeconds = mutableIntStateOf(0)
    }

    val generalSection = listOf(
        SettingsComponentState(title = "Use in-app browser",
            doesDescriptionExists = Settings.showDescriptionForSettingsState.value,
            description = "If this is enabled, links will be opened within the app; if this setting is not enabled, your default browser will open every time you click on a link when using this app.",
            isSwitchNeeded = true,
            isSwitchEnabled = Settings.isInAppWebTabEnabled,
            isIconNeeded = mutableStateOf(true),
            icon = Icons.Default.OpenInBrowser,
            onSwitchStateChange = {
                viewModelScope.launch {

                }
            }),
        SettingsComponentState(title = "Auto-Check for Updates",
            doesDescriptionExists = Settings.showDescriptionForSettingsState.value,
            description = "If this is enabled, 10MinuteMail automatically checks for updates when you open the app. If a new update is available, it notifies you with a toast message. If this setting is disabled, manual checks for the latest version can be done from the top of this screen.",
            isIconNeeded = mutableStateOf(true),
            icon = Icons.Default.SystemUpdateAlt,
            isSwitchNeeded = true,
            isSwitchEnabled = Settings.isAutoCheckUpdatesEnabled,
            onSwitchStateChange = {
                viewModelScope.launch {

                }
            }),
        SettingsComponentState(title = "Show description for Settings",
            doesDescriptionExists = true,
            description = "If this setting is enabled, detailed descriptions will be visible for certain settings, like the one you're reading now. If it is disabled, only the titles will be shown.",
            isSwitchNeeded = true,
            isIconNeeded = mutableStateOf(true),
            icon = Icons.AutoMirrored.Default.ShortText,
            isSwitchEnabled = Settings.showDescriptionForSettingsState,
            onSwitchStateChange = {
                viewModelScope.launch {

                }
            })
    )
}