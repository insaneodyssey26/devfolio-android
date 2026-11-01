package com.masum.devfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.masum.devfolio.data.settings.ThemePreferences
import com.masum.devfolio.navigation.DevfolioNavHost
import com.masum.devfolio.theme.DevfolioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val themePreferences = remember { ThemePreferences.getInstance(applicationContext) }
            val settings by themePreferences.settings.collectAsState()

            DevfolioTheme(
                themeMode = settings.themeMode,
                colorTheme = settings.colorTheme,
                pureBlack = settings.pureBlack,
                fontPreference = settings.fontPreference
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DevfolioNavHost()
                }
            }
        }
    }
}
