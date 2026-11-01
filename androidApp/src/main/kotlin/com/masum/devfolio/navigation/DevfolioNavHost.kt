package com.masum.devfolio.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.masum.devfolio.ui.detail.HackathonDetailScreen
import com.masum.devfolio.ui.main.MainScreen

private val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
private val EmphasizedDecelerateEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

private const val NAV_ENTER_DURATION = 420
private const val NAV_EXIT_DURATION = 350

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
                scaleOut(
                    targetScale = 0.95f,
                    animationSpec = tween(NAV_EXIT_DURATION, easing = EmphasizedEasing)
                ) + fadeOut(animationSpec = tween(NAV_EXIT_DURATION, easing = EmphasizedEasing))
            },
            popEnterTransition = {
                scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(NAV_ENTER_DURATION, easing = EmphasizedDecelerateEasing)
                ) + fadeIn(animationSpec = tween(NAV_ENTER_DURATION / 2, easing = EmphasizedDecelerateEasing))
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
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(NAV_ENTER_DURATION, easing = EmphasizedDecelerateEasing)
                ) + fadeIn(animationSpec = tween(NAV_ENTER_DURATION / 2, easing = EmphasizedDecelerateEasing))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(NAV_EXIT_DURATION, easing = EmphasizedEasing),
                    targetOffset = { it }
                ) + fadeOut(animationSpec = tween(NAV_EXIT_DURATION / 2, easing = EmphasizedEasing))
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
