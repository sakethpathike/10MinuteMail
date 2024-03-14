package sakethh.tenmin.mail.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavigationDrawer(modalNavigationBarState: DrawerState, content: @Composable () -> Unit) {
    ModalNavigationDrawer(gesturesEnabled = true,
        drawerState = modalNavigationBarState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = "10 Minute Mail",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(15.dp)
                )
                Divider()
                repeat(5) {
                    Spacer(modifier = Modifier.height(15.dp))
                    NavigationDrawerItem(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 25.dp,
                            bottomEnd = 25.dp
                        ), icon = {
                            Icon(
                                imageVector = Icons.Default.AllInbox,
                                contentDescription = "All Inboxes"
                            )
                        }, label = {
                            Text(
                                text = "10 Minute Mail",
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }, selected = true, onClick = {

                        })
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(15.dp))
                NavigationDrawerItem(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 25.dp,
                        bottomEnd = 25.dp
                    ), icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }, label = {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }, selected = true, onClick = {

                    })
                Spacer(modifier = Modifier.height(15.dp))
                NavigationDrawerItem(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 25.dp,
                        bottomEnd = 25.dp
                    ), icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About"
                        )
                    }, label = {
                        Text(
                            text = "10 Minute Mail",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }, selected = true, onClick = {

                    })
            }
        }) {
        content()
    }
}