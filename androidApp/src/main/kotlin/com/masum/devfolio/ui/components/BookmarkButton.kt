package com.masum.devfolio.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.masum.devfolio.theme.rememberDevfolioHaptics

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Black.copy(alpha = 0.5f)
) {
    val haptics = rememberDevfolioHaptics()

    val scale by animateFloatAsState(
        targetValue = if (isBookmarked) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val iconColor by animateColorAsState(
        targetValue = if (isBookmarked) MaterialTheme.colorScheme.primary else Color.White
    )

    IconButton(
        onClick = {
            haptics.heavyClick()
            onToggle()
        },
        modifier = modifier
            .size(36.dp)
            .scale(scale)
            .background(containerColor, CircleShape)
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
            contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark",
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}
