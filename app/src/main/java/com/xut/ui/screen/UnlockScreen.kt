package com.xut.ui.screen

import android.content.ClipData
import android.webkit.CookieManager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.xut.Constants
import com.xut.R
import com.xut.util.AuthManager
import com.xut.util.AuthUtils

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockScreen(modifier: Modifier = Modifier) {
    val authManager = AuthManager.getInstance()
    val cookieManager = CookieManager.getInstance()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val clipboard = LocalClipboard.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            cookieManager.run {
                                removeSessionCookies(null)
                                removeAllCookies(null)
                                flush()
                            }
                            authManager.logOut()
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    val copyAuthDataMessage = stringResource(R.string.copy_auth_data_message)
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val clipData = ClipData.newPlainText(
                                "auth data",
                                AuthUtils.asDataString(authManager)
                            )
                            clipboard.setClipEntry(clipData.toClipEntry())
                            snackbarHostState.showSnackbar(copyAuthDataMessage)
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_content_copy),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = {
                        coroutineScope.launch {
                            uriHandler.openUri(Constants.GITHUB_SOURCE_CODE_URL)
                        }
                    }) {
                        Icon(Icons.Outlined.Info, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior,
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedCard(modifier = Modifier.fillMaxSize()) {
                SelectionContainer {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = AuthUtils.asDataString(authManager),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}