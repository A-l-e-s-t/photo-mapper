//package com.example.photomapper.util
//
//import android.app.Activity
//import android.content.res.Configuration
//import androidx.compose.foundation.layout.ExperimentalLayoutApi
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.isImeVisible
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Snackbar
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Popup
//import androidx.compose.ui.window.PopupProperties
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.shadow.db.DatabaseProvider
//import com.example.shadow.disableImmersiveFullscreen
//import com.example.shadow.enableImmersiveFullscreen
//import kotlinx.coroutines.*
//import java.io.File
//
//import com.example.shadow.llamaVm.LlamaVm
//
//
//class LlamaVmFactory(private val filesDir: File) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        @Suppress("UNCHECKED_CAST")
//        return LlamaVm(filesDir) as T
//    }
//}
//
//
//data class ChatMessage(
//    val fromUser: Boolean,
//    val text: String
//)
//
//
//class ExpandController {
//    private val _expanded = mutableStateOf(false)
//    val expanded: Boolean get() = _expanded.value
//
//    fun show() { _expanded.value = true }
//    fun hide() { _expanded.value = false }
//    fun toggle() { _expanded.value = !_expanded.value }
//}
//
//
//class PersistentExpandController(
//    private val _expanded: MutableState<Boolean>
//) {
//    val expanded: Boolean get() = _expanded.value
//
//    fun show() { _expanded.value = true }
//    fun hide() { _expanded.value = false }
//    fun toggle() { _expanded.value = !_expanded.value }
//}
//
//
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun keyboardVisible(): Boolean {
//    return WindowInsets.isImeVisible
//}
//
//
//@Composable
//fun NotAvailablePopup(
//    controller: ExpandController
//) {
//    if (controller.expanded) {
//        AlertDialog(
//            onDismissRequest = { controller.hide() },
//            text = {
//                Text(
//                    text = "Functionality not available",
//                    style = MaterialTheme.typography.bodyMedium,
//                )
//            },
//            confirmButton = {
//                TextButton(onClick = { controller.hide() }) {
//                    Text(text = "Close ")
//                }
//            },
//        )
//    }
//}
//
//val notAvailablePopupController: ExpandController by lazy { ExpandController() }
//val deleteModelSnackbarHostState: SnackbarHostState by lazy { SnackbarHostState() }
//
////val snackbarHostState = remember { SnackbarHostState() }
//
//
//object AppScope {
//    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
//}
//
//@Composable
//fun FullscreenController(): Boolean {
//    val configuration = LocalConfiguration.current
//    val context = LocalContext.current
//    val activity = remember(context) { context as Activity }
//
//    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
//    println("isLandscape: $isLandscape")
//
//    LaunchedEffect(isLandscape) {
//        if (isLandscape) {
//            activity.enableImmersiveFullscreen()
//            println("activity.enableImmersiveFullscreen()")
//        } else {
//            activity.disableImmersiveFullscreen()
//            println("activity.disableImmersiveFullscreen()")
//        }
//    }
//
//    return isLandscape
//}
//
//
//@Composable
//fun SnackbarPopup(
//    hostState: SnackbarHostState,
//    modifier: Modifier = Modifier
//) {
//
//    // Only show Popup when there is at least one snackbar
//    val currentSnackbar = hostState.currentSnackbarData
//    val db = DatabaseProvider.database
//
//    Popup(
//        alignment = Alignment.BottomEnd,
//        // prevents outside clicks from dismissing it
//        properties = PopupProperties(focusable = false)
//    ) {
//        SnackbarHost(
//            hostState = hostState,
//            modifier = modifier
//                .padding(bottom = 24.dp)
//                .fillMaxWidth(),
//        ) { data ->
//            Snackbar(
//                action = {
//                    data.visuals.actionLabel?.let { label ->
//                        TextButton(onClick = { data.performAction() }) {
//                            Text(label)
//                        }
//                    }
//                },
//                dismissAction = {
//                    IconButton(onClick = {
//                        data.dismiss()
//                    }) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                            contentDescription = "Dismiss"
//                        )
//                    }
//                },
//                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//                contentColor = MaterialTheme.colorScheme.onSurface,
//                actionContentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//                shape = RoundedCornerShape(24.dp),
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//            ) {
//                Text(data.visuals.message)
//            }
//        }
//    }
//}