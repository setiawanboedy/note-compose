package com.tafakkur.noteapp.presentation.navigation

sealed class Screen(val route: String){
    object Home : Screen("home")
    object About: Screen("about")
    object Add: Screen("add?noteId={noteId}&noteColor={noteColor}"){
        fun createRoute(noteId: Int, noteColor: Int) = "add?noteId=$noteId&noteColor=$noteColor"
    }
    object DetailNote: Screen("home/{noteId}"){
        fun createRoute(noteId: Int) = "home/$noteId"
    }
}
