package io.github.alest.photomapper.ui.components

//import com.example.shadow.lll_chat

import android.Manifest
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

//import com.example.shadow.db.DatabaseProvider
//import com.example.shadow.db.Model
//import com.example.shadow.db.LlmEndpoint
////import com.example.shadow.llm.MainViewModel
//import com.example.shadow.llama.*
//import com.example.shadow.llm.*
//import com.example.shadow.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDestination(modifier: Modifier = Modifier) {
    val context = LocalContext.current


    // Scaffold provides the structure for the TopBar and handles system insets
    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Settings") },
//            )
//        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OpenSourceMap(50.76822, 25.383766)
        }
    }
}


@Composable
fun OpenSourceMap(latitude: Double, longitude: Double, modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()

    // 2. Select the appropriate URL
    val styleUrl = if (isDark) {
        "https://basemaps.cartocdn.com/gl/dark-matter-gl-style/style.json"
    } else {
        "https://tiles.openfreemap.org/styles/bright"
    }

    val point = LatLng(latitude, longitude)

    // We remember the MapView so we don't recreate it unnecessarily
    val context = LocalContext.current
    val mapView = remember { MapView(context) }


    // 1. We create a "Hidden" view that Compose will render into a Bitmap
//    val markerView = ComposeView(context).apply {
//        setContent {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                // THE HOVERING IMAGE (The "Bubble")
//                Surface(
//                    shape = RoundedCornerShape(12.dp),
//                    shadowElevation = 8.dp,
//                    border = BorderStroke(2.dp, Color.White),
//                    modifier = Modifier.size(80.dp) // Size of the floating preview
//                ) {
//                    AsyncImage(
//                        model = photoUri,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop
//                    )
//                }
//
//                // THE STEM/POINTER (The "Triangle")
//                Box(
//                    modifier = Modifier
//                        .size(15.dp)
//                        .graphicsLayer { rotationZ = 45f }
//                        .offset(y = (-8).dp)
//                        .background(Color.White)
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                // THE CUSTOM PIN (The "Base")
//                Icon(
//                    imageVector = Icons.Default.LocationOn,
//                    contentDescription = null,
//                    tint = Color.Red,
//                    modifier = Modifier.size(40.dp)
//                )
//            }
//        }
//    }


    // IMPORTANT: Maps need to be told to start/stop with the app
    DisposableEffect(mapView) {
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    // This block handles the "Lifecycle" (OnResume, OnPause, etc.)
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            mapView.apply {
                getMapAsync { map ->
                    map.setStyle(styleUrl)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0))

                    map.addMarker(
                        org.maplibre.android.annotations.MarkerOptions()
                            .position(LatLng(50.76122, 25.383866))
                            .title("Photo Location")
                            .snippet("Lutsk, Ukraine")
                    )
                }
            }
        },
//        update = { view ->
//            view.getMapAsync { map ->
//                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0))
//            }
//        }
    )
}