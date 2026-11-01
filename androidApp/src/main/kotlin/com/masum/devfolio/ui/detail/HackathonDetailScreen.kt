package com.masum.devfolio.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.theme.rememberDevfolioHaptics
import com.masum.devfolio.ui.components.FullScreenLoader

@Composable
fun HackathonDetailScreen(
    slug: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = remember(slug) { DetailViewModel(slug) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isBookmarked by viewModel.isBookmarked.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (uiState is DetailUiState.Success) {
                val hackathon = (uiState as DetailUiState.Success).hackathon
                val accentColor = remember(hackathon.primaryColor) {
                    hackathon.primaryColor?.let {
                        runCatching { Color(android.graphics.Color.parseColor(it)) }.getOrNull()
                    } ?: Color(0xFF3770FF)
                }

                ApplyActionBar(
                    hackathon = hackathon,
                    accentColor = accentColor
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is DetailUiState.Loading -> {
                    FullScreenLoader()
                }

                is DetailUiState.Error -> {
                    DetailErrorState(
                        message = state.message,
                        onRetry = viewModel::retry
                    )
                }

                is DetailUiState.Success -> {
                    DetailSuccessContent(
                        hackathon = state.hackathon,
                        isBookmarked = isBookmarked,
                        onBookmarkToggle = { viewModel.toggleBookmark(state.hackathon) },
                        onBackClick = onBackClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSuccessContent(
    hackathon: Hackathon,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = remember(hackathon.primaryColor) {
        hackathon.primaryColor?.let {
            runCatching { Color(android.graphics.Color.parseColor(it)) }.getOrNull()
        } ?: Color(0xFF3770FF)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        DetailHeroBanner(
            coverUrl = hackathon.coverImg,
            logoUrl = hackathon.logoImg,
            accentColor = accentColor,
            isBookmarked = isBookmarked,
            onBookmarkToggle = onBookmarkToggle,
            onBackClick = onBackClick
        )

        DetailHeaderInfo(
            name = hackathon.name,
            tagline = hackathon.tagline,
            location = hackathon.location,
            participantsCount = hackathon.participantsCount
        )

        DetailScheduleSection(
            regEndsAt = hackathon.regEndsAt,
            endsAt = hackathon.endsAt
        )

        DetailDescriptionSection(
            description = hackathon.description
        )

        DetailFaqSection(
            faqs = hackathon.faqs
        )

        DetailPlatformAttributionSection()

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun DetailErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberDevfolioHaptics()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Failed to load hackathon",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    haptics.heavyClick()
                    onRetry()
                }
            ) {
                Text(text = "Try Again")
            }
        }
    }
}
