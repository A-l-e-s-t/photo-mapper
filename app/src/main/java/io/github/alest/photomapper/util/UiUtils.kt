package io.github.alest.photomapper.util

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.alest.photomapper.db.DatabaseProvider
import kotlinx.coroutines.*
import java.io.File


// --- 1. CONTROLLERS (Logic) ---
interface VisibilityState {
    val isVisible: Boolean
    fun show()
    fun hide()
    fun toggle()
}

class VisibilityController : VisibilityState {
    private val _isVisible = mutableStateOf(false)
    override val isVisible get() = _isVisible.value
    override fun show() {
        _isVisible.value = true
    }

    override fun hide() {
        _isVisible.value = false
    }

    override fun toggle() {
        _isVisible.value = !_isVisible.value
    }
}

class PersistentVisibilityController(
    private val state: MutableState<Boolean>
) : VisibilityState {
    override val isVisible get() = state.value
    override fun show() {
        state.value = true
    }

    override fun hide() {
        state.value = false
    }

    override fun toggle() {
        state.value = !state.value
    }
}

// --- 2. GLOBAL SCOPES & STATE ---
val notAvailablePopupController: VisibilityController by lazy { VisibilityController() }

object AppScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}


// --- 3. UI COMPONENTS ---
@Composable
fun NotAvailablePopup(
    controller: VisibilityController
) {
    if (controller.isVisible) {
        AlertDialog(
            onDismissRequest = { controller.hide() },
            text = {
                Text(
                    text = "Functionality not available",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            confirmButton = {
                TextButton(onClick = { controller.hide() }) {
                    Text(text = "Close ")
                }
            },
        )
    }
}
