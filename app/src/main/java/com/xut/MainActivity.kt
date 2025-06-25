package com.xut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xut.domain.repository.AuthRepository
import com.xut.ui.screen.LoginScreen
import com.xut.ui.screen.UnlockScreen
import com.xut.ui.theme.XUTTheme
import com.xut.ui.viewmodel.UnlockViewModel
import com.xut.ui.viewmodel.UnlockViewModelFactory
import com.xut.util.AuthManager

class MainActivity : ComponentActivity(), AuthManager.Listener {
    private lateinit var appContainer: AppContainer
    private lateinit var authRepository: AuthRepository

    private var isLoggedIn by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appContainer = (application as XUTApplication).appContainer

        appContainer.authManager.registerListener(this)

        authRepository = appContainer.authRepository

        setContent {
            val viewModel = viewModel<UnlockViewModel>(
                factory = UnlockViewModelFactory(
                    authRepository = authRepository
                )
            )

            val unlockState by viewModel.unlockState.collectAsStateWithLifecycle()

            XUTTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Crossfade(isLoggedIn) { isLoggedIn ->
                        if (isLoggedIn) {
                            UnlockScreen(
                                unlockState = unlockState
                            )
                        } else {
                            LoginScreen(
                                onLoginSuccess = {}
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onLoginStateChanged(isLoggedIn: Boolean) {
        this.isLoggedIn = isLoggedIn
    }

    override fun onDestroy() {
        super.onDestroy()
        appContainer.authManager.unregisterListener(this)
    }
}