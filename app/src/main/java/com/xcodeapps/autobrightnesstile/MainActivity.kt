package com.xcodeapps.autobrightnesstile

import android.app.Activity
import android.app.StatusBarManager
import android.content.ComponentName
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Button
import com.xcodeapps.autobrightnesstile.PermissionsHelper.runWithWriteSettingsPermission
import com.xcodeapps.autobrightnesstile.SettingsModifier.getCurrentStateOfAutoBrightness
import com.xcodeapps.autobrightnesstile.SettingsModifier.toggleAdaptiveBrightness

class MainActivity : Activity() {

    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()
        requestAddTileService()
        button = findViewById(R.id.auto_brightness_button)
        button?.setOnClickListener {
            runWithWriteSettingsPermission(this) {
                toggleAdaptiveBrightness(this)
            }
            refreshButtonState()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshButtonState()
    }

    private fun refreshButtonState() {
        if (getCurrentStateOfAutoBrightness(this) == SettingsModifier.AutoBrightnessState.AUTO_BRIGHTNESS_ENABLED) {
            button?.text = "Auto Brightness ON"
            button?.background?.setTint(Color.parseColor("#673AB7"))
        } else {
            button?.text = "Auto Brightness OFF"
            button?.background?.setTint(Color.parseColor("#546E7A"))
        }
    }

    private fun requestAddTileService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val statusBarManager: StatusBarManager = getSystemService(StatusBarManager::class.java)
            statusBarManager.requestAddTileService(
                ComponentName(this, AdaptiveBrightnessTileService::class.java),
                getString(R.string.app_name),
                Icon.createWithResource(this, R.drawable.ic_auto_brightness),
                {},
            ) {}
        }
    }
}