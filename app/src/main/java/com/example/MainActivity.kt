package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.LoginScreen
import com.example.ui.MainScreen
import com.example.ui.SafetyViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SafetyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

                    if (isLoggedIn) {
                        MainScreen(viewModel = viewModel)
                    } else {
                        LoginScreen(
                            onLoginSuccess = { phone ->
                                viewModel.loginWithMobileAndOtp(phone)
                            }
                        )
                    }
                }
            }
        }
    }

    // Hardware Power Button / Key Press Interceptor for 5-press SOS shortcut
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_POWER || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (viewModel.isPowerKeyShortcutEnabled.value) {
                viewModel.registerPowerKeyPress()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
