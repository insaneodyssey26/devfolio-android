package com.masum.devfolio.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
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
import com.masum.devfolio.ui.about.AboutScreen
import com.masum.devfolio.ui.detail.HackathonDetailScreen
import com.masum.devfolio.ui.main.MainScreen
import com.masum.devfolio.ui.projects.detail.ProjectDetailScreen
import com.masum.devfolio.ui.settings.SettingsScreen

private val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
private val EmphasizedDecelerateEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

private const val NAV_ENTER_DURATION = 380
private const val NAV_EXIT_DURATION = 320

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
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(NAV_EXIT_DURATION, easing = EmphasizedEasing),
                    targetOffset = { (it * 0.15f).toInt() }
                ) + fadeOut(animationSpec = tween(NAV_EXIT_DURATION, easing = EmphasizedEasing))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(NAV_ENTER_DURATION, easing = EmphasizedDecelerateEasing),
                    initialOffset = { (it * 0.15f).toInt() }
                ) + fadeIn(animationSpec = tween(NAV_ENTER_DURATION, easing = EmphasizedDecelerateEasing))
            }
        ) {
            MainScreen(
                onHackathonClick = { hackathon ->
                    navController.navigate(DetailDestination(slug = hackathon.slug))
                },
                onProjectClick = { project ->
                    navController.navigate(ProjectDetailDestination(slug = project.slug))
                },
                onSettingsClick = {
                    navController.navigate(SettingsDestination)
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

        composable<SettingsDestination>(
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
        ) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onAboutClick = { navController.navigate(AboutDestination) }
            )
        }

        composable<AboutDestination>(
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
        ) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<ProjectDetailDestination>(
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
            val route = backStackEntry.toRoute<ProjectDetailDestination>()
            ProjectDetailScreen(
                slug = route.slug,
                onBackClick = { navController.popBackStack() },
                onHackathonClick = { hackathonSlug ->
                    navController.navigate(DetailDestination(slug = hackathonSlug))
                }
            )
        }
    }
}
