package com.xcodeapps.autobrightnesstile

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.TileService
import android.widget.Toast

fun runOrRequestPermission(context: Context, codeBlock: () -> Unit) {
    if (isWriteSystemPermissionGranted(context)) {
        codeBlock.invoke()
    } else {
        Toast.makeText(context, "Please enable permission!", Toast.LENGTH_SHORT).show()
        context.startActivity(getManageWriteSettingsPageIntent(context))
    }
}

fun runOrRequestPermissionAndCollapse(tileService: TileService, codeBlock: () -> Unit) {
    if (isWriteSystemPermissionGranted(tileService)) {
        codeBlock.invoke()
    } else {
        if (Build.VERSION.SDK_INT >= 34) {
            tileService.startActivityAndCollapse(
                PendingIntent.getActivity(
                    tileService,
                    45,
                    getManageWriteSettingsPageIntent(tileService),
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
        } else {
            tileService.startActivityAndCollapse(getManageWriteSettingsPageIntent(tileService))
        }
    }
}

fun isWriteSystemPermissionGranted(context: Context): Boolean {
    return Settings.System.canWrite(context)
}


fun getManageWriteSettingsPageIntent(context: Context): Intent {
    return Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        data = Uri.parse("package:${context.packageName}")
    }
}