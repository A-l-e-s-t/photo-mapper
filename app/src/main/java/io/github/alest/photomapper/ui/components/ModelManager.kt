//package com.example.photomapper.ui.components
//
////import com.example.shadow.lll_chat
//
//import androidx.activity.compose.BackHandler
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.*
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import app.cash.sqldelight.coroutines.asFlow
//import app.cash.sqldelight.coroutines.mapToList
//import androidx.compose.runtime.key
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.window.Dialog
//import androidx.compose.ui.window.DialogProperties
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//import com.example.shadow.db.DatabaseProvider
//import com.example.shadow.db.Model
//import com.example.shadow.db.LlmEndpoint
////import com.example.shadow.llm.MainViewModel
//import com.example.shadow.llama.*
//import com.example.shadow.llm.*
//import com.example.shadow.util.*
//
//
//@Composable
//fun AddModelDropdown(
//    controller: ExpandController,
//    onCopyFromStorage: () -> Unit,
//    onUseExternalModel: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//    ) {
//        DropdownMenu(
//            expanded = controller.expanded,
//            onDismissRequest = { controller.hide() },
//            shape = MaterialTheme.shapes.large
//        ) {
//            DropdownMenuItem(
//                text = { Text("Copy model from storage") },
//                onClick = {
//                    onCopyFromStorage()
//                    controller.hide()
//                }
//            )
//            DropdownMenuItem(
//                text = { Text("Connect external model") },
//                onClick = {
//                    onUseExternalModel()
//                    controller.hide()
//                }
//            )
//        } // DropdownMenu
//    } // Box
//}
//
//
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun AddLlmEndpointDialog(
//    controller: PersistentExpandController,
//    url: String,
//    onUrlChange: (String) -> Unit,
//    api: String,
//    onApiChange: (String) -> Unit,
//    name: String,
//    onNameChange: (String) -> Unit,
//    onAddButton: () -> Unit,
//    isLandscape: Boolean
//) {
//    if (controller.expanded) {
//        Dialog(
//            onDismissRequest = { controller.hide() },
//            properties = DialogProperties(usePlatformDefaultWidth = false)
//        ) {
//            Surface(
//                shape = RoundedCornerShape(24.dp),
////                                tonalElevation = 6.dp,
//                modifier = Modifier
////                                    .fillMaxWidth(0.9f)   // 90% of screen width
////                                    .wrapContentHeight()
//            ) {
//                if (isLandscape) {
//                    // 🎯 LANDSCAPE LAYOUT
//                    Column (
//                        modifier = Modifier.padding(24.dp),
////                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        Row {
//                            Text(
//                                "Add external LLM server",
//                            )
//                            Spacer(Modifier.weight(1f))
//
//                            Button(onClick = {
//                                onAddButton()
//                                controller.hide()
//                            }) {
//                                Text("Add")
//                            }
//                        }
//                        Row(
//                            Modifier
//                        ) {
//                            OutlinedTextField(
//                                value = url,
//                                onValueChange = onUrlChange,
//                                label = { Text("URL") },
//                                shape = RoundedCornerShape(28.dp)
//                            )
//                            Spacer(Modifier.width(12.dp))
//
//                            OutlinedTextField(
//                                value = api,
//                                onValueChange = onApiChange,
//                                label = { Text("API key (optional)") },
////                                visualTransformation = PasswordVisualTransformation(),
////                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                                shape = RoundedCornerShape(28.dp)
//                            )
//                            Spacer(Modifier.width(12.dp))
//
//                            OutlinedTextField(
//                                value = name,
//                                onValueChange = onNameChange,
//                                label = { Text("Name (optional)") },
//                                shape = RoundedCornerShape(28.dp)
//                            )
//                        }
//                    } // Row
//
//                } else {
//                    // 🎯 PORTRAIT LAYOUT
//                    Column(Modifier.padding(24.dp)) {
//                        Text("Add external LLM server")
//                        Spacer(Modifier.height(16.dp))
//
//                        OutlinedTextField(
//                            value = url,
//                            onValueChange = onUrlChange,
//                            label = { Text("URL") },
//                            shape = RoundedCornerShape(28.dp)
//                        )
//                        Spacer(Modifier.height(12.dp))
//
//                        OutlinedTextField(
//                            value = api,
//                            onValueChange = onApiChange,
//                            label = { Text("API key (optional)") },
////                            visualTransformation = PasswordVisualTransformation(),
////                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                            shape = RoundedCornerShape(28.dp)
//                        )
//                        Spacer(Modifier.height(12.dp))
//
//                        OutlinedTextField(
//                            value = name,
//                            onValueChange = onNameChange,
//                            label = { Text("Name (optional)") },
//                            shape = RoundedCornerShape(28.dp)
//                        )
//                        Spacer(Modifier.height(12.dp))
//
//                        Button(onClick = {
//                            onAddButton()
//                            controller.hide()
//                        }) {
//                            Text("Add")
//                        }
//                    }
//                } // if
//            } // Surface
//        } // Dialog
//    } // if
//}
//
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun ModelManagerMenu(menuController: PersistentExpandController) {
//    val configuration = LocalConfiguration.current
//    val screenHeightDp = configuration.screenHeightDp.dp
//    val screenWidthDp = configuration.screenWidthDp.dp
//    val ime = LocalSoftwareKeyboardController.current
//    val db = DatabaseProvider.database
//    val isLandscape = FullscreenController()
//
//    val llmEndpoints by db.llmEndpointQueries
//        .selectAll()
//        .asFlow()
//        .mapToList(Dispatchers.IO)
//        .collectAsState(initial = emptyList())
//
//    // DB-driven list (selectAll)
//    val models by db.modelQueries
//        .selectAll()
//        .asFlow()
//        .mapToList(Dispatchers.IO)
//        .collectAsState(initial = emptyList())
//
//    // derived count used in UI (you asked for this)
//    val hiddenModelsCount = db.modelQueries
//        .countHiddenModels()
//        .executeAsOne()
//        .toInt()
//
//    val copiedModels = remember { mutableStateMapOf<String, CopyStatus>() }
//
//
//    // AddLlmEndpointDialog
//    val addLlmEndpointDialogState = rememberSaveable { mutableStateOf(false) }
//    val addLlmEndpointDialogController = remember(addLlmEndpointDialogState) {
//        PersistentExpandController(addLlmEndpointDialogState)
//    }
//    var addLlmEndpointDialogUrl by rememberSaveable { mutableStateOf("") }
//    var addLlmEndpointDialogName by rememberSaveable { mutableStateOf("") }
//    var addLlmEndpointDialogAPI by rememberSaveable { mutableStateOf("") }
//
//    val addModelDropdownController = remember { ExpandController() }
//    val buttonAddModelDropdownController = remember { ExpandController() }
//
//    val desiredItemVerticalSpace = 500.dp
//    val conditionTopSpace = 200.dp
//    val remainingTopSpace = screenHeightDp - desiredItemVerticalSpace
//    var topPadding = remainingTopSpace - conditionTopSpace
//    topPadding = if (topPadding < 0.dp) 0.dp else topPadding
//
//    val desiredItemHorizontalSpace = 90.dp
//    val conditionStartSpace = 400.dp
//    val remainingStartSpace = screenWidthDp - desiredItemHorizontalSpace
//    var startPadding = remainingStartSpace - conditionStartSpace
//    startPadding = if (startPadding < 0.dp) 0.dp else startPadding
//    val endPadding = if (startPadding != 0.dp) 30.dp else 0.dp
//
//    BackHandler(enabled = menuController.expanded) {
//        menuController.hide()
//    }
//
//    LaunchedEffect(menuController.expanded) {
//        if (menuController.expanded) {
//            ime?.hide()
//        }
//    }
//
//    AnimatedVisibility(
//        visible = menuController.expanded,
//        enter = fadeIn(),
//        exit = fadeOut()
//    ) {
//        Box(Modifier.fillMaxSize()) {
//            // --- Scrim (fades in/out) ---
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.6f))
//                    .clickable(
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() }
//                    ) { menuController.hide() }
////                    .animateEnterExit(enter = fadeIn(), exit = fadeOut())
//            )
//
//            Surface(
//                color = MaterialTheme.colorScheme.surface,
//                tonalElevation = 6.dp,
//                shape = RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
////                    .fillMaxHeight(0.7f)
//                    .statusBarsPadding()
//                    .displayCutoutPadding()
////                    .navigationBarsPadding()
//                    .padding(startPadding, topPadding, endPadding, 0.dp)
////                    .offset(y = offsetY)
////                    .animateEnterExit(
////                        enter = slideInVertically { it },
////                        exit  = slideOutVertically { it }
////                    )
//                    .animateEnterExit(
//                        enter = slideInVertically { it },
//                        exit = slideOutVertically { it }
//                    )
//            ) {
//                // Host state remembered once per sheet lifecycle
////        val snackbarHostState = remember { SnackbarHostState() }
//
////                val modelPicker = modelPickerLauncher(onProgress = { modelName, percent ->
////                    copiedModels[modelName] = percent
////                    println(copiedModels)
////                })
//
//                // Sheet content
////            Column(
////                verticalArrangement = Arrangement.spacedBy(16.dp),
////                modifier = Modifier
////                    .padding(start = 16.dp, end = 16.dp)
////            ) {
////                Row(
////                    Modifier.fillMaxWidth(),
////                    verticalAlignment = Alignment.CenterVertically,
////                    horizontalArrangement = Arrangement.SpaceBetween
////                ) {
//                Box {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                    ) {
//                        stickyHeader {
//                            Surface(
//                                tonalElevation = 4.dp,
//                                color = MaterialTheme.colorScheme.surface
//                            ) {
//                                Box(
////                                    verticalAlignment = Alignment.CenterVertically,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(8.dp)
//                                        .align(Alignment.Center)
//                                ) {
//                                    IconButton(onClick = { menuController.hide() }) {
//                                        Icon(Icons.Default.Close, contentDescription = "Close")
//                                    }
//
//                                    Text(
//                                        text = "Model Manager",
//                                        style = MaterialTheme.typography.titleLarge,
//                                        textAlign = TextAlign.Center,
//                                        modifier = Modifier
//                                            .align(Alignment.Center)
////                                            .weight(1f)
//                                    )
//
//                                    IconButton(
//                                        onClick = { addLlmEndpointDialogController.show() },
//                                        modifier = Modifier
//                                            .align(Alignment.CenterEnd)
//                                    ) {
//                                        Icon(Icons.Default.Add, contentDescription = "Add")
//                                    }
//
////                                    Row(
////                                        modifier = Modifier
////                                            .align(Alignment.CenterEnd)
//////                                        contentAlignment = Alignment.Center
////                                    ) {
////                                        Button(onClick = {
////                                            addLlmEndpointDialogController.show()
////                                        }) {
////                                            Text("Add")
////                                        }
////
////                                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
////                                    }
//                                }
//                            } // Surface
//                        } // stickyHeader
//
////                        item {
////                            Row(
////                                Modifier.fillMaxWidth(),
////                                verticalAlignment = Alignment.CenterVertically,
////                                horizontalArrangement = Arrangement.SpaceBetween
////                            ) {
////                                Text("Model manager", style = MaterialTheme.typography.titleLarge)
////
//////                        Box(  // Plus button
//////                            modifier = Modifier
//////                            //                            .align(Alignment.CenterEnd)
//////                        ) {
//////                            IconButton(
//////                                onClick = { addModelDropdownController.toggle() },
//////                                modifier = Modifier
//////                                    .align(Alignment.CenterEnd)
//////                            ) {
//////                                Icon(Icons.Default.Add, contentDescription = "Add a model")
//////                            }
//////
//////                            AddModelDropdown(
//////                                controller = addModelDropdownController,
//////                                onCopyFromStorage = {
//////                                    db.modelQueries.deleteHiddenModels()
//////                                    modelPicker.launch("*/*")
//////                                },
//////                                onUseExternalModel = { notAvailablePopupController.show() },
//////                            )
//////                        }
////
////                                Box(
////                                    contentAlignment = Alignment.Center
////                                ) {
////                                    Button(onClick = {
////                                        addLlmEndpointDialogController.show()
//////                                notAvailablePopupController.show()
////                                    }) {
////                                        Text("Add")
////                                    }
////                                }
////
////
//////                        IconButton(onClick = {
//////                            // example insert
//////                            scope.launch {
//////    //                                withContext(Dispatchers.IO) {
//////                                db.modelQueries.insert("Item ${System.currentTimeMillis()}", System.currentTimeMillis())
//////    //                                }
//////                                deleteModelSnackbarHostState.showSnackbar("Inserted new item", duration = SnackbarDuration.Short)
//////                            }
//////                        }) {
//////                            Icon(Icons.Default.Adb, contentDescription = "Add")
//////                        }
////                            }
////                        } // item
//
//                        item {
//                            Spacer(modifier = Modifier.height(16.dp))
//                        }
//
////                        itemsIndexed(
////                            items = copiedModels.entries.toList(),
////                            key = { _, entry -> entry.key } // stable key = model name
////                        ) { index, entry ->
////                            val modelName = entry.key
////                            val copyStatus = entry.value
////
////                            ActivelyCopiedModels(
////                                modelName,
////                                copyStatus,
////                                modifier = Modifier
////                                    .padding(start = 16.dp, end = 16.dp )
////                            )
////                        }
//
//
//                        itemsIndexed(
//                            items = llmEndpoints,
//                            key = { _, endpoint -> endpoint.id }
//                        ) { _, endpoint ->
//                            Spacer(modifier = Modifier.padding(bottom = 16.dp))
//                            LlmEndpointFolder(
//                                endpoint,
//                                modifier = Modifier
//                                    .padding(start = 16.dp, end = 16.dp )
//                            )
//                        }
////
////                        itemsIndexed(
////                            items = models,
////                            key = { _, model -> model.id }
////                        ) { index, model ->
////                            ModelItem(
////                                model,
////                                index,
////                                models,
////                                modifier = Modifier
////                                    .padding(start = 16.dp, end = 16.dp )
////                            )
////                        }
//
//                        item {
//                            Spacer(modifier = Modifier.navigationBarsPadding())
//                        }
//                    } // LazyColumn
//
//                    key("snackbarHost") {
//                        SnackbarPopup(deleteModelSnackbarHostState)
//                    }
//                } // Box
//
//                AddLlmEndpointDialog(
//                    controller = addLlmEndpointDialogController,
//                    url = addLlmEndpointDialogUrl,
//                    onUrlChange = { newValue -> addLlmEndpointDialogUrl = newValue },
//                    api = addLlmEndpointDialogAPI,
//                    onApiChange = { newValue -> addLlmEndpointDialogAPI = newValue },
//                    name = addLlmEndpointDialogName,
//                    onNameChange = { newValue -> addLlmEndpointDialogName = newValue },
//                    onAddButton = {
////                        db.llmEndpointQueries.selectAll()
//                        db.llmEndpointQueries.insert(
//                            url = addLlmEndpointDialogUrl,
//                            api = addLlmEndpointDialogAPI,
//                            name = addLlmEndpointDialogName
//                        )
//                        addLlmEndpointDialogUrl = ""
//                        addLlmEndpointDialogAPI = ""
//                        addLlmEndpointDialogName = ""
//                    },
//                    isLandscape = isLandscape
//                )
//            } // Surface
//        } // Box
//    } // AnimatedVisibility
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ActivelyCopiedModels(
//    modelName: String,
//    copyStatus: CopyStatus,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    val db = DatabaseProvider.database
//
//    val scope = rememberCoroutineScope()
//
//    if (copyStatus.success != null) {
//        return
//    }
//
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
////                .padding(bottom = 24.dp)
//    ) {
//        Button(
//            onClick = { notAvailablePopupController.show() },
//            shape = RoundedCornerShape(32.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Column {
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "Copying $modelName",
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier
//                            .align(Alignment.CenterStart)
//                            .padding(end = 45.dp)
//                    )
//                    Text(
//                        "${copyStatus.percent}%",
//                        modifier = Modifier
//                            .align(Alignment.CenterEnd)
//                    )
//                }
//            } // Column
//        } // Button
//    } // Row
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LlmEndpointFolder(
//    endpoint: LlmEndpoint,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val db = DatabaseProvider.database
//
//    val cardController = remember { ExpandController() }
//
//    Surface(
//        shape = RoundedCornerShape(24.dp),
//        color = MaterialTheme.colorScheme.surfaceVariant,
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        val rotation by animateFloatAsState(
//            targetValue = if (cardController.expanded) 180f else 0f,
//
//            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
//            label = "arrowRotation"
//        )
//
//        Column(
//            Modifier
//                .padding(12.dp)
//        ) {
//            Row {
//                Text(
//                    endpoint.url,
//                    Modifier
//                        .weight(1f)
//                )
//                IconButton(
//                    onClick = { notAvailablePopupController.show() },
//                    modifier = Modifier
//                ) {
//                    Icon(Icons.Default.Edit, contentDescription = "Rename")
//                }
//                IconButton(
//                    onClick = { cardController.toggle() },
////                        modifier = Modifier.align(Alignment.CenterVertically)
//                ) {
//                    Icon(
//                        Icons.Default.ArrowDropDown,
//                        contentDescription = "Expand",
//                        modifier = Modifier.graphicsLayer { rotationZ = rotation }
//                    )
//                }
//            } // Row
//
//            AnimatedVisibility(
//                visible = cardController.expanded,
//                enter = expandVertically(),
//                exit = shrinkVertically()
//            ) {
//                Column (
//                    modifier = Modifier
//                        .fillMaxWidth(),
////                    contentAlignment = Alignment.Center
//                ) {
//                    var endpointModels by remember { mutableStateOf<EndpointModels?>(null) }
//
//                    LaunchedEffect(Unit) {
//                        endpointModels = getEndpointModels(endpoint.url)
//                    }
////                    Text(text = models.toString())
////                    itemsIndexed(
////                        items = models,
////                        key = { _, model -> model.id }
////                    ) { index ->
////                        LlmEndpointFolderItem(models, index)
////                    }
//                    val safeEndpointModels = endpointModels
//                    if (safeEndpointModels != null) {
//                        val modelsCount = safeEndpointModels.data.size
//                        repeat(modelsCount) { modelIndex ->
////                        println("index = $index")
////                        Text(text = endpointModels[index].id)
//                            LlmEndpointFolderItem(
//                                safeEndpointModels,
//                                endpoint,
//                                modelIndex,
//                                modifier = Modifier
////                                    .padding(start = 16.dp, end = 16.dp )
//                            )
//                        }
//                    } else {
//                        Text("Couldn't retrieve any models")
//                    }
//                }
//
//
////                LazyColumn(
////                    modifier = Modifier
////                        .fillMaxHeight()
////                ) {
////                    itemsIndexed(
////                        items = llmEndpoints,
////                        key = { _, endpoint -> endpoint.id }
////                    ) { index, endpoint ->
////                        LlmEndPointFolder(
////                            endpoint,
////                            modifier = Modifier
////                                .padding(start = 16.dp, end = 16.dp )
////                        )
////                    }
////                }
//            } // AnimatedVisibility
//        } // Column
//    } // Surface
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LlmEndpointFolderItem(
//    models: EndpointModels,
//    endpoint: LlmEndpoint,
//    index: Int,
//    modifier: Modifier = Modifier
//) {
//    val model = models.data[index]
//
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val db = DatabaseProvider.database
//
//    // compute first and last visible indexes in the full models list
//    val firstVisibleIndex = 0
//    val lastVisibleIndex = models.data.size - 1
//
//    val isFirst = index == firstVisibleIndex
//    val isLast = index == lastVisibleIndex
//
//    // compute shape and padding using index positions in the original models list
//    val cornerLarge = 16.dp
//    val cornerSmall = 6.dp
//    val spacedByPadding = if (isLast) 0.dp else 3.dp
//
////    if (isFirst) cardController.show()
//
//    val shape = when {
//        isLast && isFirst -> RoundedCornerShape(
//            topStart = cornerLarge,
//            topEnd = cornerLarge,
//            bottomStart = cornerLarge,
//            bottomEnd = cornerLarge
//        )
//        isFirst -> RoundedCornerShape(
//            topStart = cornerLarge,
//            topEnd = cornerLarge,
//            bottomStart = cornerSmall,
//            bottomEnd = cornerSmall
//        )
//        isLast -> RoundedCornerShape(
//            topStart = cornerSmall,
//            topEnd = cornerSmall,
//            bottomStart = cornerLarge,
//            bottomEnd = cornerLarge
//        )
//        else -> RoundedCornerShape(cornerSmall)
//    }
//
//
//    Card(
//        onClick = {
////            startServer(context, model)
//            selectedEndpoint = SelectedLlmEndpoint(model.id, endpoint.url)
//        },
//        shape = shape,
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(bottom = spacedByPadding)
////            .padding(12.dp),
//    ) {
////    Button(
////        onClick = {
//////            val resilt = modelManager.loadModel()
//////            println("resilt: $resilt")
////        },
//////            shape = RoundedCornerShape(32.dp),
////        shape = shape,
////        colors = ButtonDefaults.buttonColors(
////            containerColor = MaterialTheme.colorScheme.surfaceVariant,
////            contentColor = MaterialTheme.colorScheme.onSurface
////        ),
////        modifier = Modifier
////            .fillMaxWidth()
////            .padding(bottom = spacedByPadding)
////    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//        ) {
////            Column(
////                modifier = Modifier.fillMaxWidth(),
//////                verticalAlignment = Alignment.CenterVertically,
//////                horizontalArrangement = Arrangement.SpaceBetween
////            ) {
//                Text(
//                    model.id,
//                    modifier = Modifier
////                        .align(Alignment.CenterVertically)
////                        .weight(1f)
//                )
////            }
//        } // Row
//    } // Card
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ModelItem(
//    model: Model,
//    index: Int,
//    models: List<Model>,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val db = DatabaseProvider.database
//
//    if (!model.show) {
//        return
//    }
//
//    // compute shape and padding using index positions in the original models list
//    val cornerLarge = 24.dp
//    val cornerSmall = 8.dp
//
//    // compute first and last visible indexes in the full models list
//    val firstVisibleIndex = models.indexOfFirst { it.show }
//    val lastVisibleIndex = models.indexOfLast { it.show }
//
//    val cardController = remember { ExpandController() }
//    val loadingStateController = remember { ExpandController() }
//
//
//    val isFirst = index == firstVisibleIndex
//    val isLast = index == lastVisibleIndex
//
////    if (isFirst) cardController.show()
//
//    val shape = when {
//        isLast && isFirst -> RoundedCornerShape(
//            topStart = cornerLarge,
//            topEnd = cornerLarge,
//            bottomStart = cornerLarge,
//            bottomEnd = cornerLarge
//        )
//        isFirst -> RoundedCornerShape(
//            topStart = cornerLarge,
//            topEnd = cornerLarge,
//            bottomStart = cornerSmall,
//            bottomEnd = cornerSmall
//        )
//        isLast -> RoundedCornerShape(
//            topStart = cornerSmall,
//            topEnd = cornerSmall,
//            bottomStart = cornerLarge,
//            bottomEnd = cornerLarge
//        )
//        else -> RoundedCornerShape(cornerSmall)
//    }
//
//    val spacedByPadding = if (isLast) 0.dp else 4.dp
//
//    Card(
//        onClick = { startServer(context, model) },
//        shape = shape,
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(bottom = spacedByPadding)
//    ) {
////    Button(
////        onClick = {
//////            val resilt = modelManager.loadModel()
//////            println("resilt: $resilt")
////        },
//////            shape = RoundedCornerShape(32.dp),
////        shape = shape,
////        colors = ButtonDefaults.buttonColors(
////            containerColor = MaterialTheme.colorScheme.surfaceVariant,
////            contentColor = MaterialTheme.colorScheme.onSurface
////        ),
////        modifier = Modifier
////            .fillMaxWidth()
////            .padding(bottom = spacedByPadding)
////    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//        ) {
////            Text(model.name, Modifier.weight(1f))
////
////            IconButton(onClick = {
////                scope.launch {
////                    // mark hidden on IO
////                    //withContext(Dispatchers.IO) {
////                    db.modelQueries.updateShowStatus(false, model.id)
////                    //}
////
////                    // give Compose one frame to settle (optional safeguard)
////                    // withFrameNanos { }
////
////                    // show snackbar with undo
////                    val result = deleteModelSnackbarHostState.showSnackbar(
////                        message = "Model deleted",
////                        actionLabel = "Undo",
////                        withDismissAction = true,
////                        duration = SnackbarDuration.Short
////                    )
////
////                    when (result) {
////                        SnackbarResult.ActionPerformed -> withContext(Dispatchers.IO) {
////                            db.modelQueries.updateShowStatus(true, model.id)
////                        }
////
////                        SnackbarResult.Dismissed -> withContext(Dispatchers.IO) {
////                            db.modelQueries.deleteHiddenModels()
////                            // keep any additional cleanup on IO
////                        }
////                    }
////
////                    // optional: call platform cleanup on main thread
////                    if (result == SnackbarResult.Dismissed) {
////                        deleteUnknownModels(context)
////                    }
////                }
////            }) {
////                Icon(Icons.Default.Delete, contentDescription = "Delete")
////            }
//
//            Column {
//                val rotation by animateFloatAsState(
//                    targetValue = if (cardController.expanded) 180f else 0f,
//
//                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
//                    label = "arrowRotation"
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        model.name,
//                        modifier = Modifier
////                            .align(Alignment.CenterVertically)
//                            .weight(1f)
//                    )
//
////                    Button(
////                        onClick = { notAvailablePopupController.show() },
////                    ) {
////                        Text("Load")
////                    }
//
//                    IconButton(
//                        onClick = { cardController.toggle() },
////                        modifier = Modifier.align(Alignment.CenterVertically)
//                    ) {
//                        Icon(
//                            Icons.Default.ArrowDropDown,
//                            contentDescription = "Expand",
//                            modifier = Modifier.graphicsLayer { rotationZ = rotation }
//                        )
//                    }
//                }
//
//                AnimatedVisibility(
//                    visible = cardController.expanded,
//                    enter = expandVertically(),
//                    exit = shrinkVertically()
//                ) {
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        IconButton(
//                            onClick = { notAvailablePopupController.show() },
//                            modifier = Modifier
//                                .align(Alignment.CenterStart)
//                        ) {
//                            Icon(Icons.Default.Edit, contentDescription = "Rename")
//                        }
//                        IconButton(
//                            onClick = {
//                                db.modelQueries.updateShowStatus(false, model.id)
//                                scope.launch {
//                                    val result = deleteModelSnackbarHostState.showSnackbar(
//                                        message = "Model deleted",
//                                        actionLabel = "Undo",
//                                        withDismissAction = true,
//                                        duration = SnackbarDuration.Short
//                                    )
//
//                                    when (result) {
//                                        SnackbarResult.ActionPerformed ->
//                                            db.modelQueries.updateShowStatus(true, model.id)
//
//                                        SnackbarResult.Dismissed -> {
//                                            db.modelQueries.deleteHiddenModels()
//                                            deleteUnknownModels(context)
//                                        }
//                                    }
//                                }
//                            },
//                            modifier = Modifier
//                                .align(Alignment.CenterEnd)
//                        ) {
//                            Icon(Icons.Default.Delete, contentDescription = "Delete")
//                        }
//                    }
//                }
//            } // Column
//        } // Row
//    } // Card
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LegacyModelManagerSheet() {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val db = DatabaseProvider.database
//
//    // DB-driven list (selectAll)
//    val models by db.modelQueries
//        .selectAll()
//        .asFlow()
//        .mapToList(Dispatchers.IO)
//        .collectAsState(initial = emptyList())
//
//    // derived count used in UI (you asked for this)
//    val hiddenModelsCount = db.modelQueries
//        .countHiddenModels()
//        .executeAsOne()
//        .toInt()
//
//    val copiedModels = remember { mutableStateMapOf<String, CopyStatus>() }
//
////    val activelyCopiedModels = copiedModels
////        .filterValues { it.success == null }
////        .entries.toList()
//
//    // Bottom sheet control
//    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    val sheetController = remember { ExpandController() }
//    var showDialog = remember { ExpandController() }
//    val addModelDropdownController = remember { ExpandController() }
//    val buttonAddModelDropdownController = remember { ExpandController() }
//
////    val viewModel: MainViewModel = viewModel()
////    val modelManager = viewModel.modelManager
//
//    val configuration = LocalConfiguration.current
//    val screenHeightDp = configuration.screenHeightDp.dp
//    val remainingTopSpace = screenHeightDp - 600.dp
//
////    TODO: add topPadding with
//    var topPadding = remainingTopSpace - 200.dp
//    if (topPadding < 0.dp) {
//        topPadding = 0.dp
//    }
//
//    // open button
//    Box(
////        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Button(onClick = {
//            ensureServerBinary(context)
//            sheetController.show()
////            db.modelQueries.deleteHiddenModels()
////            deleteUnknownModels(context)
//        }) {
//            Text("Open Sheet")
//        }
//    }
//
//    if (!sheetController.expanded) {
//        return
//    }
//
//    ModalBottomSheet(
//        onDismissRequest = { sheetController.hide() },
//        sheetState = bottomSheetState,
//        sheetMaxWidth = 600.dp,
//        modifier = Modifier
//            .statusBarsPadding()
//            .padding(top = topPadding)
//    ) {
//        // Host state remembered once per sheet lifecycle
////        val snackbarHostState = remember { SnackbarHostState() }
//
////        val modelPicker = modelPickerLauncher(onProgress = { modelName, percent ->
////            copiedModels[modelName] = percent
////            println(copiedModels)
////        })
//
//        // Sheet content
////            Column(
////                verticalArrangement = Arrangement.spacedBy(16.dp),
////                modifier = Modifier
////                    .padding(start = 16.dp, end = 16.dp)
////            ) {
////                Row(
////                    Modifier.fillMaxWidth(),
////                    verticalAlignment = Alignment.CenterVertically,
////                    horizontalArrangement = Arrangement.SpaceBetween
////                ) {
//        Box {
//            LazyColumn(
//                modifier = Modifier
//                    .padding(start = 16.dp, end = 16.dp)
//            ) {
//                item {
//                    Row(
//                        Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text("Model manager", style = MaterialTheme.typography.titleLarge)
//
////                        Box(  // Plus button
////                            modifier = Modifier
////                            //                            .align(Alignment.CenterEnd)
////                        ) {
////                            IconButton(
////                                onClick = { addModelDropdownController.toggle() },
////                                modifier = Modifier
////                                    .align(Alignment.CenterEnd)
////                            ) {
////                                Icon(Icons.Default.Add, contentDescription = "Add a model")
////                            }
////
////                            AddModelDropdown(
////                                controller = addModelDropdownController,
////                                onCopyFromStorage = {
////                                    db.modelQueries.deleteHiddenModels()
////                                    modelPicker.launch("*/*")
////                                },
////                                onUseExternalModel = { notAvailablePopupController.show() },
////                            )
////                        }
//
//                        Box(
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Button(onClick = {
//                                showDialog.show()
////                                notAvailablePopupController.show()
//                            }) {
//                                Text("Add")
//                            }
//                        }
//
//
////                        IconButton(onClick = {
////                            // example insert
////                            scope.launch {
////    //                                withContext(Dispatchers.IO) {
////                                db.modelQueries.insert("Item ${System.currentTimeMillis()}", System.currentTimeMillis())
////    //                                }
////                                deleteModelSnackbarHostState.showSnackbar("Inserted new item", duration = SnackbarDuration.Short)
////                            }
////                        }) {
////                            Icon(Icons.Default.Adb, contentDescription = "Add")
////                        }
//                    }
//                } // item
//
//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//
//                itemsIndexed(
//                    items = copiedModels.entries.toList(),
//                    key = { _, entry -> entry.key } // stable key = model name
//                ) { index, entry ->
//                    val modelName = entry.key
//                    val copyStatus = entry.value
//
//                    ActivelyCopiedModels(modelName, copyStatus)
//                }
//
//                itemsIndexed(
//                    items = models,
//                    key = { _, model -> model.id }
//                ) { index, model ->
//                    ModelItem(model, index, models)
//                }
//            } // LazyColumn
//
//            key("snackbarHost") {
//                SnackbarPopup(deleteModelSnackbarHostState)
//            }
//        } // Box
//    } // ModalBottomSheet
//
//    if (showDialog.expanded) {
//        var text = ""
//
//        Dialog(onDismissRequest = { showDialog.hide() }) {
//            Surface(
//                shape = RoundedCornerShape(24.dp),
//                tonalElevation = 6.dp
//            ) {
//                Column(Modifier.padding(24.dp)) {
//                    Text("Enter name")
//                    Spacer(Modifier.height(16.dp))
//                    OutlinedTextField(
//                        value = text,
//                        onValueChange = { text = it }
//                    )
//                    Spacer(Modifier.height(16.dp))
//                    Button(onClick = { showDialog.hide() }) {
//                        Text("Save")
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LegacyButtonWithBottomSheet() {
//    val context = LocalContext.current
////    val viewModel: MainViewModel = viewModel()
//    val scope = rememberCoroutineScope()
//    val db = DatabaseProvider.database
//
////    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
//    val configuration = LocalConfiguration.current
//    val screenHeightDp = configuration.screenHeightDp.dp
//
////    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
////    var processes by remember { mutableStateOf(0) }
//
////    var copyProgresses: MutableMap<String, CopyStatus> = mutableMapOf()
//    val copiedModels = remember { mutableStateMapOf<String, CopyStatus>() }
//
//    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
//    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
//
//    val sheetController = remember { ExpandController() }
//    val addModelDropdownController = remember { ExpandController() }
//    val buttonAddModelDropdownController = remember { ExpandController() }
//
////    val viewModel: MainViewModel = viewModel()
////    val modelManager = viewModel.modelManager
//
//    val remainingTopSpace = screenHeightDp - 600.dp
//    var topPadding = remainingTopSpace - 200.dp
//    if (topPadding < 0.dp) {
//        topPadding = 0.dp
//    }
//
////    val activelyCopiedModels = copiedModels
////        .filterValues { it.success == null }
////        .entries.toList()
//
//    Button(
//        onClick = {
//            sheetController.show()
//            db.modelQueries.deleteHiddenModels()
//            deleteUnknownModels(context)
//        }
//    ) {
//        Text("Open from here")
//    }
//
//    // This can be triggered by the parent or internally
//    if (sheetController.expanded) {
//        ModalBottomSheet(
//            onDismissRequest = {
//                sheetController.hide()
//                deleteUnknownModels(context)
//            },
//            sheetState = bottomSheetState,
//            sheetMaxWidth = 600.dp,
//            modifier = Modifier
////                .statusBarsPadding()
//                .padding(top = topPadding)
////                .fillMaxWidth()
//        ) {
////            val modelPicker = modelPickerLauncher(onProgress = { modelName, percent ->
////                copiedModels[modelName] = percent
////                println(copiedModels)
////            })
//
//            Box {
//
//                //            SnackbarPopup(deleteModelSnackbarHostState)
////                key("snackbarHost") {
////                    SnackbarPopup(deleteModelSnackbarHostState)
////                }
//                key("snackbarHost") {
//                    SnackbarHost(
//                        hostState = deleteModelSnackbarHostState,
//                        modifier = Modifier
//                            .align(Alignment.BottomCenter)
//                            .padding(16.dp)
//                    )
//                }
//
//                //            LazyColumn(
//                //                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
//                ////                verticalArrangement = Arrangement.spacedBy(12.dp),
//                ////                verticalArrangement = Arrangement.Top,
//                //                horizontalAlignment = Alignment.CenterHorizontally,
//                //                modifier = Modifier
//                //            ) {
//                //                item {
//
//                Column(
//                    modifier = Modifier
////                        .fillMaxSize()
//                        .padding(16.dp)
//                ) {
//                    Text(
//                        "Select an LLM Model",
//                        style = MaterialTheme.typography.headlineMedium,
////                        modifier = Modifier.align(Alignment.Center)
//                    )
//
//                    Box(  // Plus button
//                        modifier = Modifier
////                            .align(Alignment.CenterEnd)
//                    ) {
//                        IconButton(
//                            onClick = { addModelDropdownController.toggle() },
//                            modifier = Modifier
//                                .align(Alignment.CenterEnd)
//                        ) {
//                            Icon(Icons.Default.Add, contentDescription = "Add a model")
//                        }
//
//                        AddModelDropdown(
//                            controller = addModelDropdownController,
//                            onCopyFromStorage = {
//                                db.modelQueries.deleteHiddenModels()
////                                modelPicker.launch("*/*")
//                            },
//                            onUseExternalModel = { notAvailablePopupController.show() },
//                        )
//                    }
//                    LegacyModelList()
//                }
//            }
//        }
//
//
//        //                    if (anyVisibleModel) {
//        //                        Box {
//        //                            Button(
//        //                                onClick = { buttonAddModelDropdownController.toggle() },
//        //                                modifier = Modifier
//        //                            ) {
//        //                                Text(
//        //                                    "Add a model"
//        //                                )
//        //                            }
//        //
//        //                            AddModelDropdown(
//        //                                controller = buttonAddModelDropdownController,
//        //                                onCopyFromStorage = {
//        //                                    db.modelQueries.deleteHiddenModels()
//        //                                    modelPicker.launch("*/*")
//        //                                },
//        //                                onUseExternalModel = { notAvailablePopupController.show() },
//        //                            )
//        //                        }
//        //                    }
////                for (model in models) {
////                    Log.d("Database", "Model: ${model.name}")
////                }
//
////                itemsIndexed(
////                    items = copiedModels.entries.toList(),
////                    key = { _, entry -> entry.key } // use model name as stable key
////                ) { index, entry ->
////                    val modelName = entry.key
////                    val copyStatus = entry.value
////
//////                    if (copyStatus.success == null) {  // if in progress
////                    CoppingModelCard(modelName, copyStatus)
//////                    }
////
//////                    Row(
//////                        modifier = Modifier
//////                            .fillMaxWidth()
//////                            .padding(8.dp),
//////                        verticalAlignment = Alignment.CenterVertically
//////                    ) {
//////                        Text("$modelName: ${copyStatus.percent}%")
//////                        CoppingModelCard(modelName, copyStatus)
//////                    }
////                }
//
////            }
////        }
//    }
//}
//
//
//
//@Composable
//fun LegacyModelList() {
//    val context = LocalContext.current
////    val viewModel: MainViewModel = viewModel()
//    val scope = rememberCoroutineScope()
//    val db = DatabaseProvider.database
//
//
//    val models by db.modelQueries
//        .selectAll()
//        .asFlow()
//        .mapToList(Dispatchers.IO)
//        .collectAsState(initial = emptyList())
//
////    val visibleModels = models.filter { it.show }
//    //    val hasHiddenModels = db.modelQueries.hasHiddenModels().executeAsOne()
////    val hiddenModelsCount = db.modelQueries.countHiddenModels().executeAsOne().toInt()
//    //    val anyVisibleModel = !(hasHiddenModels or models.isEmpty()) and !(hasHiddenModels and models.isEmpty())
////    val anyVisibleModel = models.isEmpty() or (hiddenModelsCount == models.size)
//    //    println("hasVisibleModels: $hasHiddenModels \nmodels.isEmpty(): ${models.isEmpty()}")
//
//
//    LazyColumn(
////        contentPadding = PaddingValues(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        itemsIndexed(
//            items = models,
//            key = { _, model -> model.id }  // keep stable key
//        ) { index, model ->
////                ModelCard(model, index, visibleModels.lastIndex)
//            LegacyModelCard(model, index, 7)
//
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LegacyModelCard(model: Model, index: Int, lastIndex: Int) {
//    if (!model.show) {
//        return
//    }
//
//    val context = LocalContext.current
//    val db = DatabaseProvider.database
//
////    val chatViewModel: ChatViewModel = viewModel()
////    val viewModel: MainViewModel = viewModel() // or hiltViewModel() if using Hilt
////    val modelManager = viewModel.modelManager
//
//    val scope = rememberCoroutineScope()
//    println("index: $index, lastIndex: $lastIndex")
//    var expandedCard by remember { mutableStateOf(index == 0) }
//
//    val cornerLarge = 24.dp   // for first/last
//    val cornerSmall = 8.dp    // for middle items
//
//    val isFirst = index == 0
//    val isLast = index == lastIndex
//
//    val shape = when {
//        isLast and isFirst -> RoundedCornerShape(
//            topStart = cornerLarge,
//            topEnd = cornerLarge,
//            bottomStart = cornerLarge,
//            bottomEnd = cornerLarge
//        )
//        isFirst -> RoundedCornerShape(
//            topStart = cornerLarge,
//            topEnd = cornerLarge,
//            bottomStart = cornerSmall,
//            bottomEnd = cornerSmall
//        )
//        isLast -> RoundedCornerShape(
//            topStart = cornerSmall,
//            topEnd = cornerSmall,
//            bottomStart = cornerLarge,
//            bottomEnd = cornerLarge
//        )
//        else -> RoundedCornerShape(cornerSmall)
//    }
//    val padding = when {
//        isLast -> 0.dp
//        else -> 4.dp
//    }
//
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = padding)
//    ) {
//        Button(
//            onClick = {
////                val resilt = modelManager.loadModel()
////                println("resilt: $resilt")
//            },
////            shape = RoundedCornerShape(32.dp),
//            shape = shape,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Column {
//                val rotation by animateFloatAsState(
//                    targetValue = if (expandedCard) 180f else 0f,
//
//                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
//                    label = "arrowRotation"
//                )
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        model.name,
//                        modifier = Modifier
//                            .align(Alignment.CenterStart)
//                            .padding(end = 45.dp)
//                    )
//                    IconButton(
//                        onClick = { expandedCard = !expandedCard },
//                        modifier = Modifier
//                            .align(Alignment.CenterEnd)
//                    ) {
//                        Icon(
//                            Icons.Default.ArrowDropDown,
//                            contentDescription = "Expand",
//                            modifier = Modifier
//                                .graphicsLayer { rotationZ = rotation }
//                        )
//                    }
//                }
//
//                AnimatedVisibility(
//                    visible = expandedCard,
//                    enter = expandVertically(),
//                    exit = shrinkVertically()
//                ) {
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        IconButton(
//                            onClick = { notAvailablePopupController.show() },
//                            modifier = Modifier
//                                .align(Alignment.CenterStart)
//                        ) {
//                            Icon(Icons.Default.Edit, contentDescription = "Rename")
//                        }
//
//                        Button(
//                            onClick = {
//                                notAvailablePopupController.show()
//                            },
//                            modifier = Modifier
//                                .align(Alignment.Center)
//                        ) {
//                            Text("Load")
//                        }
//
//                        IconButton(
//                            onClick = {
//                                db.modelQueries.updateShowStatus(false, model.id)
//                                scope.launch {
//                                    val result = deleteModelSnackbarHostState.showSnackbar(
//                                        message = "Model deleted",
//                                        actionLabel = "Undo",
//                                        withDismissAction = true,
//                                        duration = SnackbarDuration.Short
//                                    )
//
//                                    when (result) {
//                                        SnackbarResult.ActionPerformed ->
//                                            db.modelQueries.updateShowStatus(true, model.id)
//
//                                        SnackbarResult.Dismissed -> {
//                                            db.modelQueries.deleteHiddenModels()
//                                            deleteUnknownModels(context)
//                                        }
//                                    }
//                                }
//                            },
//                            modifier = Modifier
//                                .align(Alignment.CenterEnd)
//                        ) {
//                            Icon(Icons.Default.Delete, contentDescription = "Delete")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LegacyCoppingModelCard(modelName: String, copyStatus: CopyStatus) {
//    val context = LocalContext.current
//    val db = DatabaseProvider.database
//
//    val scope = rememberCoroutineScope()
//
//    if (copyStatus.success == null) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
////                .padding(bottom = 24.dp)
//        ) {
//            Button(
//                onClick = { notAvailablePopupController.show() },
//                shape = RoundedCornerShape(32.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Column {
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Copying $modelName",
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            modifier = Modifier
//                                .align(Alignment.CenterStart)
//                                .padding(end = 45.dp)
//                        )
//                        Text(
//                            "${copyStatus.percent}%",
//                            modifier = Modifier
//                                .align(Alignment.CenterEnd)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}