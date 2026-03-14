package io.github.alest.photomapper.ui.components

//import com.example.shadow.lll_chat

import android.Manifest
import android.app.Activity
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image

import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
//import org.maplibre.compose.material3.CompassButton
//import org.maplibre.compose.material3.ExpandingAttributionButton
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.spatialk.geojson.GeoJsonObject

import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.github.alest.photomapper.db.DatabaseProvider
import io.github.alest.photomapper.db.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.maplibre.compose.expressions.dsl.asNumber
import org.maplibre.compose.expressions.dsl.asString
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.convertToNumber
import org.maplibre.compose.expressions.dsl.feature
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.dsl.not
import org.maplibre.compose.expressions.dsl.offset
import org.maplibre.compose.expressions.dsl.plus
import org.maplibre.compose.expressions.dsl.step
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.GeoJsonOptions
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import org.maplibre.spatialk.geojson.dsl.featureCollectionOf
import org.maplibre.spatialk.geojson.toJson
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import org.maplibre.spatialk.geojson.dsl.buildFeature
import org.maplibre.spatialk.geojson.dsl.buildFeatureCollection
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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
//                .padding(innerPadding)
        ) {
            OpenSourceMap(innerPadding = innerPadding)
        }
    }
}


@Composable
fun OpenSourceMap(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val db = DatabaseProvider.database
    val isDark = isSystemInDarkTheme()

    // 2. Select the appropriate URL
    val styleUrl = if (isDark) {
        "https://basemaps.cartocdn.com/gl/dark-matter-gl-style/style.json"
    } else {
        "https://tiles.openfreemap.org/styles/bright"
    }

//    val pointFeature = remember {
//        Feature.fromGeometry(Point.fromLngLat(13.4050, 52.5200))
//    }

    // We remember the MapView so we don't recreate it unnecessarily
    val context = LocalContext.current
//    val mapView = remember { MapView(context) }

    val cameraState = rememberCameraState()
    val styleState = rememberStyleState()

    // TODO: use ViewModel
    val photos = db.photoQueries.selectAll().executeAsList()

    Box(Modifier.fillMaxSize()) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                // true = Dark icons (for bright backgrounds)
                // false = White icons (for dark backgrounds)
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            }
        }

        MaplibreMap(
            baseStyle = BaseStyle.Uri(styleUrl),
            cameraState = cameraState,
//            styleState = styleState,
            options = MapOptions(
                ornamentOptions = OrnamentOptions(
                    padding = innerPadding,
                    // This enables the native scale bar
                    isScaleBarEnabled = false,
                    // You can also hide the logo here if you want
                    isLogoEnabled = false
                )
            ),
        ) {
            val source = rememberGeoJsonSource(
                data = GeoJsonData.Features(
                    buildFeatureCollection {
                        // TODO: prevent crash when no photos are scanned yet
                        photos.forEach { photo ->
                            add(
                                buildFeature {
                                    geometry = Point(Position(photo.longitude, photo.latitude))
                                    properties = buildJsonObject {
                                        put("id", photo.id)
                                        put("uri", photo.uri)
                                    }
                                }
                            )
                        }

                    }
                )
            )
//            SymbolLayer(
//                id = "amtrak-stations",
//                source = amtrakStations,
//                iconImage = painterResource(Res.drawable.ic_default_marker)
//            )

            CircleLayer(
                id = "amtrak-stations-test",
                source = source,
                color = const(Color.Red)
            )
        }

//        Box(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(8.dp)) {
////            ScaleBar(cameraState.metersPerDpAtTarget, modifier = Modifier.align(Alignment.TopStart))
//            CompassButton(cameraState, modifier = Modifier.align(Alignment.TopEnd))
//            ExpandingAttributionButton(
//                cameraState = cameraState,
//                styleState = styleState,
//                modifier = Modifier.align(Alignment.BottomEnd),
//                contentAlignment = Alignment.BottomEnd,
//            )
//        }
    }


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
//    DisposableEffect(mapView) {
//        mapView.onStart()
//        mapView.onResume()
//        onDispose {
//            mapView.onPause()
//            mapView.onStop()
//            mapView.onDestroy()
//        }
//    }
//
//    // This block handles the "Lifecycle" (OnResume, OnPause, etc.)
//    AndroidView(
//        modifier = modifier.fillMaxSize(),
//        factory = {
//            mapView.apply {
//                getMapAsync { map ->
//                    map.setStyle(styleUrl)
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0))
//
//                    map.addMarker(
//                        org.maplibre.android.annotations.MarkerOptions()
//                            .position(LatLng(50.76122, 25.383866))
//                            .title("Photo Location")
//                            .snippet("Lutsk, Ukraine")
//                    )
//                }
//            }
//        },
////        update = { view ->
////            view.getMapAsync { map ->
////                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0))
////            }
////        }
//    )
}