package io.github.alest.photomapper.ui.components

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import io.github.alest.photomapper.db.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import io.github.alest.photomapper.util.*

@Composable
fun PermissionFeatureBlock(
    onGrantClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // Optional: You can put a TopBar here that stays visible for both screens
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
//            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(64.dp))
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Photos Access Required",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Photo Mapper needs access to all your photos to display them on the map. " +
                            "Without this, the app cannot function.",
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Button(onClick = onGrantClick) {
                    Text("Grant Permission")
                }
            }
        }
    }
}



@Composable
fun DeniedPermissionFeatureBlock(
    onOpenSettings: () -> Unit,
    onPermissionCheck: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // Optional: You can put a TopBar here that stays visible for both screens
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
//            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(64.dp))
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Photos Access Required",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Photo Mapper needs access to all your photos to display them on the map. " +
                            "Go to settings to grant access to all photos. Without this, the app cannot function.",
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))

                Row {
                    Button(onClick = onOpenSettings) {
                        Text("Open app settings")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(onClick = onPermissionCheck) {
                        Text("Check permission")
                    }
                }
            }
        }
    }
}