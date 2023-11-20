package com.xcodeapps.autobrightnesstile

import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.xcodeapps.autobrightnesstile.SettingsModifier.toggleAdaptiveBrightness

class AdaptiveBrightnessTileService: TileService() {

    override fun onStartListening() {
        updateTileState()
        super.onStartListening()
    }

    private fun updateTileState() {
        if (isWriteSystemPermissionGranted(this)) {
            this.qsTile?.let {
                it.state = if (Settings.System.getInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
                    == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                ) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                it.updateTile()
            }
        }
    }

    override fun onClick() {
        runOrRequestPermissionAndCollapse(this) {
            toggleAdaptiveBrightness(this, qsTile)
        }
        super.onClick()
    }

}