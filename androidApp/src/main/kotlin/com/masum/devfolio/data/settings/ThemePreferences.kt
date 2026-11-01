package com.masum.devfolio.data.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettings(
    val themeMode: String = "system",
    val colorTheme: String = "devfolio",
    val pureBlack: Boolean = false,
    val hapticsEnabled: Boolean = true,
    val fontPreference: String = "google_sans"
)

class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("devfolio_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private fun loadSettings(): AppSettings {
        return AppSettings(
            themeMode = prefs.getString(KEY_THEME_MODE, "system") ?: "system",
            colorTheme = prefs.getString(KEY_COLOR_THEME, "devfolio") ?: "devfolio",
            pureBlack = prefs.getBoolean(KEY_PURE_BLACK, false),
            hapticsEnabled = prefs.getBoolean(KEY_HAPTICS_ENABLED, true),
            fontPreference = prefs.getString(KEY_FONT_PREFERENCE, "google_sans") ?: "google_sans"
        )
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply()
        _settings.value = _settings.value.copy(themeMode = mode)
    }

    fun setColorTheme(theme: String) {
        prefs.edit().putString(KEY_COLOR_THEME, theme).apply()
        _settings.value = _settings.value.copy(colorTheme = theme)
    }

    fun setPureBlack(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PURE_BLACK, enabled).apply()
        _settings.value = _settings.value.copy(pureBlack = enabled)
    }

    fun setHapticsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, enabled).apply()
        _settings.value = _settings.value.copy(hapticsEnabled = enabled)
    }

    fun setFontPreference(font: String) {
        prefs.edit().putString(KEY_FONT_PREFERENCE, font).apply()
        _settings.value = _settings.value.copy(fontPreference = font)
    }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_COLOR_THEME = "color_theme"
        private const val KEY_PURE_BLACK = "pure_black"
        private const val KEY_HAPTICS_ENABLED = "haptics_enabled"
        private const val KEY_FONT_PREFERENCE = "font_preference"

        @Volatile
        private var instance: ThemePreferences? = null

        fun getInstance(context: Context): ThemePreferences {
            return instance ?: synchronized(this) {
                instance ?: ThemePreferences(context.applicationContext).also { instance = it }
            }
        }
    }
}
