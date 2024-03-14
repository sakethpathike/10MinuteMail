package sakethh.tenmin.mail.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.SettingsInputSvideo
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val topAppBarScrollState = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(topBar = {
        Column {
            LargeTopAppBar(scrollBehavior = topAppBarScrollState, title = {
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
                .fillMaxSize()
                .padding(it)
                .nestedScroll(topAppBarScrollState.nestedScrollConnection)
        ) {
            item(key = "themeRow") {
                SettingsSectionComposable(
                    onClick = {
                    },
                    sectionTitle = "Theme",
                    sectionIcon = Icons.Default.ColorLens
                )
            }
            item(key = "generalRow") {
                SettingsSectionComposable(
                    onClick = {
                    },
                    sectionTitle = "General",
                    sectionIcon = Icons.Default.SettingsInputSvideo
                )
            }
            item(key = "dataRow") {
                SettingsSectionComposable(
                    onClick = {
                    },
                    sectionTitle = "Data",
                    sectionIcon = Icons.Default.Storage
                )
            }
            item(key = "privacyRow") {
                SettingsSectionComposable(
                    onClick = {
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