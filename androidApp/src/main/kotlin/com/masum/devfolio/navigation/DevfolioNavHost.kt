package com.masum.devfolio.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.masum.devfolio.ui.detail.HackathonDetailScreen
import com.masum.devfolio.ui.main.MainScreen

@Composable
fun DevfolioNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DiscoverDestination,
        modifier = modifier
    ) {
        composable<DiscoverDestination>(
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            }
        ) {
            MainScreen(
                onHackathonClick = { hackathon ->
                    navController.navigate(DetailDestination(slug = hackathon.slug))
                }
            )
        }

        composable<DetailDestination>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<DetailDestination>()
            HackathonDetailScreen(
                slug = route.slug,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
