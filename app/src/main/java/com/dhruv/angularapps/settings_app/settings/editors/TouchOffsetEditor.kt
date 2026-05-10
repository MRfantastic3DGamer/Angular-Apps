package com.dhruv.angularapps.settings_app.settings.editors

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R
import com.dhruv.angularapps.settings_app.settings.SettingsVM
import com.dhruv.angularapps.ui.components.FloatStepper
import kotlin.math.roundToInt

private const val PAD_MIN = -20f
private const val PAD_MAX = 20f

private fun posToValue(p: Offset, w: Int, h: Int, marginPx: Float): Offset {
    val cx = w / 2f
    val cy = h / 2f
    val rx = (w / 2f - marginPx).coerceAtLeast(1f)
    val ry = (h / 2f - marginPx).coerceAtLeast(1f)
    val nx = ((p.x - cx) / rx) * PAD_MAX
    val ny = ((p.y - cy) / ry) * PAD_MAX
    return Offset(
        nx.coerceIn(PAD_MIN, PAD_MAX),
        ny.coerceIn(PAD_MIN, PAD_MAX),
    )
}

private fun valueToPos(o: Offset, w: Int, h: Int, marginPx: Float): Offset {
    val cx = w / 2f
    val cy = h / 2f
    val rx = (w / 2f - marginPx).coerceAtLeast(1f)
    val ry = (h / 2f - marginPx).coerceAtLeast(1f)
    return Offset(
        cx + (o.x / PAD_MAX) * rx,
        cy + (o.y / PAD_MAX) * ry,
    )
}

@Composable
fun TouchOffsetEditor(
    vm: SettingsVM,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val marginPx = with(density) { 24.dp.toPx() }

    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = "Drag on the pad to set touch calibration. Center is (0, 0).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHighest,
                    MaterialTheme.shapes.large,
                )
                .pointerInput(marginPx) {
                    detectDragGestures(
                        onDragStart = { start ->
                            vm.touchOffset = posToValue(
                                start,
                                size.width.toInt(),
                                size.height.toInt(),
                                marginPx,
                            )
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            vm.touchOffset = posToValue(
                                change.position,
                                size.width.toInt(),
                                size.height.toInt(),
                                marginPx,
                            )
                        },
                    )
                },
        ) {
            val w = constraints.maxWidth
            val h = constraints.maxHeight
            Canvas(Modifier.matchParentSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                drawLine(
                    color = Color.Gray.copy(alpha = 0.45f),
                    start = Offset(0f, cy),
                    end = Offset(size.width, cy),
                    strokeWidth = 2f,
                )
                drawLine(
                    color = Color.Gray.copy(alpha = 0.45f),
                    start = Offset(cx, 0f),
                    end = Offset(cx, size.height),
                    strokeWidth = 2f,
                )
            }

            val thumbPos = valueToPos(vm.touchOffset, w, h, marginPx)
            Icon(
                painter = painterResource(R.drawable.round_pan_tool_alt_24),
                contentDescription = null,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (thumbPos.x - with(density) { 20.dp.toPx() }).roundToInt(),
                            (thumbPos.y - with(density) { 20.dp.toPx() }).roundToInt(),
                        )
                    }
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Spacer(Modifier.height(12.dp))
        FloatStepper(
            label = "X offset",
            value = vm.touchOffset.x,
            min = PAD_MIN,
            max = PAD_MAX,
            step = 0.1f,
            format = { "%.1f".format(it) },
            onChange = { vm.touchOffset = vm.touchOffset.copy(x = it) },
        )
        FloatStepper(
            label = "Y offset",
            value = vm.touchOffset.y,
            min = PAD_MIN,
            max = PAD_MAX,
            step = 0.1f,
            format = { "%.1f".format(it) },
            onChange = { vm.touchOffset = vm.touchOffset.copy(y = it) },
        )
        TextButton(
            onClick = { vm.touchOffset = Offset.Zero },
            modifier = Modifier.align(Alignment.End),
        ) {
            Text("Reset to center")
        }
    }
}
