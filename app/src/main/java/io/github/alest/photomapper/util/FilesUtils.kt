//package com.example.photomapper.util
//
//import android.content.Context
//import android.net.Uri
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.compose.ManagedActivityResultLauncher
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
////import com.example.shadow.db.DatabaseProvider
//import com.example.shadow.llm.getFileNameFromUri
////import com.example.shadow.llm.isGgufFile
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.io.FileOutputStream
//import java.io.InputStream
//import java.io.OutputStream
//
//
//data class CopyStatus(
//    val percent: Int,
//    val success: Boolean? = null // null = still copying, true/false = done
//)
//
//
//
//fun InputStream.copyToWithProgress(
//    out: OutputStream,
//    totalBytes: Long,
//    onProgress: (percent: Int) -> Unit
//): Long {
//    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
//    var bytesCopied = 0L
//    var bytesRead: Int
//
//    while (true) {
//        bytesRead = this.read(buffer)
//        if (bytesRead == -1) break
//        out.write(buffer, 0, bytesRead)
//        bytesCopied += bytesRead
//        val percent = if (totalBytes > 0) ((bytesCopied * 100) / totalBytes).toInt() else 0
//        onProgress(percent)
//    }
//
//    out.flush()
//    return bytesCopied
//}
//
//suspend fun copyModelWithProgress(
//    context: Context,
//    uri: Uri,
//    fileName: String,
//    onProgress: (modelName: String, percent: CopyStatus) -> Unit
//): Boolean = withContext(Dispatchers.IO) {
//    try {
//        val input = context.contentResolver.openInputStream(uri) ?: return@withContext false
////        val fileName = getFileNameFromUri(context, uri) ?: return@withContext false
//        val modelsDir = File(context.filesDir, "models").apply { mkdirs() }
//        val destFile = File(modelsDir, "$fileName.gguf")
//
//        val totalBytes: Long = context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1L
//
//        input.use { i ->
//            FileOutputStream(destFile).use { o ->
//                i.copyToWithProgress(o, totalBytes) { percent ->
//                    onProgress(fileName, CopyStatus(percent = percent))
//                }
//            }
//        }
//
//        onProgress(fileName, CopyStatus(percent = 100, true))  // Report success
//        true
//    } catch (e: Exception) {
//        onProgress(fileName, CopyStatus(percent = -1, false))  // Report failer
//        Log.e("FileUtils", "Error copying model", e)
//        false
//    }
//}
//
//
//fun deleteUnknownModels(context: Context) {
//    val db = DatabaseProvider.database
//
//    // 2️⃣ Get all models from DB (after deletion)
//    val dbModelNames = db.modelQueries
//        .selectAll()
//        .executeAsList()
//        .map { it.name }
//
//    // 3️⃣ Get all model files from /files/models/
//    val modelsDir = File(context.filesDir, "models")
//    if (!modelsDir.exists()) return
//
//    val modelFiles = modelsDir.listFiles { file ->
//        file.extension == "gguf"
//    } ?: return
//
//    // 4️⃣ Find files not listed in DB
//    val unknownFiles = modelFiles.filter { file ->
//        val fileNameWithoutExt = file.nameWithoutExtension
//        fileNameWithoutExt !in dbModelNames
//    }
//
//    // 5️⃣ Delete them safely
//    for (file in unknownFiles) {
//        try {
//            file.delete()
//            Log.d("ModelCleanup", "Deleted orphaned model file: ${file.name}")
//        } catch (e: Exception) {
//            Log.e("ModelCleanup", "Failed to delete ${file.name}", e)
//        }
//    }
//}
//
//
////@Composable
////fun modelPickerLauncher(
////    onProgress: (modelName: String, percent: CopyStatus) -> Unit
////): ManagedActivityResultLauncher<String, Uri?> {
////    val context = LocalContext.current
////
////    return rememberLauncherForActivityResult(
////        contract = ActivityResultContracts.GetContent()
////    ) { uri: Uri? ->
////        if (uri != null) {
////            CoroutineScope(Dispatchers.IO).launch {
////                if (!isGgufFile(context, uri)) {        // move this inside IO
////                    withContext(Dispatchers.Main) {
////                        Toast.makeText(context, "Not a .gguf file", Toast.LENGTH_SHORT).show()
////                    }
////                    return@launch
////                }
////
////                val fileName = getFileNameFromUri(context, uri)
////                if (fileName == null) {
////                    withContext(Dispatchers.Main) {
////                        Toast.makeText(context, "Invalid file name", Toast.LENGTH_LONG).show()
////                    }
////                    return@launch
////                }
////
////                val db = DatabaseProvider.database
////                val models = db.modelQueries.selectAll().executeAsList()
////
////                // TODO: Detect if the same model being copied
////                if (models.any { it.name == fileName }) {
////                    withContext(Dispatchers.Main) {
////                        Toast.makeText(context, "This model is already added", Toast.LENGTH_LONG).show()
////                    }
////                    return@launch
////                }
////
////                val copiedFileName = copyModelWithProgress(
////                    context,
////                    uri,
////                    fileName,
////                    onProgress = onProgress
////                )
////
//////                processes[fileName] = CopyStatus(true, progressNumber)
////
////
//////                val copiedFileName = copyModelToAppDir(context, uri)
//////                println("fileName: $fileName")
////                withContext(Dispatchers.Main) {
////                    if (copiedFileName != null) {
////                        db.modelQueries.insert(
////                            name = fileName,
////                            lastUsed = System.currentTimeMillis()
////                        )
////                        Toast.makeText(context, "Model copied", Toast.LENGTH_LONG).show()
////                    } else {
////                        Toast.makeText(context, "Copy failed", Toast.LENGTH_LONG).show()
////                    }
////                }
////            }
////        }
////    }
////}