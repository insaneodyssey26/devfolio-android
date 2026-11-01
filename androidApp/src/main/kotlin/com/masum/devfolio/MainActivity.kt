package com.masum.devfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.masum.devfolio.navigation.DevfolioNavHost
import com.masum.devfolio.theme.DevfolioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DevfolioTheme {
                DevfolioNavHost()
            }
        }
    }
}
