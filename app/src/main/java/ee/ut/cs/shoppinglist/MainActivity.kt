package ee.ut.cs.shoppinglist

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import ee.ut.cs.shoppinglist.ui.theme.ShoppingListTheme
import kotlin.text.compareTo


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationPermissionManager(this).requestIfNeeded()
        enableEdgeToEdge()
        val app = (application as ShoppingListApplication)
        setContent {

            ShoppingListTheme {
                AppNav(
                    app.navCoordinator,
                    shoppingListRepository = app.shoppingListRepository,
                    shoppingItemDetailsRepository = app.shoppingItemDetailsRepository,
                    viewModeRepository = app.viewModeRepository
                )
            }
        }
    }
}

