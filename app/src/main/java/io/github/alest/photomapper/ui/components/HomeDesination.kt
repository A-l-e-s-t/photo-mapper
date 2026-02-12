package io.github.alest.photomapper.ui.components

//import com.example.shadow.lll_chat

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import io.github.alest.photomapper.db.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import io.github.alest.photomapper.util.*

//import com.example.shadow.db.DatabaseProvider
//import com.example.shadow.db.Model
//import com.example.shadow.db.LlmEndpoint
////import com.example.shadow.llm.MainViewModel
//import com.example.shadow.llama.*
//import com.example.shadow.llm.*
//import com.example.shadow.util.*


//data class Location(
//    val latitude: Float,
//    val longitude: Float
//)


suspend fun scanAllPhotos(context: Context) {
    withContext(Dispatchers.IO) {
        val db = DatabaseProvider.database

        val processedUris = db.photoQueries.selectAll()
            .executeAsList()
            .map { it.uri }
            .toHashSet() // HashSet provides O(1) lookup speed

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        context.contentResolver.query(
            collection,
            arrayOf(MediaStore.Images.Media._ID),
            "${MediaStore.Images.Media.MIME_TYPE} = ?",
            arrayOf("image/jpeg"),
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
    //            println("uri: $uri")

                if (processedUris.contains(uri.toString())) {
                    continue
                }

                // NOW: Run your EXIF logic for this specific URI
                val metadata = extractPhotoMetadata(context, uri)
                if (metadata != null) {
                    println("latitude: ${metadata.latitude}, longitude: ${metadata.longitude}, date: ${metadata.dateTaken}")

                    val photo = db.photoQueries.selectByUri(metadata.uri.toString()).executeAsOneOrNull()

                    if (photo == null) {
                        db.photoQueries.insert(
                            metadata.uri.toString(),
                            metadata.dateTaken,
                            metadata.latitude,
                            metadata.longitude,
                        )
                    }
                }
            }
        }
}
}


@Composable
fun rememberPhotoPickerLauncher(
    onPhotoPicked: (PhotoMetadata?) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    val context = LocalContext.current

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Logic to extract all info
            val metadata = extractPhotoMetadata(context, uri)
            onPhotoPicked(metadata)
        }
    }
}


data class PhotoMetadata(
    val uri: Uri,
    val dateTaken: String,
    val latitude: Double,
    val longitude: Double
)


fun extractPhotoMetadata(
    context: Context,
    uri: Uri
): PhotoMetadata? {
    var dateTaken: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
//    var altitude: Double? = null

    // B. GET INTERNAL INFO (Dimensions, Specs, Orientation)
    try {
        val photoUri = MediaStore.setRequireOriginal(uri)
        context.contentResolver.openInputStream(photoUri)?.use { inputStream ->
            val exif = ExifInterface(inputStream)

//            altitude = exif.getAltitude(-999.9).takeIf { it != -999.9 }?.toFloat()

            // 1. Extract Basic Info
            dateTaken = exif.getAttribute(ExifInterface.TAG_DATETIME)
//            println("""
//                |TAG_GPS_LATITUDE: ${exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)}
//                |TAG_GPS_LATITUDE_REF: ${exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)}
//                |TAG_GPS_DEST_LATITUDE_REF: ${exif.getAttribute(ExifInterface.TAG_GPS_DEST_LATITUDE_REF)}
//                |TAG_GPS_LONGITUDE: ${exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)}
//                |TAG_GPS_LONGITUDE_REF: ${exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)}
//                |TAG_GPS_DEST_LONGITUDE_REF: ${exif.getAttribute(ExifInterface.TAG_GPS_DEST_LONGITUDE_REF)}
//                |TAG_GPS_ALTITUDE: ${exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE)}
//            """.trimMargin())
//            location_latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
//            location_longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)

            // 2. Extract Location Info
            val latLong = FloatArray(2)
            if (exif.getLatLong(latLong)) {
//                location = Location(latLong[0], latLong[1])

                latitude = latLong[0].toDouble()
                longitude = latLong[1].toDouble()
//                println("Location found: ${latLong[0]}, ${latLong[1]}")
//            } else {
//                println("No GPS tags found in this photo's metadata.")
            }
        }
    } catch (e: Exception) {
        println("Error reading EXIF: ${e.message}")
    }
    if (dateTaken != null && latitude != null && longitude != null) {
        return PhotoMetadata(
            uri = uri,
            dateTaken = dateTaken,
            latitude = latitude,
            longitude = longitude
        )
    } else {
        return null
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDestination(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val db = DatabaseProvider.database

    val context = LocalContext.current

//    var selectedUri by remember { mutableStateOf<Uri?>(null) }
//    var displayName by remember { mutableStateOf("Unknown") }
//    var fileSize by remember { mutableStateOf("Unknown") }
//    var mimeType by remember { mutableStateOf("Unknown") }
//    var dimensions by remember { mutableStateOf("Unknown") }
//    var orientation by remember { mutableStateOf("Unknown") }
//    var cameraSpecs by remember { mutableStateOf("Unknown") }
//    var cameraModel by remember { mutableStateOf("Unknown") }
//    var dateTaken by remember { mutableStateOf("Unknown") }
//    var location by remember { mutableStateOf<Location?>(null) }

//    val photoPermissionLauncher = rememberPhotoPermissionLauncher(
//        onPermissionsGranted = { println("Success! Launching gallery...") },
//        onPermissionsDenied = { println("Permission denied.") }
//    )

//    val (storageGranted, storageLauncher) = rememberStoragePermission()
//    val (locationGranted, locationLauncher) = rememberLocationPermission()
//
//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            // Permission Accepted: Do something
//            Log.d("ExampleScreen","PERMISSION GRANTED")
//
//        } else {
//            // Permission Denied: Do something
//            Log.d("ExampleScreen","PERMISSION DENIED")
//        }
//    }
//
//    val photoPickerLauncher = rememberPhotoPickerLauncher(
//        onPhotoPicked = { metadata ->
//            if (metadata != null) {
//                println("latitude: ${metadata.latitude}, longitude: ${metadata.longitude}, date: ${metadata.dateTaken}")
//                db.photoQueries.insert(
//                    uri = metadata.uri.toString(),
//                    dateTaken = metadata.dateTaken,
//                    latitude = metadata.latitude,
//                    longitude = metadata.longitude
//                )
//            }
//        }
//    )

//    val storedString = queries.selectAll().executeAsOne().uri
//    val photoUri = Uri.parse(storedString) // Turns text back into a usable address

//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia()
//    ) { uri: Uri? ->
//        // THIS is the callback.
//        // It runs AFTER the user picks an image.
//        if (uri != null) {
//            selectedUri = uri
//            val photoUri = MediaStore.setRequireOriginal(uri)
//
//            // 2. Extract the data
//            context.contentResolver.openInputStream(photoUri)?.use { inputStream ->
//                val exif = ExifInterface(inputStream)
//
//                // Get the attributes using the AndroidX ExifInterface
//                cameraModel = exif.getAttribute(ExifInterface.TAG_MODEL) ?: "Unknown Model"
//                dateTaken = exif.getAttribute(ExifInterface.TAG_DATETIME) ?: "Unknown Date"
//
//                val latLong = FloatArray(2)
//                if (exif.getLatLong(latLong)) {
//                    val latitude = latLong[0]
//                    val longitude = latLong[1]
//                    println("Location: $latitude, $longitude")
//                }
//            }
//        }
//    }

    val permission = Manifest.permission.READ_MEDIA_IMAGES
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) Log.d("Permissions", "Granted!")
        else Log.d("Permissions", "Denied!")
    }


    // Scaffold provides the structure for the TopBar and handles system insets
    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Settings") },
//            )
//        }
    ) { innerPadding ->
        // Use the innerPadding provided by Scaffold to avoid
        // overlapping with the TopBar or bottom navigation
//        LazyColumn(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(horizontal = 16.dp), // General side padding
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            item {
//                Text(
//                    text = "App Settings",
//                    style = MaterialTheme.typography.headlineSmall,
//                    modifier = Modifier.padding(vertical = 16.dp)
//                )
//            }
//
//            // Example of setting items
//            item {
//                SettingsListItem(title = "Account", icon = Icons.Default.Person)
//            }
//            item {
//                SettingsListItem(title = "Notifications", icon = Icons.Default.Notifications)
//            }
//        }
//    }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
//                .padding(horizontal = 16.dp), // General side padding
        ) {
//            if (selectedUri != null) {
//                Text("Display Name: $displayName")
//                Text("Phone Used: $cameraModel")
//                Text("File Size: $fileSize")
//                Text("Dimensions: $dimensions")
//                Text("Orientation: $orientation")
//                Text("Camera Specs: $cameraSpecs")
//                Text("MIME Type: $mimeType")
//                Text("Date Captured: $dateTaken")
//                Text("Location: ${location?.latitude}, ${location?.longitude}")
//            }

            Row {
                Button(
                    onClick = {
                        scope.launch {
                            scanAllPhotos(context)
                        }
                    },
                    contentPadding = PaddingValues(16.dp * 2),
                ) {
                    Text("Scan all photos")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        val status = ContextCompat.checkSelfPermission(context, permission)

                        if (status != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(permission)
                        } else {
                            Log.d("Permissions", "We already have it. Doing work...")
                        }

//                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                launcher.launch("image/*")
                    },
                    contentPadding = PaddingValues(16.dp * 2),
                ) {
                    Text("Permission")
                }
            }

//            Button(
////                onClick = { /* */ },
//                onClick = {
//                    launcher.launch(
//                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                    )
//                },
//                contentPadding = PaddingValues(16.dp * 3),
//                modifier = Modifier
////                    .fillMaxWidth(0.7f) // 80% of the screen width
////                    .width(12.dp * 25)
////                    .height(12.dp * 7),
//            ) {
//                Text(
//                    "Inspect an image",
//                    style = MaterialTheme.typography.labelLarge
//                )
//            }
        }
    }
}

//@Composable
//fun Photos(title: String, icon: ImageVector) {
//    Surface(
//        onClick = { /* Handle click */ },
//        shape = RoundedCornerShape(12.dp),
//        color = MaterialTheme.colorScheme.surfaceContainerLow
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(icon, contentDescription = null)
//            Spacer(Modifier.width(16.dp))
//            Text(text = title, style = MaterialTheme.typography.bodyLarge)
//        }
//    }
//}