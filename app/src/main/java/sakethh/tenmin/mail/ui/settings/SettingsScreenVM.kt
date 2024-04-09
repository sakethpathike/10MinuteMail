package sakethh.tenmin.mail.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShortText
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SystemUpdateAlt
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import sakethh.tenmin.mail.ui.settings.SettingsScreenVM.Settings.shouldAutomaticallyRetrieveMails
import sakethh.tenmin.mail.ui.settings.model.SettingsComponentState
import javax.inject.Inject

@HiltViewModel
class SettingsScreenVM @Inject constructor(private val dataStore: DataStore<Preferences>) :
    ViewModel() {
    companion object {
        var settingsScreenType = SettingsScreenType.THEME
        const val APP_VERSION_NAME = "v0.0.1"
        const val APP_VERSION_CODE = 1
    }

    object Settings {
        val shouldFollowDynamicTheming = mutableStateOf(false)
        val shouldFollowSystemTheme = mutableStateOf(true)
        val shouldDarkThemeBeEnabled = mutableStateOf(false)
        val shouldDimDarkThemeBeEnabled = mutableStateOf(false)
        val isInAppWebTabEnabled = mutableStateOf(true)
        val isSendCrashReportsEnabled = mutableStateOf(true)
        val isAutoCheckUpdatesEnabled = mutableStateOf(false)
        val showDescriptionForSettingsState = mutableStateOf(false)
        val isOnLatestUpdate = mutableStateOf(false)
        val didServerTimeOutErrorOccurred = mutableStateOf(false)
        val savedAppCode = mutableIntStateOf(APP_VERSION_CODE - 1)
        val autoRefreshMailsDurationSeconds = mutableIntStateOf(0)
        val shouldAutomaticallyRetrieveMails = mutableStateOf(true)
    }

    val generalSection = listOf(
        SettingsComponentState(
            title = "Auto-Refresh Mails",
            doesDescriptionExists = Settings.showDescriptionForSettingsState.value,
            description = "If enabled, emails will refresh automatically based on the duration you specify below. If disabled, you'll need to refresh manually to view the latest emails.",
            isSwitchNeeded = true,
            isSwitchEnabled = shouldAutomaticallyRetrieveMails,
            onSwitchStateChange = {
                shouldAutomaticallyRetrieveMails.value = it
            },
            isIconNeeded = mutableStateOf(true),
            icon = Icons.Default.Refresh
        ),
        SettingsComponentState(title = "Use in-app browser",
            doesDescriptionExists = Settings.showDescriptionForSettingsState.value,
            description = "If this is enabled, links will be opened within the app; if this setting is not enabled, your default browser will open every time you click on a link when using this app.",
            isSwitchNeeded = true,
            isSwitchEnabled = Settings.isInAppWebTabEnabled,
            isIconNeeded = mutableStateOf(true),
            icon = Icons.Default.OpenInBrowser,
            onSwitchStateChange = {
                Settings.isInAppWebTabEnabled.value = it
                viewModelScope.launch {
                    changeSettingPreferenceValue(
                        preferenceKey = booleanPreferencesKey(Setting.USE_IN_APP_BROWSER.name),
                        dataStore = dataStore,
                        newValue = !Settings.isInAppWebTabEnabled.value
                    )
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
                Settings.isAutoCheckUpdatesEnabled.value = it
                viewModelScope.launch {
                    changeSettingPreferenceValue(
                        preferenceKey = booleanPreferencesKey(Setting.USE_IN_APP_BROWSER.name),
                        dataStore = dataStore,
                        newValue = !Settings.isAutoCheckUpdatesEnabled.value
                    )
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
                Settings.showDescriptionForSettingsState.value = it
                viewModelScope.launch {
                    changeSettingPreferenceValue(
                        preferenceKey = booleanPreferencesKey(Setting.USE_IN_APP_BROWSER.name),
                        dataStore = dataStore,
                        newValue = !Settings.showDescriptionForSettingsState.value
                    )
                }
            })
    )

    private fun <T> changeSettingPreferenceValue(
        preferenceKey: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
        newValue: T,
    ) {
        viewModelScope.launch {
            dataStore.edit {
                it[preferenceKey] = newValue
            }
        }
    }

    private suspend fun readSortingPreferenceValue(
        preferenceKey: Preferences.Key<String>,
        dataStore: DataStore<Preferences>,
    ): String? {
        return dataStore.data.first()[preferenceKey]
    }
}
