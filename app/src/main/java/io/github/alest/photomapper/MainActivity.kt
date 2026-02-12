package io.github.alest.photomapper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

import io.github.alest.photomapper.ui.components.*
import io.github.alest.photomapper.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                PhotoOptimizerApp()
            }
        }
    }
}


@Composable
fun PhotoOptimizerApp() {
    val context = LocalContext.current
    val activity = context as? Activity

    val storagePermission = Manifest.permission.READ_MEDIA_IMAGES
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    // 1. Create a state to track if we should show the app or the "permission wall"
    // We initialize it by checking the current status
    var hasStoragePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, locationPermission) == PackageManager.PERMISSION_GRANTED
        )
    }

    val storageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasStoragePermission = isGranted // Update UI state based on user choice
    }
    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted // Update UI state based on user choice
    }

    val showStorageRationale = activity?.let {
        ActivityCompat.shouldShowRequestPermissionRationale(it, storagePermission)
    } ?: false

    when {
        !hasStoragePermission -> {
            PermissionFeatureBlock(
                title = "Storage Access Required",
                content = "Photo Mapper needs to access your photos to display them on the map. Without this, the app cannot function.",
                onGrantClick = { storageLauncher.launch(storagePermission) }
            )
        }
        !hasLocationPermission -> {
            PermissionFeatureBlock(
                title = "Precise location Access Required",
                content = "Photo Mapper needs precise location access to display photos them on the map. Without this, the app cannot function.",
                onGrantClick = { locationLauncher.launch(locationPermission) }
            )
        }
        else -> {
            MainAppContent()
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
