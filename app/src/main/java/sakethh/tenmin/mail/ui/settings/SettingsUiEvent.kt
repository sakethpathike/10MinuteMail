package sakethh.tenmin.mail.ui.settings

sealed class SettingsUiEvent {
    data class ChangeSettingPreference(val settingType: Setting, val newValue: Boolean) :
        SettingsUiEvent()
}