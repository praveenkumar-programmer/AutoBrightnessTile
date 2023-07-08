package com.xcodeapps.autobrightnesstile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat

object PermissionsHelper {

    fun runWithWriteSettingsPermission(context: Context, codeBlock: () -> Unit) {
        if (isWriteSystemPermissionGranted(context)) {
            codeBlock.invoke()
        } else {
            redirectToPermissionsSettingsPage(context)
        }
    }

    fun isWriteSystemPermissionGranted(context: Context): Boolean {
        return Settings.System.canWrite(context)
    }

    fun redirectToPermissionsSettingsPage(context: Context) {
        Toast.makeText(context, "Please enable permission!", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("package:${context.packageName}")
        ContextCompat.startActivity(context, intent, null)
    }
}