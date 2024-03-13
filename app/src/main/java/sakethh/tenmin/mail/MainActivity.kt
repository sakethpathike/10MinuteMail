package sakethh.tenmin.mail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import sakethh.tenmin.mail.ui.home.HomeScreen
import sakethh.tenmin.mail.ui.theme.TenMinuteMailTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TenMinuteMailTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surface)
                Surface{
                    HomeScreen()
                }
            }
        }
    }
}
