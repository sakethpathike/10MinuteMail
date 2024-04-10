package sakethh.tenmin.mail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import sakethh.tenmin.mail.ui.accounts.screens.SignInScreen
import sakethh.tenmin.mail.ui.accounts.screens.StartUpScreen
import sakethh.tenmin.mail.ui.home.HomeScreen
import sakethh.tenmin.mail.ui.settings.SettingsScreenVM
import sakethh.tenmin.mail.ui.theme.TenMinuteMailTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsScreenVM = hiltViewModel<SettingsScreenVM>()
            LaunchedEffect(key1 = true) {
                settingsScreenVM.readAllPreferenceValues()
            }
            TenMinuteMailTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface)
                val navController = rememberNavController()
                Surface {
                    NavHost(
                        navController = navController,
                        startDestination = NavigationRoutes.STARTUP.name
                    ) {
                        composable(route = NavigationRoutes.STARTUP.name) {
                            StartUpScreen(navController)
                        }
                        composable(route = NavigationRoutes.SIGN_IN.name) {
                            SignInScreen(navController = navController)
                        }
                        composable(route = NavigationRoutes.HOME.name) {
                            HomeScreen(mainNavController = navController)
                        }
                    }
                }
                rememberSystemUiController().setNavigationBarColor(
                    color = if (SettingsScreenVM.Settings.shouldDimDarkThemeBeEnabled.value) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}
