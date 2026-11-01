package com.masum.devfolio.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.masum.devfolio.data.repository.ProfileRepositoryImpl
import com.masum.devfolio.model.Hackathon
import com.masum.devfolio.model.Project
import com.masum.devfolio.ui.components.DevfolioFloatingNavBar
import com.masum.devfolio.ui.components.MainNavDestination
import com.masum.devfolio.ui.discover.DiscoverScreen
import com.masum.devfolio.ui.profile.ProfileScreen
import com.masum.devfolio.ui.profile.ProfileViewModel
import com.masum.devfolio.ui.projects.ProjectsScreen
import com.masum.devfolio.ui.saved.SavedScreen

@Composable
fun MainScreen(
    onHackathonClick: (Hackathon) -> Unit,
    onProjectClick: (Project) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDestination by rememberSaveable { mutableStateOf(MainNavDestination.Discover) }

    val profileRepository = remember { ProfileRepositoryImpl() }
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.provideFactory(profileRepository)
    )

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedDestination,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "main_tab_switch",
                modifier = Modifier.fillMaxSize()
            ) { destination ->
                when (destination) {
                    MainNavDestination.Discover -> {
                        DiscoverScreen(
                            onHackathonClick = onHackathonClick
                        )
                    }

                    MainNavDestination.Projects -> {
                        ProjectsScreen(
                            onProjectClick = onProjectClick
                        )
                    }

                    MainNavDestination.Saved -> {
                        SavedScreen(
                            onHackathonClick = onHackathonClick
                        )
                    }

                    MainNavDestination.Profile -> {
                        ProfileScreen(
                            onSettingsClick = onSettingsClick,
                            viewModel = profileViewModel
                        )
                    }
                }
            }

            DevfolioFloatingNavBar(
                selectedDestination = selectedDestination,
                onDestinationSelected = { destination ->
                    selectedDestination = destination
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
