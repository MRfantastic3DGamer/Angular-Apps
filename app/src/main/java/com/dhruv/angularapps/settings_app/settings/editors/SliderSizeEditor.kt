package com.dhruv.angularapps.settings_app.settings.editors

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.settings_app.settings.SettingsVM
import com.dhruv.angularapps.ui.components.IntStepper
import com.dhruv.angularapps.ui.components.PhonePreviewFrame
import kotlin.math.roundToInt

private data class SizePreset(val label: String, val h: Int, val w: Int)

private val sizePresets = listOf(
    SizePreset("Compact", 120, 35),
    SizePreset("Default", 150, 50),
    SizePreset("Tall", 320, 60),
)

@Composable
fun SliderSizeEditor(vm: SettingsVM, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Resize the trigger on the right edge. Drag the purple handle to adjust height and width.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        PhonePreviewFrame {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val maxH = constraints.maxHeight.toFloat()
                val maxW = constraints.maxWidth.toFloat()
                val scaleH = maxH / 1000f
                val scaleW = maxW / 150f
                val drawH = (vm.slHeight * scaleH).coerceIn(40f, maxH)
                val drawW = (vm.slWidth * scaleW).coerceIn(16f, maxW * 0.35f)
                val drawHdp = with(density) { drawH.toDp() }
                val drawWdp = with(density) { drawW.toDp() }

                Box(Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(width = drawWdp, height = drawHdp)
                            .offset(x = (-4).dp, y = (-4).dp)
                            .background(Color.Black, RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
                            .pointerInput(scaleH, scaleW) {
                                var accH = vm.slHeight
                                var accW = vm.slWidth
                                detectDragGestures(
                                    onDragStart = {
                                        accH = vm.slHeight
                                        accW = vm.slWidth
                                    },
                                    onDrag = { change, drag ->
                                        change.consume()
                                        accH = (accH - (drag.y / scaleH).roundToInt()).coerceIn(100, 1000)
                                        accW = (accW + (drag.x / scaleW).roundToInt()).coerceIn(25, 150)
                                        vm.slHeight = accH
                                        vm.slWidth = accW
                                    },
                                )
                            },
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(18.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(4.dp),
                                ),
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        IntStepper("Height (px)", vm.slHeight, 100, 1000) { vm.slHeight = it }
        IntStepper("Width (px)", vm.slWidth, 25, 150) { vm.slWidth = it }
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            sizePresets.forEach { preset ->
                FilterChip(
                    selected = vm.slHeight == preset.h && vm.slWidth == preset.w,
                    onClick = {
                        vm.slHeight = preset.h
                        vm.slWidth = preset.w
                    },
                    label = { Text(preset.label) },
                )
            }
        }
    }
}
