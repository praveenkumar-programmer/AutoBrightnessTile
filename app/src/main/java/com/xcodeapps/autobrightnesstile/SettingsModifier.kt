package com.xcodeapps.autobrightnesstile

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.service.quicksettings.Tile
import android.widget.Toast

object SettingsModifier {

    fun getCurrentStateOfAutoBrightness(context: Context): AutoBrightnessState {
        return if (Settings.System.getInt(
                context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE
            ) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        ) {
            AutoBrightnessState.AUTO_BRIGHTNESS_ENABLED
        } else {
            AutoBrightnessState.AUTO_BRIGHTNESS_DISABLED
        }
    }

    fun toggleAdaptiveBrightness(context: Context, tile: Tile? = null): AutoBrightnessState {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("preferences", 0)
        try {
            return if (getCurrentStateOfAutoBrightness(context) == AutoBrightnessState.AUTO_BRIGHTNESS_ENABLED) {
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
                )
                val currentBrightnessValue = Settings.System.getInt(
                    context.contentResolver, Settings.System.SCREEN_BRIGHTNESS
                )
                val newBrightnessValue =
                    sharedPreferences.getInt("current_brightness", currentBrightnessValue)
                Settings.System.putInt(
                    context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, newBrightnessValue
                )
                tile?.state = Tile.STATE_INACTIVE
                tile?.updateTile()
                Toast.makeText(context, "Adaptive brightness disabled", Toast.LENGTH_SHORT).show()
                AutoBrightnessState.AUTO_BRIGHTNESS_DISABLED
            } else {
                val currentBrightnessValue = Settings.System.getInt(
                    context.contentResolver, Settings.System.SCREEN_BRIGHTNESS
                )
                sharedPreferences.edit().putInt("current_brightness", currentBrightnessValue)
                    .apply()
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                )
                tile?.state = Tile.STATE_ACTIVE
                tile?.updateTile()
                Toast.makeText(context, "Adaptive brightness enabled", Toast.LENGTH_SHORT).show()
                AutoBrightnessState.AUTO_BRIGHTNESS_ENABLED
            }
        } catch (e: Settings.SettingNotFoundException) {
            Toast.makeText(context, "Setting Not Found!", Toast.LENGTH_SHORT).show()
        }
        return AutoBrightnessState.UNKNOWN
    }

    enum class AutoBrightnessState {
        AUTO_BRIGHTNESS_ENABLED, AUTO_BRIGHTNESS_DISABLED, UNKNOWN
    }

}