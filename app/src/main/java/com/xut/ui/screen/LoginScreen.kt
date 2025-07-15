package com.xut.ui.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.webkit.WebViewClientCompat

import com.xut.Constants.DEVICE_ID_KEY
import com.xut.Constants.LOGIN_URL
import com.xut.Constants.PASS_TOKEN_KEY
import com.xut.Constants.USER_ID_KEY
import com.xut.utils.AuthManager

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen() {
    val authManager = AuthManager.getInstance()
    val cookieManager = CookieManager.getInstance()

    var webView by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var canGoBack by remember { mutableStateOf(false) }

    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    settings.javaScriptEnabled = true
                    @Suppress("DEPRECATION")
                    settings.databaseEnabled = true
                    settings.domStorageEnabled = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    settings.cacheMode = WebSettings.LOAD_DEFAULT

                    webViewClient = object : WebViewClientCompat() {
                        override fun onPageStarted(
                            view: WebView?, url: String?, favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }

                        override fun doUpdateVisitedHistory(
                            view: WebView?,
                            url: String?,
                            isReload: Boolean
                        ) {
                            super.doUpdateVisitedHistory(view, url, isReload)
                            canGoBack = view?.canGoBack() == true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false

                            cookieManager.getCookie(LOGIN_URL)
                                ?.split(";")
                                ?.map { it.trim() }
                                ?.forEach { cookie ->
                                    val parts = cookie.split("=", limit = 2)
                                    if (parts.size == 2) {
                                        val name = parts.getOrNull(0)?.trim()
                                        val value = parts.getOrNull(1)?.trim()

                                        when (name) {
                                            PASS_TOKEN_KEY -> {
                                                authManager.passToken = value!!
                                            }

                                            USER_ID_KEY -> {
                                                authManager.userId = value!!
                                            }

                                            DEVICE_ID_KEY -> {
                                                authManager.deviceId = value!!
                                            }
                                        }

                                        if (authManager.isLoggedIn) {
                                            return@forEach
                                        }
                                    }
                                }
                        }
                    }

                    webView = this
                    cookieManager.run {
                        setAcceptCookie(true)
                        setAcceptThirdPartyCookies(this@apply, true)
                    }
                    loadUrl(LOGIN_URL)
                }
            })

        if (isLoading) {
            webView?.isVisible = false
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            webView?.isVisible = true
        }
    }
}