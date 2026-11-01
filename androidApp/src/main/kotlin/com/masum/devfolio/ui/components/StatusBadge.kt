package com.masum.devfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.masum.devfolio.theme.PillShape
import com.masum.devfolio.theme.StatusInPerson
import com.masum.devfolio.theme.StatusOnline
import com.masum.devfolio.theme.StatusOpen
import com.masum.devfolio.theme.StatusUrgent

enum class EventStatusType {
    OPEN, UPCOMING, ENDED, ONLINE, IN_PERSON
}

@Composable
fun StatusBadge(
    status: EventStatusType,
    modifier: Modifier = Modifier
) {
    val (label, dotColor, containerColor) = when (status) {
        EventStatusType.OPEN -> Triple("Open", StatusOpen, StatusOpen.copy(alpha = 0.12f))
        EventStatusType.UPCOMING -> Triple("Upcoming", StatusInPerson, StatusInPerson.copy(alpha = 0.12f))
        EventStatusType.ENDED -> Triple("Ended", Color.Gray, Color.Gray.copy(alpha = 0.12f))
        EventStatusType.ONLINE -> Triple("Online", StatusOnline, StatusOnline.copy(alpha = 0.12f))
        EventStatusType.IN_PERSON -> Triple("In Person", StatusInPerson, StatusInPerson.copy(alpha = 0.12f))
    }

    Row(
        modifier = modifier
            .background(containerColor, PillShape)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = dotColor
        )
    }
}
