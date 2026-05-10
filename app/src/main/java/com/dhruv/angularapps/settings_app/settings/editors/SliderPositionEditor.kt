package com.dhruv.angularapps.settings_app.settings.editors

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.settings_app.settings.SettingsVM
import com.dhruv.angularapps.ui.components.IntStepper
import com.dhruv.angularapps.ui.components.PhonePreviewFrame
import kotlin.math.roundToInt

@Composable
fun SliderPositionEditor(vm: SettingsVM, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Distance from the bottom of the screen to the slider. Drag the slider or use presets.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        PhonePreviewFrame {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val maxH = constraints.maxHeight.toFloat()
                val maxW = constraints.maxWidth.toFloat()
                val padScale = maxH / 1000f
                val sliderH = (150f / 1000f * maxH).coerceIn(48f, maxH * 0.45f)
                val sliderW = (50f / 150f * maxW * 0.25f).coerceIn(20f, maxW * 0.2f)
                val bottomPadPx = vm.slBottomPadding * padScale
                val sliderBottomY = maxH - bottomPadPx

                Canvas(Modifier.fillMaxSize()) {
                    val x0 = size.width - sliderW - 8f
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.6f),
                        start = Offset(x0 + sliderW / 2f, sliderBottomY + sliderH),
                        end = Offset(x0 + sliderW / 2f, size.height - 4f),
                        strokeWidth = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                    )
                }

                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                ) {
                    Text(
                        text = "${vm.slBottomPadding} dp",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }

                val sliderHdp = with(density) { sliderH.toDp() }
                val sliderWdp = with(density) { sliderW.toDp() }

                Box(
                    modifier = Modifier
                        .offset(
                            x = with(density) { (maxW - sliderW - 8f).toDp() },
                            y = with(density) { (sliderBottomY - sliderH).coerceAtLeast(0f).toDp() },
                        )
                        .size(width = sliderWdp, height = sliderHdp)
                        .background(Color.Black, RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                        .pointerInput(padScale, maxH) {
                            var acc = vm.slBottomPadding
                            detectDragGestures(
                                onDragStart = { acc = vm.slBottomPadding },
                                onDrag = { change, drag ->
                                    change.consume()
                                    acc = (acc - (drag.y / padScale).roundToInt()).coerceIn(0, 1000)
                                    vm.slBottomPadding = acc
                                },
                            )
                        },
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        IntStepper("Bottom padding (px)", vm.slBottomPadding, 0, 1000) { vm.slBottomPadding = it }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = vm.slBottomPadding == 12,
                onClick = { vm.slBottomPadding = 12 },
                label = { Text("Low") },
            )
            FilterChip(
                selected = vm.slBottomPadding == 120,
                onClick = { vm.slBottomPadding = 120 },
                label = { Text("Comfort") },
            )
            FilterChip(
                selected = vm.slBottomPadding == 400,
                onClick = { vm.slBottomPadding = 400 },
                label = { Text("High") },
            )
        }
    }
}
