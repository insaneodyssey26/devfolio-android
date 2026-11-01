package com.masum.devfolio.ui.discover

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.theme.SquircleBadgeShape
import com.masum.devfolio.theme.rememberDevfolioHaptics
import com.masum.devfolio.ui.components.CountdownTimerBadge
import com.masum.devfolio.ui.components.EventStatusType
import com.masum.devfolio.ui.components.StatusBadge

@Composable
fun HackathonCard(
    hackathon: Hackathon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberDevfolioHaptics()
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        haptics.lightTick()
                        onClick()
                    }
                )
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            HackathonCoverBanner(
                coverUrl = hackathon.coverImg,
                primaryColor = hackathon.primaryColor,
                isOnline = hackathon.isOnline,
                regEndsAt = hackathon.regEndsAt
            )

            HackathonCardContent(
                name = hackathon.name,
                tagline = hackathon.tagline,
                logoUrl = hackathon.logoImg,
                city = hackathon.city,
                state = hackathon.state,
                country = hackathon.country,
                participantsCount = hackathon.participantsCount
            )
        }
    }
}

@Composable
private fun HackathonCoverBanner(
    coverUrl: String?,
    primaryColor: String?,
    isOnline: Boolean,
    regEndsAt: String?
) {
    val accentColor = remember(primaryColor) {
        primaryColor?.let {
            runCatching { Color(android.graphics.Color.parseColor(it)) }.getOrNull()
        } ?: Color(0xFF3770FF)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 8f)
            .background(accentColor.copy(alpha = 0.15f))
    ) {
        if (!coverUrl.isNullOrBlank()) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBadge(
                status = if (isOnline) EventStatusType.ONLINE else EventStatusType.IN_PERSON
            )

            CountdownTimerBadge(deadlineIso = regEndsAt)
        }
    }
}

@Composable
private fun HackathonCardContent(
    name: String,
    tagline: String,
    logoUrl: String?,
    city: String?,
    state: String?,
    country: String?,
    participantsCount: Int
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!logoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(SquircleBadgeShape)
                        .border(1.dp, MaterialTheme.colorScheme.outline, SquircleBadgeShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val locationText = listOfNotNull(city, state, country).joinToString(", ")
                if (locationText.isNotEmpty()) {
                    Text(
                        text = locationText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        if (tagline.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = tagline,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (participantsCount > 0) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$participantsCount builders participating",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
