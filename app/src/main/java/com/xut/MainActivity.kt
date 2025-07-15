package com.xut

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import com.xut.ui.screen.LoginScreen
import com.xut.ui.screen.UnlockScreen
import com.xut.ui.theme.XUTTheme
import com.xut.utils.AuthManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val authManager = AuthManager.getInstance()
            var isLoggedIn by remember { mutableStateOf(false) }

            DisposableEffect(Unit) {
                val authListener = object : AuthManager.Listener {
                    override fun onLoginChange() {
                        isLoggedIn = authManager.isLoggedIn
                    }
                }

                authManager.addListener(authListener)
                onDispose {
                    authManager.removeListener(authListener)
                }
            }

            XUTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Crossfade(isLoggedIn) { isLoggedIn ->
                        if (isLoggedIn) {
                            UnlockScreen()
                        } else {
                            LoginScreen()
                        }
                    }
                }
            }
        }
    }
}