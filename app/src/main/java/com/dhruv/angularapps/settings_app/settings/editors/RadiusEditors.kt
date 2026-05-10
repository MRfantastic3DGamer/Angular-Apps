package com.dhruv.angularapps.settings_app.settings.editors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.settings_app.settings.SettingsVM
import com.dhruv.angularapps.ui.components.IntStepper

@Composable
fun AppsLookEditor(vm: SettingsVM, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Base radius for app icons in the overlay.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        RadiusPreview(radiusDp = vm.appsBaseRad.dp, showInnerIcon = false)
        Spacer(Modifier.height(16.dp))
        IntStepper("Radius (dp)", vm.appsBaseRad, 16, 100) { vm.appsBaseRad = it }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(16 to "S", 28 to "M", 40 to "L", 72 to "XL").forEach { (v, label) ->
                FilterChip(
                    selected = vm.appsBaseRad == v,
                    onClick = { vm.appsBaseRad = v },
                    label = { Text(label) },
                )
            }
        }
        Slider(
            value = vm.appsBaseRad.toFloat(),
            onValueChange = { vm.appsBaseRad = it.toInt() },
            valueRange = 16f..100f,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun GroupsLookEditor(vm: SettingsVM, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Base radius for group icons on the slider.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        RadiusPreview(radiusDp = vm.groupBaseRad.dp, showInnerIcon = true)
        Spacer(Modifier.height(16.dp))
        IntStepper("Radius (dp)", vm.groupBaseRad, 10, 100) { vm.groupBaseRad = it }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(10 to "S", 22 to "M", 36 to "L", 64 to "XL").forEach { (v, label) ->
                FilterChip(
                    selected = vm.groupBaseRad == v,
                    onClick = { vm.groupBaseRad = v },
                    label = { Text(label) },
                )
            }
        }
        Slider(
            value = vm.groupBaseRad.toFloat(),
            onValueChange = { vm.groupBaseRad = it.toInt() },
            valueRange = 10f..100f,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun RadiusPreview(
    radiusDp: androidx.compose.ui.unit.Dp,
    showInnerIcon: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size((radiusDp * 2).coerceAtMost(160.dp))
                .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (showInnerIcon) {
                Box(
                    modifier = Modifier
                        .size((radiusDp).coerceAtMost(72.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("G", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
