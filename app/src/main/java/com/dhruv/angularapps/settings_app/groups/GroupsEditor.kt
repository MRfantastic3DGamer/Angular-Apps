package com.dhruv.angularapps.settings_app.groups

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R
import com.dhruv.angularapps.data.models.Group
import com.dhruv.angularapps.utils.rememberDrawablePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsEditor(
    modifier: Modifier = Modifier,
    vm: GroupsEditorVM,
    snackbarHostState: SnackbarHostState,
    scrollConnection: Modifier = Modifier,
) {
    val groups = vm.groups.collectAsState(initial = emptyList()).value
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(vm.errorPop, vm.errorMessage) {
        if (vm.errorPop && vm.errorMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(vm.errorMessage)
            vm.closeErrorPopup()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(scrollConnection),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vm.openAutoGroupSheet() },
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.round_travel_explore_24),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        )
                        Column(Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(
                                text = "Suggest groups for me",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "Games, music, maps, and more from app categories",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            text = "›",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            items(groups.size, key = { groups[it].key + it }) { index ->
                val group = groups[index]
                ElevatedCard(
                    onClick = { vm.selectGroup(group, index) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val painter = if (GroupIcons.containsKey(group.key)) {
                            painterResource(id = GroupIcons[group.key]!!)
                        } else {
                            painterResource(id = R.drawable.round_report_gmailerrorred_24)
                        }
                        Image(
                            painter = painter,
                            contentDescription = group.key,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .padding(4.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        )
                        Column(Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = "${group.apps.size} apps",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        if (vm.showAutoGroupSheet) {
            val autoSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { vm.dismissAutoGroupSheet() },
                sheetState = autoSheetState,
                modifier = Modifier.heightIn(max = 640.dp),
            ) {
                AutoGroupSuggestionSheetContent(vm = vm)
            }
        }

        if (vm.showGroupEditingDialog) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { vm.dismiss() },
                sheetState = sheetState,
                modifier = Modifier.heightIn(max = 720.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { vm.showGroupIconChoices = !vm.showGroupIconChoices }) {
                            Image(
                                painter = painterResource(
                                    GroupIcons.getOrDefault(vm.keyValue, R.drawable.round_report_gmailerrorred_24),
                                ),
                                contentDescription = "Group icon",
                                modifier = Modifier.size(52.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            )
                        }
                        OutlinedTextField(
                            value = vm.nameValue.text,
                            onValueChange = { vm.nameValue = TextFieldValue(it) },
                            label = { Text("Group name") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            singleLine = true,
                        )
                    }

                    if (vm.showGroupIconChoices) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                        ) {
                            items(GroupIcons.keys.toList(), key = { it }) { key ->
                                FilterChip(
                                    selected = vm.keyValue == key,
                                    onClick = {
                                        vm.keyValue = key
                                        vm.showGroupIconChoices = false
                                    },
                                    label = { Text(key, maxLines = 1) },
                                    leadingIcon = {
                                        Image(
                                            painter = painterResource(GroupIcons[key]!!),
                                            contentDescription = null,
                                            modifier = Modifier.size(22.dp),
                                        )
                                    },
                                )
                            }
                        }
                    }

                    HorizontalDivider(Modifier.padding(vertical = 8.dp))

                    Text(
                        "Selected apps",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Row(
                        Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        vm.selectedAppsForUi.forEach { pkg ->
                            InputChip(
                                selected = true,
                                onClick = { vm.removeApp(pkg) },
                                label = {
                                    Text(
                                        vm.apps[pkg] ?: pkg,
                                        maxLines = 1,
                                        modifier = Modifier.widthIn(max = 120.dp),
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_drag_indicator_24),
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(18.dp),
                                    )
                                },
                            )
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search apps") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        singleLine = true,
                    )

                    val appsSorted = vm.apps.keys
                        .sortedBy { vm.apps[it] }
                        .filter { pkg ->
                            val name = vm.apps[pkg] ?: ""
                            name.contains(searchQuery, ignoreCase = true) ||
                                pkg.contains(searchQuery, ignoreCase = true)
                        }

                    if (vm.apps.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp),
                        ) {
                            items(appsSorted, key = { it }) { app ->
                                if (vm.apps.containsKey(app)) {
                                    val checked = vm.isAppSelected(app)
                                    val name = vm.apps[app] ?: app
                                    val drawable = vm.appsIcons[app]
                                    ListItem(
                                        headlineContent = { Text(name) },
                                        leadingContent = {
                                            Image(
                                                painter = if (drawable != null) {
                                                    rememberDrawablePainter(drawable = drawable)
                                                } else {
                                                    painterResource(R.drawable.round_report_gmailerrorred_24)
                                                },
                                                contentDescription = app,
                                                modifier = Modifier.size(40.dp),
                                            )
                                        },
                                        trailingContent = {
                                            Checkbox(
                                                checked = checked,
                                                onCheckedChange = {
                                                    if (checked) vm.removeApp(app) else vm.addApp(app)
                                                },
                                            )
                                        },
                                        modifier = Modifier.clickable {
                                            if (checked) vm.removeApp(app) else vm.addApp(app)
                                        },
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextButton(onClick = { vm.delete() }) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                        Button(onClick = { vm.confirm() }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AutoGroupSuggestionSheetContent(vm: GroupsEditorVM) {
    val selectedCount = vm.suggestions.count { it.group.key in vm.selectedSuggestionKeys }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
    ) {
        Text(
            text = "Suggested groups",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Text(
            text = "Based on app categories (same signal game launchers use for games).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        if (vm.suggestions.isEmpty()) {
            Text(
                text = "No category data found for your apps. You can still create groups manually.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(24.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp),
            ) {
                items(vm.suggestions.size, key = { vm.suggestions[it].group.key }) { index ->
                    val suggestion = vm.suggestions[index]
                    val g = suggestion.group
                    val checked = g.key in vm.selectedSuggestionKeys
                    val supporting = buildString {
                        append("${g.apps.size} apps")
                        if (suggestion.totalCandidates > g.apps.size) {
                            append(" of ${suggestion.totalCandidates}")
                        }
                    }
                    ListItem(
                        headlineContent = { Text(g.name) },
                        supportingContent = { Text(supporting) },
                        leadingContent = {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.size(44.dp),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Image(
                                        painter = painterResource(
                                            GroupIcons[g.key] ?: R.drawable.round_report_gmailerrorred_24,
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(26.dp),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                                    )
                                }
                            }
                        },
                        trailingContent = {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { vm.toggleSuggestionKey(g.key) },
                            )
                        },
                        modifier = Modifier.clickable { vm.toggleSuggestionKey(g.key) },
                    )
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = { vm.dismissAutoGroupSheet() }) {
                Text("Cancel")
            }
            Spacer(Modifier.size(8.dp))
            Button(
                onClick = { vm.confirmAutoGroups() },
                enabled = vm.suggestions.isNotEmpty() && selectedCount > 0,
            ) {
                Text("Add $selectedCount group(s)")
            }
        }
    }
}
