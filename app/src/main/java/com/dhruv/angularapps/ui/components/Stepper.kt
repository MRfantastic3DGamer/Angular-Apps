package com.dhruv.angularapps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun IntStepper(
    label: String,
    value: Int,
    min: Int,
    max: Int,
    modifier: Modifier = Modifier,
    onChange: (Int) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        FilledTonalIconButton(
            onClick = {
                if (value > min) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onChange(value - 1)
                }
            },
            enabled = value > min,
        ) {
            Text("−", style = MaterialTheme.typography.titleLarge)
        }
        Surface(
            tonalElevation = 1.dp,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.widthIn(min = 48.dp),
        ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        FilledTonalIconButton(
            onClick = {
                if (value < max) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onChange(value + 1)
                }
            },
            enabled = value < max,
        ) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun FloatStepper(
    label: String,
    value: Float,
    min: Float,
    max: Float,
    step: Float,
    format: (Float) -> String,
    modifier: Modifier = Modifier,
    onChange: (Float) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val dec = (value / step).roundToInt()
    val minSteps = (min / step).roundToInt()
    val maxSteps = (max / step).roundToInt()

    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        FilledTonalIconButton(
            onClick = {
                if (dec > minSteps) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onChange((dec - 1) * step)
                }
            },
            enabled = dec > minSteps,
        ) {
            Text("−", style = MaterialTheme.typography.titleLarge)
        }
        Surface(
            tonalElevation = 1.dp,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.widthIn(min = 56.dp),
        ) {
            Text(
                text = format(value),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        FilledTonalIconButton(
            onClick = {
                if (dec < maxSteps) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onChange((dec + 1) * step)
                }
            },
            enabled = dec < maxSteps,
        ) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
    }
}
