package com.masum.devfolio.ui.detail

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masum.devfolio.data.auth.AuthSessionManager
import com.masum.devfolio.data.auth.AuthState
import com.masum.devfolio.data.hackathon.ApplicationStatusDetail
import com.masum.devfolio.data.hackathon.HackathonApplicationRepository
import com.masum.devfolio.data.hackathon.NativeApplicationStatus
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.theme.rememberDevfolioHaptics
import com.masum.devfolio.ui.auth.DevfolioSignInDialog
import com.masum.devfolio.util.addHackathonToCalendar
import com.masum.devfolio.util.launchDevfolioCustomTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ApplyActionBar(
    hackathon: Hackathon,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptics = rememberDevfolioHaptics()
    val scope = rememberCoroutineScope()
    val authManager = remember { AuthSessionManager.getInstance(context) }
    val authState by authManager.authState.collectAsState()
    val appRepo = remember { HackathonApplicationRepository() }

    val cachedDetail = (authState as? AuthState.Authenticated)?.let {
        HackathonApplicationRepository.getCachedStatus(it.user.username, hackathon.slug)
    }
    var applicationDetail by remember(hackathon.slug) { mutableStateOf(cachedDetail) }
    var isCheckingStatus by remember(hackathon.slug, authState) {
        mutableStateOf(false)
    }
    var isActionInProgress by remember { mutableStateOf(false) }
    var showSignInDialog by remember { mutableStateOf(false) }
    var showWithdrawConfirmDialog by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    fun refreshStatus() {
        val user = (authState as? AuthState.Authenticated)?.user ?: return
        scope.launch {
            isCheckingStatus = true
            appRepo.getApplicationStatus(user, hackathon.slug).onSuccess {
                applicationDetail = it
            }.onFailure {
                applicationDetail = ApplicationStatusDetail(NativeApplicationStatus.NOT_APPLIED)
            }
            isCheckingStatus = false
        }
    }

    LaunchedEffect(authState, hackathon.slug) {
        if (authState is AuthState.Authenticated) {
            refreshStatus()
        } else {
            applicationDetail = null
        }
    }

    if (showSignInDialog) {
        DevfolioSignInDialog(
            onDismissRequest = { showSignInDialog = false },
            onSignInSuccess = {
                showSignInDialog = false
                refreshStatus()
            }
        )
    }

    if (showWithdrawConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showWithdrawConfirmDialog = false },
            title = { Text("Withdraw Application?") },
            text = { Text("Are you sure you want to withdraw your application from ${hackathon.name}? This will de-register you from this hackathon.") },
            confirmButton = {
                Button(
                    onClick = {
                        val user = (authState as? AuthState.Authenticated)?.user
                        if (user != null) {
                            scope.launch {
                                isActionInProgress = true
                                showWithdrawConfirmDialog = false
                                appRepo.withdrawApplication(user, hackathon.slug).onSuccess {
                                    haptics.heavyClick()
                                    applicationDetail = ApplicationStatusDetail(NativeApplicationStatus.WITHDRAWN)
                                }
                                isActionInProgress = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Yes, Withdraw")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            val status = applicationDetail?.status
            val teamName = applicationDetail?.teamName

            if (status == NativeApplicationStatus.APPLIED_UNDER_REVIEW) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.HourglassTop,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (!teamName.isNullOrBlank()) "Submitted · Team $teamName" else "Application Submitted · Under Review",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            } else if (status == NativeApplicationStatus.RSVP_CONFIRMED) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF064E3B))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "RSVP Confirmed · You're Attending!",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFA7F3D0)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            } else if (status == NativeApplicationStatus.SESSION_EXPIRED) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Session Expired · Sign in again to view status",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            } else if (status == NativeApplicationStatus.WAITLISTED) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.HourglassTop,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Application on Waitlist",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    val currentButtonState = when {
                        isActionInProgress -> "loading"
                        authState !is AuthState.Authenticated -> "sign_in"
                        status == NativeApplicationStatus.APPLIED_UNDER_REVIEW -> "withdraw"
                        status == NativeApplicationStatus.ACCEPTED_RSVP_PENDING -> "rsvp"
                        else -> "apply"
                    }

                    Crossfade(
                        targetState = currentButtonState,
                        label = "action_bar_button_crossfade"
                    ) { stateKey ->
                        when (stateKey) {
                            "sign_in" -> {
                                Button(
                                    onClick = {
                                        haptics.lightTick()
                                        showSignInDialog = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(27.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Sign in to Apply",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            "loading" -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = accentColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            "withdraw" -> {
                                FilledTonalButton(
                                    onClick = {
                                        haptics.lightTick()
                                        showWithdrawConfirmDialog = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(27.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DeleteOutline,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Withdraw Application",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            "rsvp" -> {
                                Button(
                                    onClick = {
                                        val user = (authState as? AuthState.Authenticated)?.user ?: return@Button
                                        scope.launch {
                                            isActionInProgress = true
                                            appRepo.confirmRsvp(user, hackathon.slug).onSuccess {
                                                haptics.heavyClick()
                                                applicationDetail = applicationDetail?.copy(status = NativeApplicationStatus.RSVP_CONFIRMED)
                                            }
                                            isActionInProgress = false
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(27.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                                ) {
                                    Text(
                                        text = "Confirm RSVP",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            else -> {
                                Button(
                                    onClick = {
                                        haptics.lightTick()
                                        launchDevfolioCustomTab(context, "https://${hackathon.slug}.devfolio.co")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(27.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                                ) {
                                    Text(
                                        text = "Apply on Devfolio",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }

                Box {
                    FilledTonalButton(
                        onClick = {
                            haptics.lightTick()
                            isMenuExpanded = !isMenuExpanded
                        },
                        modifier = Modifier.height(54.dp),
                        shape = RoundedCornerShape(27.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Options",
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        DropdownMenuItem(
                            text = { Text("Add to Calendar") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            onClick = {
                                haptics.lightTick()
                                isMenuExpanded = false
                                addHackathonToCalendar(
                                    context = context,
                                    hackathon = hackathon
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Share Hackathon") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            onClick = {
                                haptics.lightTick()
                                isMenuExpanded = false
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, hackathon.name)
                                    putExtra(Intent.EXTRA_TEXT, "Check out ${hackathon.name} on Devfolio: https://${hackathon.slug}.devfolio.co")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Hackathon"))
                            }
                        )
                    }
                }
            }
        }
    }
}
