package com.tafakkur.noteapp.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.tafakkur.noteapp.presentation.about.AboutScreen
import com.tafakkur.noteapp.presentation.add.AddNoteScreen
import com.tafakkur.noteapp.presentation.detail.DetailNoteScreen
import com.tafakkur.noteapp.presentation.home.HomeScreen
import com.tafakkur.noteapp.presentation.navigation.NavigationItem
import com.tafakkur.noteapp.presentation.navigation.Screen
import com.tafakkur.noteapp.presentation.utils.NOTE_COLOR
import com.tafakkur.noteapp.presentation.utils.NOTE_ID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteUIApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            // BottomBar disabled
            // if (currentRoute != Screen.DetailNote.route && currentRoute != Screen.Add.route){
            //     BottomBar(navController)
            // }
        },
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)){
            composable(Screen.Home.route){
                HomeScreen(
                    navigateToDetail = {noteId ->
                        navController.navigate(Screen.DetailNote.createRoute(noteId))
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
                        name = NOTE_ID
                    ){
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument(
                        name = NOTE_COLOR
                    ){
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ){
                val color = it.arguments?.getInt(NOTE_COLOR) ?: -1

                AddNoteScreen(navigateBack = {
                    navController.navigateUp()

                },
                    noteColor = color)
            }
            composable(
                route = Screen.DetailNote.route,
                arguments = listOf(
                    navArgument(NOTE_ID){
                    type = NavType.IntType
                },
                )
            ){
                DetailNoteScreen(
                    navigateToEdit = {noteId, color ->
                        navController.navigate(Screen.Add.createRoute(noteId, color))
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