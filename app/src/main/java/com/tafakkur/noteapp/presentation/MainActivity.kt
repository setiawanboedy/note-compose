package com.tafakkur.noteapp.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.tafakkur.noteapp.ui.theme.noteappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display
        enableEdgeToEdge()
        
        // Configure system UI
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            noteappTheme {
                val view = LocalView.current
                val darkTheme = isSystemInDarkTheme()
                
                // Configure status bar color
                SideEffect {
                    val window = (view.context as ComponentActivity).window
                    window.statusBarColor = Color.Transparent.toArgb()
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                }
                
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NoteUIApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    noteappTheme {
        NoteUIApp()
    }
}