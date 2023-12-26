package com.tafakkur.subcompose.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tafakkur.subcompose.presentation.about.AboutScreen
import com.tafakkur.subcompose.presentation.add.AddDiaryScreen
import com.tafakkur.subcompose.presentation.detail.DetailDiaryScreen
import com.tafakkur.subcompose.presentation.home.HomeScreen
import com.tafakkur.subcompose.presentation.navigation.NavigationItem
import com.tafakkur.subcompose.presentation.navigation.Screen
import com.tafakkur.subcompose.presentation.utils.DIARY_COLOR
import com.tafakkur.subcompose.presentation.utils.DIARY_ID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryUIApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailDiary.route && currentRoute != Screen.Add.route){
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) {innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)){
            composable(Screen.Home.route){
                HomeScreen(
                    navigateToDetail = {diaryId ->
                        navController.navigate(Screen.DetailDiary.createRoute(diaryId))
                    },
                    navigateToAdd = {
                        navController.navigate(Screen.Add.route)
                    }
                )
            }
            composable(Screen.About.route){
                AboutScreen()
            }
            composable(
                route = Screen.Add.route,
                arguments = listOf(
                    navArgument(
                        name = DIARY_ID
                    ){
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument(
                        name = DIARY_COLOR
                    ){
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ){
                val color = it.arguments?.getInt(DIARY_COLOR) ?: -1

                AddDiaryScreen(navigateBack = {
                    navController.navigateUp()

                },
                    diaryColor = color)
            }
            composable(
                route = Screen.DetailDiary.route,
                arguments = listOf(
                    navArgument(DIARY_ID){
                    type = NavType.IntType
                },
                )
            ){
                DetailDiaryScreen(
                    navigateToEdit = {diaryId, color ->
                        navController.navigate(Screen.Add.createRoute(diaryId, color))
                    },
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        modifier = modifier
    ) {
        val navigationItems = listOf(
            NavigationItem(
                title = "Home",
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = "About",
                icon = Icons.Default.AccountCircle,
                screen = Screen.About
            )
        )
        navigationItems.map { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                label = {
                    Text(text = item.title)
                },
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}