package sakethh.tenmin.mail.ui.settings.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sakethh.tenmin.mail.ui.common.pulsateEffect
import sakethh.tenmin.mail.ui.settings.model.SettingsComponentState

@Composable
fun SettingsComponent(
    settingsComponentState: SettingsComponentState
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    Row(modifier = Modifier
        .pulsateEffect {
            settingsComponentState.onSwitchStateChange(!settingsComponentState.isSwitchEnabled.value)
            settingsComponentState.onAcknowledgmentClick(uriHandler, context)
        }
        .fillMaxWidth()
        .padding(bottom = 15.dp)
        .animateContentSize(), verticalAlignment = Alignment.CenterVertically) {
        if (settingsComponentState.isIconNeeded.value && settingsComponentState.icon != null) {
            Spacer(modifier = Modifier.width(10.dp))
            FilledTonalIconButton(
                onClick = { settingsComponentState.onSwitchStateChange(!settingsComponentState.isSwitchEnabled.value) }) {
                Icon(imageVector = settingsComponentState.icon, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
        Column {
            Text(
                text = settingsComponentState.title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(if (settingsComponentState.shouldArrowIconBeAppear.value || settingsComponentState.isSwitchNeeded) 0.75f else 1f)
                    .padding(
                        start = if (settingsComponentState.isIconNeeded.value) 0.dp else 15.dp,
                    ),
                lineHeight = 20.sp
            )
            if (settingsComponentState.doesDescriptionExists) {
                Text(
                    text = settingsComponentState.description.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth(if (settingsComponentState.shouldArrowIconBeAppear.value || settingsComponentState.isSwitchNeeded) 0.75f else 1f)
                        .padding(
                            start = if (settingsComponentState.isIconNeeded.value) 0.dp else 15.dp,
                            top = 10.dp
                        )
                )
            }
        }
        if (settingsComponentState.isSwitchNeeded) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Switch(
                    modifier = Modifier
                        .padding(end = 15.dp),
                    checked = settingsComponentState.isSwitchEnabled.value,
                    onCheckedChange = {
                        settingsComponentState.onSwitchStateChange(it)
                    })
            }
        }
        if (settingsComponentState.shouldArrowIconBeAppear.value) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = {
                    settingsComponentState.onAcknowledgmentClick(
                        uriHandler,
                        context
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}