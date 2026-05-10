package com.dhruv.angularapps.settings_app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R
import com.dhruv.angularapps.settings_app.settings.editors.AppsLookEditor
import com.dhruv.angularapps.settings_app.settings.editors.GroupsLookEditor
import com.dhruv.angularapps.settings_app.settings.editors.SliderPositionEditor
import com.dhruv.angularapps.settings_app.settings.editors.SliderSizeEditor
import com.dhruv.angularapps.settings_app.settings.editors.TouchOffsetEditor
import com.dhruv.angularapps.ui.components.SectionHeader
import com.dhruv.angularapps.ui.components.SettingsListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    modifier: Modifier = Modifier,
    vm: SettingsVM,
    snackbarHostState: SnackbarHostState,
    haveOverlayPermission: Boolean,
    isOverlayServiceRunning: () -> Boolean,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    scrollConnection: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var localRunning by remember { mutableStateOf(isOverlayServiceRunning()) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val sheetTitle = when (vm.popup) {
        1.1f -> "Touch offset"
        2.1f -> "Slider size"
        2.2f -> "Slider position"
        3.2f -> "App icon look"
        4.2f -> "Group icon look"
        else -> "Settings"
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .then(scrollConnection),
            ) {
            item {
                ServiceStatusCard(
                    isRunning = localRunning,
                    haveOverlayPermission = haveOverlayPermission,
                    onStart = {
                        if (haveOverlayPermission) onStartService()
                        localRunning = isOverlayServiceRunning()
                    },
                    onStop = {
                        onStopService()
                        localRunning = isOverlayServiceRunning()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Interaction",
                    description = "How touches map to the overlay",
                )
            }
            item {
                SettingsListItem(
                    icon = painterResource(R.drawable.round_pan_tool_alt_24),
                    title = "Touch offset",
                    subtitle = "Align finger position with what the app registers",
                    onClick = { vm.openPopup(1.1f) },
                )
            }

            item {
                SectionHeader(
                    title = "Slider",
                    description = "The trigger on the bottom-right of the screen",
                )
            }
            item {
                SettingsListItem(
                    icon = painterResource(R.drawable.round_open_in_full_24),
                    title = "Size",
                    subtitle = "Height and width of the slider",
                    onClick = { vm.openPopup(2.1f) },
                )
            }
            item {
                SettingsListItem(
                    icon = painterResource(R.drawable.round_location_searching_24),
                    title = "Position",
                    subtitle = "Distance from the bottom edge",
                    onClick = { vm.openPopup(2.2f) },
                )
            }

            item {
                SectionHeader(
                    title = "App icons",
                    description = "Icons when a group is open",
                )
            }
            item {
                SettingsListItem(
                    icon = painterResource(R.drawable.round_looks_24),
                    title = "Look",
                    subtitle = "Base radius for app icons",
                    onClick = { vm.openPopup(3.2f) },
                )
            }

            item {
                SectionHeader(
                    title = "Group icons",
                    description = "Icons on the slider",
                )
            }
            item {
                SettingsListItem(
                    icon = painterResource(R.drawable.round_looks_24),
                    title = "Look",
                    subtitle = "Base radius for group icons",
                    onClick = { vm.openPopup(4.2f) },
                )
            }

            item { Spacer(Modifier.height(88.dp)) }
        }
        }

        if (vm.popup != 0f) {
            ModalBottomSheet(
                onDismissRequest = { vm.dismiss() },
                sheetState = sheetState,
            ) {
            Column(Modifier.padding(bottom = 24.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(
                                when (vm.popup) {
                                    1.1f -> R.drawable.round_pan_tool_alt_24
                                    2.1f, 2.2f -> R.drawable.round_swipe_vertical_24
                                    3.2f -> R.drawable.round_apps_24
                                    4.2f -> R.drawable.baseline_group_work_24
                                    else -> R.drawable.round_report_gmailerrorred_24
                                },
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = sheetTitle,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                    TextButton(onClick = { vm.openPopup(vm.popup) }) {
                        Text("Reset")
                    }
                }
                HorizontalDivider()
                when (vm.popup) {
                    1.1f -> TouchOffsetEditor(vm)
                    2.1f -> SliderSizeEditor(vm)
                    2.2f -> SliderPositionEditor(vm)
                    3.2f -> AppsLookEditor(vm)
                    4.2f -> GroupsLookEditor(vm)
                    else -> Text("Not available", Modifier.padding(16.dp))
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = { vm.dismiss() }) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.size(8.dp))
                    Button(
                        onClick = {
                            vm.confirm()
                            scope.launch {
                                snackbarHostState.showSnackbar("Settings saved")
                            }
                        },
                    ) {
                        Text("Save")
                    }
                }
            }
        }
        }
    }
}

@Composable
private fun ServiceStatusCard(
    isRunning: Boolean,
    haveOverlayPermission: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(
                        if (isRunning) R.drawable.round_swipe_vertical_24 else R.drawable.round_lock_24,
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(32.dp),
                )
                Column(Modifier.padding(start = 12.dp)) {
                    Text(
                        text = if (isRunning) "Overlay running" else "Overlay stopped",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = if (haveOverlayPermission) {
                            if (isRunning) "Quick access is active over other apps."
                            else "Start the overlay from Home or here."
                        } else {
                            "Grant draw-over-other-apps permission on the Home tab."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            if (haveOverlayPermission) {
                if (isRunning) {
                    OutlinedButton(onClick = onStop, modifier = Modifier.fillMaxWidth()) {
                        Text("Stop overlay")
                    }
                } else {
                    Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
                        Text("Start overlay")
                    }
                }
            }
        }
    }
}
