package sakethh.tenmin.mail.ui.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sakethh.tenmin.mail.ui.settings.common.SettingsComponent
import sakethh.tenmin.mail.ui.settings.model.SettingsComponentState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecificSettingsScreen(
    navController: NavController,
    settingsScreenVM: SettingsScreenVM = hiltViewModel()
) {
    val topAppBarScrollState = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(topBar = {
        Column {
            LargeTopAppBar(navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Icon for navigating to previous screen"
                    )
                }
            }, scrollBehavior = topAppBarScrollState, title = {
                Text(
                    text = SettingsScreenVM.settingsScreenType.name.substring(0, 1).toUpperCase()
                        .plus(SettingsScreenVM.settingsScreenType.name.substring(1).toLowerCase()),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp
                )
            })
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .nestedScroll(topAppBarScrollState.nestedScrollConnection)
        ) {
            when (SettingsScreenVM.settingsScreenType) {
                SettingsScreenType.THEME -> {
                    themeSection()
                }

                SettingsScreenType.GENERAL -> {
                    generalSection(settingsScreenVM.generalSection)
                }

                SettingsScreenType.DATA -> {

                }

                SettingsScreenType.PRIVACY -> {

                }
            }
        }
    }
}

private fun LazyListScope.generalSection(generalSection: List<SettingsComponentState>) {
    items(generalSection) {
        SettingsComponent(
            settingsComponentState = SettingsComponentState(
                title = it.title,
                doesDescriptionExists = it.doesDescriptionExists,
                description = it.description,
                isSwitchNeeded = it.isSwitchNeeded,
                isSwitchEnabled = it.isSwitchEnabled,
                onSwitchStateChange = it.onSwitchStateChange,
                isIconNeeded = it.isIconNeeded,
                icon = it.icon
            )
        )
    }
}


private fun LazyListScope.themeSection() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !SettingsScreenVM.Settings.shouldDarkThemeBeEnabled.value) {
        item(key = "Follow System Theme") {
            SettingsComponent(
                settingsComponentState = SettingsComponentState(title = "Follow System Theme",
                    doesDescriptionExists = false,
                    isSwitchNeeded = true,
                    description = null,
                    isSwitchEnabled = SettingsScreenVM.Settings.shouldFollowSystemTheme,
                    onSwitchStateChange = {

                    }, isIconNeeded = remember {
                        mutableStateOf(false)
                    })
            )
        }
    }
    if (!SettingsScreenVM.Settings.shouldFollowSystemTheme.value) {
        item(key = "Use Dark Mode") {
            SettingsComponent(
                settingsComponentState = SettingsComponentState(title = "Use Dark Mode",
                    doesDescriptionExists = false,
                    description = null,
                    isSwitchNeeded = true,
                    isSwitchEnabled = SettingsScreenVM.Settings.shouldDarkThemeBeEnabled,
                    onSwitchStateChange = {

                    }, isIconNeeded = remember {
                        mutableStateOf(false)
                    })
            )
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        item(key = "Use dynamic theming") {
            SettingsComponent(
                settingsComponentState = SettingsComponentState(title = "Use dynamic theming",
                    doesDescriptionExists = true,
                    description = "Change colour themes within the app based on your wallpaper.",
                    isSwitchNeeded = true,
                    isSwitchEnabled = SettingsScreenVM.Settings.shouldFollowDynamicTheming,
                    onSwitchStateChange = {

                    }, isIconNeeded = remember {
                        mutableStateOf(false)
                    })
            )
        }
    }
}