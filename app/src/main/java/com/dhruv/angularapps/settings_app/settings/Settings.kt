package com.dhruv.angularapps.settings_app.settings

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.settings_app.LabelForFloat
import com.dhruv.angularapps.settings_app.LabelForInt

const val TAG = "Settings"

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    vm: SettingsVM
) {
    val context = LocalContext.current
    var popup by remember { mutableIntStateOf(0) }
    val dpInv = 1f/1.dp.value

    val slideInTransition = slideInVertically(initialOffsetY = { it + 50 }) + fadeIn()
    val slideOutTransition = slideOutVertically(targetOffsetY = { it + 50 }) + fadeOut()

    Box(modifier = modifier) {
        Column(
            Modifier
        ) {
            Button(onClick = { popup = 1 }) {
                Text(text = "adjust touch offset")
            }
            Button(onClick = { popup = 2 }) {
                Text(text = "adjust slider dimensions")
            }
            Button(onClick = { popup = 3 }) {
                Text(text = "adjust apps look")
            }
            Button(onClick = { popup = 4 }) {
                Text(text = "adjust groups look")
            }
        }

        // dark bg
//        AnimatedVisibility(visible = popup != 0, enter = fadeIn(), exit = fadeOut()) {
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .drawBehind {
//                    drawRect(
//                        Color.Black,
//                        alpha = 0.5f,
//                        topLeft = Offset(0f, 0f),
//                        size = Size(
//                            context.resources.displayMetrics.widthPixels.toFloat(),
//                            context.resources.displayMetrics.heightPixels.toFloat()
//                        )
//                    )
//                }
//                .clickable {
//                    popup = 0
//                }
//                .padding(5.dp)
//            )
//        }

        if (popup != 0) {
            AlertDialog(
                onDismissRequest = { popup = 0 },
                confirmButton = {},
                text = {
                    when (popup) {
                        1 -> {
                            var offset by remember { mutableStateOf(vm.touchOffset()) }

                            Column {

                                LabelForFloat(key = "X", min = -20f, value = offset.x, max = 20f) {
                                    offset = offset.copy(x = it)
                                    vm.saveTouchOffset(offset)
                                }
                                LabelForFloat(key = "Y", min = -20f, value = offset.y, max = 20f) {
                                    offset = offset.copy(y = it)
                                    vm.saveTouchOffset(offset)
                                }
                            }
                        }

                        2 -> {
                            var height by remember { mutableIntStateOf(vm.sliderHeight()) }
                            var width by remember { mutableIntStateOf(vm.sliderWidth()) }
                            var bottomPadding by remember { mutableIntStateOf(vm.bottomPadding()) }

                            Row(
                                Modifier,
                                Arrangement.SpaceBetween,
                                Alignment.Bottom
                            ) {
                                Column{
                                    LabelForInt(key = "height", min = 100, value = height, max = 1000) {
                                        height = it; vm.saveSliderHeight(it)
                                    }
                                    LabelForInt(key = "width", min = 100, value = width, max = 1000) {
                                        width = it; vm.saveSliderWidth(it)
                                    }
                                    LabelForInt(key = "distance from bottom", min = 0, value = bottomPadding, max = 1000) {
                                        bottomPadding = it; vm.saveBottomPadding(it)
                                    }
                                }
                            }
                        }

                        3 -> {
                            var baseRad by remember { mutableIntStateOf(vm.appsBaseRadius()) }
                            var selectionRad by remember { mutableIntStateOf(vm.appsSelectionRadius()) }

                            val appsPositioning = vm.appsPositioning()
                            var appsPop by remember { mutableIntStateOf(vm.appsPop()) }
                            var firstRingRadius by remember { mutableFloatStateOf(appsPositioning.startingRadius.toFloat()) }
                            var radiusDiff by remember { mutableFloatStateOf(appsPositioning.radiusDiff.toFloat()) }
                            var iconsDiff by remember { mutableFloatStateOf(appsPositioning.iconDistance.toFloat()) }

                            Row(
                                Modifier,
                                Arrangement.SpaceBetween,
                                Alignment.Bottom
                            ) {
                                Column{
                                    LabelForInt(key = "base radius", min = 10, value = baseRad, max = 100) {
                                        baseRad = it; vm.saveAppsBaseRadius(it)
                                    }
                                    LabelForInt(key = "selection radius", min = 10, value = selectionRad, max = 100) {
                                        selectionRad = it; vm.saveAppsSelectionRadius(it)
                                    }
                                    LabelForInt(key = "apps pop", min = 10, value = appsPop, max = 100) {
                                        appsPop = it; vm.saveAppsPop(it)
                                    }
                                    LabelForFloat(key = "first ring radius", min = 50f, value = firstRingRadius, max = 500f) {
                                        firstRingRadius = it; vm.saveAppsPositioning(appsPositioning.copy(startingRadius = firstRingRadius.toDouble()))
                                    }
                                    LabelForFloat(key = "difference between rings", min = 25f, value = radiusDiff, max = 300f) {
                                        radiusDiff = it;  vm.saveAppsPositioning(appsPositioning.copy(radiusDiff = radiusDiff.toDouble()))
                                    }
                                    LabelForFloat(key = "distance between icons", min = 25f, value = iconsDiff, max = 300f) {
                                        iconsDiff = it;  vm.saveAppsPositioning(appsPositioning.copy(iconDistance = iconsDiff.toDouble()))
                                    }
                                }
                            }
                        }

                        4 -> {
                            var baseRad by remember { mutableIntStateOf(vm.groupsBaseRadius()) }
                            var basePop by remember { mutableIntStateOf(vm.groupsBasePop()) }
                            var selectionRad by remember { mutableIntStateOf(vm.groupsSelectionRadius()) }
                            var selectionPop by remember { mutableIntStateOf(vm.groupsSelectionPop()) }

                            Row(
                                Modifier,
                                Arrangement.SpaceBetween,
                                Alignment.Bottom
                            ) {
                                Column{
                                    LabelForInt(key = "base radius", min = 10, value = baseRad, max = 100) {
                                        baseRad = it; vm.saveGroupsBaseRadius(it)
                                    }
                                    LabelForInt(key = "selection radius", min = 10, value = selectionRad, max = 100) {
                                        selectionRad = it; vm.saveGroupsSelectionRadius(it)
                                    }
                                    LabelForInt(key = "base pop", min = 0, value = basePop, max = 200) {
                                        basePop = it; vm.saveGroupsBasePop(it)
                                    }
                                    LabelForInt(key = "selection pop", min = 0, value = selectionPop, max = 200) {
                                        selectionPop = it; vm.saveGroupsSelectionPop(it)
                                    }
                                    // todo : pick between different curves
                                }
                            }
                        }
                        else -> {
                            Text(text = "page not created")
                        }
                    }
                }
            )
        }
    }
}