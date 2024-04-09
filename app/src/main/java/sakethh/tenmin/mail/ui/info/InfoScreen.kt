package sakethh.tenmin.mail.ui.info

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import sakethh.tenmin.mail.ui.common.pulsateEffect
import sakethh.tenmin.mail.ui.settings.SettingsScreenVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(navController: NavController) {
    val infoScreenVM: InfoScreenVM = viewModel()
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface),
            navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "navigate to previous screen"
                )
            }
        }, title = {
            Text(
                text = "10 Minute Mail",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )
        })
    }) {
        LazyColumn(
            modifier = Modifier
                .background(if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(it)
                .padding(start = 15.dp, end = 15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Text(text = buildAnnotatedString {
                    append("10 Minute Mail provides temporary email addresses for signing up for websites and services, powered by the ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("mail.gw")
                    }
                    append(" public API to manage the emails and accounts.\n\nAny emails you receive will be ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("automatically deleted after 10 minutes")
                    }
                    append(" for your peace of mind.\n\n")

                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Note:")
                    }
                    append(" Emails you receive will not be deleted locally until you manually delete them. However, messages stored on the cloud will be automatically deleted after 10 minutes.")
                }, style = MaterialTheme.typography.titleSmall, lineHeight = 20.sp)
            }

            item {
                Text(
                    text = "Frequently Asked Questions",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp
                )
            }
            item {
                Text(
                    text = buildAnnotatedString {
                        append("These questions and descriptions are picked from the ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("official mail.gw FAQ")
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            items(infoScreenVM.faqData) {
                CollapsableFAQItem(question = it.question, description = it.description)
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

enum class ItemState { Pressed, Idle }

@Composable
private fun CollapsableFAQItem(question: String, description: String) {
    val isExpandClicked = rememberSaveable {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = Modifier
                .pulsateEffect {
                    isExpandClicked.value = !isExpandClicked.value
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
            IconButton(onClick = {
                isExpandClicked.value = !isExpandClicked.value
            }) {
                Icon(
                    imageVector = if (!isExpandClicked.value) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                    contentDescription = "Expand Question Icon"
                )
            }
        }
        Box(
            modifier = Modifier
                .animateContentSize()
        ) {
            if (isExpandClicked.value) {
                Text(text = description, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}