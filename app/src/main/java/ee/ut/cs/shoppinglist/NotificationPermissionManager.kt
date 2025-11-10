package ee.ut.cs.shoppinglist

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.play.integrity.internal.ac

class NotificationPermissionManager(
    private val activity: ComponentActivity,
) {

    private val PACKAGE_SCHEMA = "package"
    private val launcher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                showNotificationsDisabledDialog()
            }
        }

    fun requestIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        when (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)) {
            PackageManager.PERMISSION_GRANTED -> {

            }

            else -> {
                if (activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    showRationaleDialog()
                } else {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showRationaleDialog() {
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.title_notification_rationale_dialog))
            .setMessage(
                activity.getString(R.string.message_notification_rationale_dialog)
            )
            .setPositiveButton(activity.getString(R.string.btn_allow)) { _, _ ->
                launcher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
            .setNegativeButton(activity.getString(R.string.btn_decline_notifications), null)
            .show()
    }

    private fun showNotificationsDisabledDialog() {
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.title_notifications_disabled_dialog))
            .setMessage(activity.getString(R.string.message_notifications_disabled_dialog))
            .setPositiveButton(activity.getString(R.string.btn_open_settings)) { _, _ -> openAppNotificationSettings() }
            .setNegativeButton(activity.getString(R.string.btn_dismiss), null)
            .show()
    }

    private fun openAppNotificationSettings() {
        val intent = Intent().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            } else {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts(PACKAGE_SCHEMA, activity.packageName, null)
            }
        }
        activity.startActivity(intent)
    }


}