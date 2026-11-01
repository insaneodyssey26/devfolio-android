package com.masum.devfolio.ui.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.masum.devfolio.data.auth.AuthSessionManager
import com.masum.devfolio.theme.rememberDevfolioHaptics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevfolioSignInDialog(
    onDismissRequest: () -> Unit,
    onSignInSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val context = LocalContext.current
    val haptics = rememberDevfolioHaptics()
    val authManager = remember { AuthSessionManager.getInstance(context) }
    var isLoading by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(start = 12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sign in to Devfolio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Connect Google, GitHub or Email",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledTonalIconButton(
                    onClick = {
                        haptics.lightTick()
                        onDismissRequest()
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            @SuppressLint("SetJavaScriptEnabled")
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.userAgentString = settings.userAgentString + " DevfolioMobileClient/1.0"

                            val cookieManager = CookieManager.getInstance()
                            cookieManager.setAcceptCookie(true)
                            cookieManager.setAcceptThirdPartyCookies(this, true)

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading = true
                                    checkCookies(url)
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    checkCookies(url)
                                }

                                 override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: android.webkit.WebResourceError?
                                ) {
                                    super.onReceivedError(view, request, error)
                                    if (request?.isForMainFrame == true) {
                                        isLoading = false
                                    }
                                }

                                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                    checkCookies(request?.url?.toString())
                                    return false
                                }

                                private var isProcessingSuccess = false

                                private fun checkCookies(currentUrl: String?) {
                                    if (isProcessingSuccess) return
                                    val cookieStr = cookieManager.getCookie("https://devfolio.co") ?: return
                                    val hasAuthCookie = cookieStr.contains("devfolio_auth=") || cookieStr.contains("access_token=")
                                    val hasUserCookie = cookieStr.contains("devfolio_user=")

                                    if (hasAuthCookie && hasUserCookie) {
                                        isProcessingSuccess = true
                                        authManager.parseAndSaveCookies(cookieStr) { success ->
                                            if (success) {
                                                haptics.heavyClick()
                                                onSignInSuccess()
                                            } else {
                                                isProcessingSuccess = false
                                            }
                                        }
                                    }
                                }
                            }

                            loadUrl("https://devfolio.co/signin")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
