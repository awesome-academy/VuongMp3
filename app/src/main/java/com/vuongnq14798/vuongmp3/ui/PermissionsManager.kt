package com.vuongnq14798.vuongmp3.ui

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vuongnq14798.vuongmp3.R
import com.vuongnq14798.vuongmp3.util.ContextExtensions.toast

class PermissionsManager(
    private val activity: Activity,
    private val permissions: List<String>,
    private val code: Int
) {

    fun checkPermissions() {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        } else {
            activity.toast(activity.getString(R.string.permission_granted))
        }
    }

    private fun isPermissionsGranted(): Int {
        var counter = 0
        for (permission in permissions) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }

    private fun deniedPermission(): String {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED
            ) return permission
        }
        return ""
    }

    private fun showAlert() =
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.title_permission_message))
            .setMessage(activity.getString(R.string.mess_permission))
            .setPositiveButton(activity.getString(R.string.ok)) { _, _ -> requestPermissions() }
            .setNeutralButton(activity.getString(R.string.cancel), null)
            .create()
            .show()

    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            activity.toast(activity.getString(R.string.should_show_an_explanation))
        } else {
            ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), code)
        }
    }

    fun processPermissionsResult(grantResults: IntArray): Boolean =
        grantResults.size == PackageManager.PERMISSION_GRANTED
}
