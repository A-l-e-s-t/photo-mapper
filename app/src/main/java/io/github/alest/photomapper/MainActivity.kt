package io.github.alest.photomapper

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

import io.github.alest.photomapper.ui.components.*
import io.github.alest.photomapper.ui.theme.AppTheme
import io.github.alest.photomapper.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                PhotoMapperApp()
            }
        }
    }
}


@Composable
fun PhotoMapperApp() {
    val context = LocalContext.current
    val activity = context as? Activity

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.ACCESS_MEDIA_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION
        )
    }

    var isStorageDenied by remember { mutableStateOf(false) }

    // 1. Create a state to track if we should show the app or the "permission wall"
    // We initialize it by checking the current status
    var hasStoragePermission by remember {
        mutableStateOf(
            checkPermission(context, storagePermission)
        )
    }

    val storageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions() // 1. Change contract
    ) { permissionsMap ->
        val allGranted = permissionsMap.values.all { it }

        hasStoragePermission = allGranted
        if (!allGranted) {
            isStorageDenied = true
        }
    }

//    val showStorageRationale = activity?.let {
//        ActivityCompat.shouldShowRequestPermissionRationale(it, storagePermission)
//    } ?: false

    when {
        hasStoragePermission -> MainAppContent()
        !hasStoragePermission -> {
            if (isStorageDenied) {
                DeniedPermissionFeatureBlock(
                    onOpenSettings = { openAppSettings(context) },
                    onPermissionCheck = { hasStoragePermission = checkPermission(context, storagePermission) }
                )
            } else {
                PermissionFeatureBlock(
                    onGrantClick = {
                        storageLauncher.launch(storagePermission)
                    }
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MainAppContent() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.HOME -> HomeDestination()
            AppDestinations.MAP -> MapDestination()
            AppDestinations.SETTINGS -> SettingsDestination()
        }
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            if (currentDestination == AppDestinations.HOME) {
//                HomeScreen()
//            }
//        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    MAP("Map", Icons.Default.LocationOn),
    SETTINGS("Settings", Icons.Default.Settings),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
