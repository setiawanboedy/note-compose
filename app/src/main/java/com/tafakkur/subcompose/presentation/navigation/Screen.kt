package com.tafakkur.subcompose.presentation.navigation

sealed class Screen(val route: String){
    object Home : Screen("home")
    object About: Screen("about")
    object Add: Screen("add?diaryId={diaryId}&diaryColor={diaryColor}"){
        fun createRoute(diaryId: Int, diaryColor: Int) = "add?diaryId=$diaryId&diaryColor=$diaryColor"
    }
    object DetailDiary: Screen("home/{diaryId}"){
        fun createRoute(diaryId: Int) = "home/$diaryId"
    }
}
