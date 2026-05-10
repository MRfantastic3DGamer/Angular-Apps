package com.dhruv.angularapps.settings_app

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R

@Composable
fun Home(
    modifier: Modifier = Modifier,
    haveOverlayPermission: Boolean,
    enableOverlayPermission: () -> Unit,
    startOverlayService: () -> Unit,
    stopOverlayService: () -> Unit,
    isOverlayServiceRunning: () -> Boolean,
    scrollConnection: Modifier = Modifier,
) {
    var isOverlayRunning by remember { mutableStateOf(isOverlayServiceRunning()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .then(scrollConnection),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            Text(
                text = "Quick access",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Open your app groups with one gesture, anywhere.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
        }

        item {
            StatusHeroCard(
                isRunning = isOverlayRunning,
                haveOverlayPermission = haveOverlayPermission,
            )
        }

        item {
            Crossfade(targetState = isOverlayRunning, label = "cta") { running ->
                if (running) {
                    OutlinedButton(
                        onClick = {
                            stopOverlayService()
                            isOverlayRunning = isOverlayServiceRunning()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.round_lock_24),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                        )
                        Text("Remove from home", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    FilledTonalButton(
                        onClick = {
                            if (haveOverlayPermission) startOverlayService()
                            isOverlayRunning = isOverlayServiceRunning()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.round_swipe_vertical_24),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(22.dp),
                        )
                        Text("Set at home", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        if (!haveOverlayPermission) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Permission needed",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                        Text(
                            text = "Allow drawing over other apps so the overlay can appear on top.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        Button(
                            onClick = enableOverlayPermission,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                        ) {
                            Text("Open permission settings")
                            Icon(
                                painter = painterResource(R.drawable.round_arrow_outward_24),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "How it works",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )
        }

        item {
            StepRow(
                step = 1,
                title = "Create groups",
                body = "Add app groups on the Groups tab and pick icons.",
                iconRes = R.drawable.baseline_group_work_24,
            )
        }
        item {
            StepRow(
                step = 2,
                title = "Enable overlay",
                body = "Grant the overlay permission once, then start the service.",
                iconRes = R.drawable.round_arrow_outward_24,
            )
        }
        item {
            StepRow(
                step = 3,
                title = "Swipe the slider",
                body = "Use the curved bar at the bottom-right to open your groups.",
                iconRes = R.drawable.round_swipe_vertical_24,
            )
        }

        item { Spacer(Modifier.height(72.dp)) }
    }
}

@Composable
private fun StatusHeroCard(
    isRunning: Boolean,
    haveOverlayPermission: Boolean,
) {
    val pulse = rememberInfiniteTransition(label = "pulse")
    val alpha by pulse.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dotAlpha",
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .alpha(if (isRunning) alpha else 0.4f)
                        .background(
                            if (isRunning) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline,
                            CircleShape,
                        ),
                )
                Text(
                    text = if (isRunning) "Running" else "Stopped",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
            Text(
                text = when {
                    !haveOverlayPermission -> "Overlay permission is off — enable it below."
                    isRunning -> "Your groups are ready over any screen."
                    else -> "Start the service to pin quick access to your home workflow."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun StepRow(
    step: Int,
    title: String,
    body: String,
    iconRes: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = step.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 12.dp),
            )
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    body,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
