package sakethh.tenmin.mail.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.SettingsInputSvideo
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import sakethh.tenmin.mail.NavigationRoutes
import sakethh.tenmin.mail.ui.settings.common.SettingsSectionComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val topAppBarScrollState = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(topBar = {
        Column {
            LargeTopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface),
                navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "navigate to previous screen"
                    )
                }
            }, scrollBehavior = topAppBarScrollState, title = {
                Text(
                    text = "Settings",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp
                )
            })
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .background(if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(it)
                .nestedScroll(topAppBarScrollState.nestedScrollConnection)
        ) {
            item(key = "themeRow") {
                SettingsSectionComposable(
                    onClick = {
                        SettingsScreenVM.settingsScreenType = SettingsScreenType.THEME
                        navController.navigate(NavigationRoutes.SPECIFIC_SETTINGS.name)
                    },
                    sectionTitle = "Theme",
                    sectionIcon = Icons.Default.ColorLens
                )
            }
            item(key = "generalRow") {
                SettingsSectionComposable(
                    onClick = {
                        SettingsScreenVM.settingsScreenType = SettingsScreenType.GENERAL
                        navController.navigate(NavigationRoutes.SPECIFIC_SETTINGS.name)
                    },
                    sectionTitle = "General",
                    sectionIcon = Icons.Default.SettingsInputSvideo
                )
            }
            item(key = "dataRow") {
                SettingsSectionComposable(
                    onClick = {
                        SettingsScreenVM.settingsScreenType = SettingsScreenType.DATA
                        navController.navigate(NavigationRoutes.SPECIFIC_SETTINGS.name)
                    },
                    sectionTitle = "Data",
                    sectionIcon = Icons.Default.Storage
                )
            }
            item(key = "privacyRow") {
                SettingsSectionComposable(
                    onClick = {
                        SettingsScreenVM.settingsScreenType = SettingsScreenType.PRIVACY
                        navController.navigate(NavigationRoutes.SPECIFIC_SETTINGS.name)
                    },
                    sectionTitle = "Privacy",
                    sectionIcon = Icons.Default.PrivacyTip
                )
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}