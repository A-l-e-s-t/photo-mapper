package io.github.alest.photomapper

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    var isStorageDenied by remember { mutableStateOf(false) }
    var isLocationDenied by remember { mutableStateOf(false) }

    // 1. Create a state to track if we should show the app or the "permission wall"
    // We initialize it by checking the current status
    var hasStoragePermission by remember {
        mutableStateOf(
            checkPermission(context, storagePermission)
        )
    }
    var hasLocationPermission by remember {
        mutableStateOf(
            checkPermission(context, locationPermission)
        )
    }

    val storageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasStoragePermission = isGranted // Update UI state based on user choice

        if (!hasStoragePermission) {
            isStorageDenied = true
        }
    }
    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted // Update UI state based on user choice

        if (!hasLocationPermission) {
            isLocationDenied = true
        }
    }

//    val showStorageRationale = activity?.let {
//        ActivityCompat.shouldShowRequestPermissionRationale(it, storagePermission)
//    } ?: false
//
//    val showLocationRationale = activity?.let {
//        ActivityCompat.shouldShowRequestPermissionRationale(it, locationPermission)
//    } ?: false

    when {
        hasStoragePermission && hasLocationPermission -> MainAppContent()

        !hasStoragePermission -> {
            if (isStorageDenied) {
                DeniedPermissionFeatureBlock(
                    title = "Photos Access Required",
                    content = "Photo Mapper needs access to all your photos to display them on the map. " +
                            "Go to settings to grant access to all photos. Without this, the app cannot function.",
                    onOpenSettings = { openAppSettings(context) },
                    onPermissionCheck = { hasStoragePermission = checkPermission(context, storagePermission) }
                )
            } else {
                PermissionFeatureBlock(
                    title = "Photos Access Required",
                    content = "Photo Mapper needs access to all your photos to display them on the map. Without this, the app cannot function.",
                    onGrantClick = { storageLauncher.launch(storagePermission) }
                )
            }
        }

        !hasLocationPermission -> {
            if (isLocationDenied) {
                DeniedPermissionFeatureBlock(
                    title = "Precise location Access Required",
                    content = "Photo Mapper needs precise location access to display photos on the map. " +
                            "Go to settings to grant precise location access. Without this, the app cannot function.",
                    onOpenSettings = { openAppSettings(context) },
                    onPermissionCheck = { hasLocationPermission = checkPermission(context, locationPermission) }
                )
            } else {
                PermissionFeatureBlock(
                    title = "Precise location Access Required",
                    content = "Photo Mapper needs precise location access to display photos on the map. Without this, the app cannot function.",
                    onGrantClick = { locationLauncher.launch(locationPermission) }
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
