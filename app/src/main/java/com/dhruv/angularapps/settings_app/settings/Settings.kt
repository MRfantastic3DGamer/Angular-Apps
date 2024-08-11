package com.dhruv.angularapps.settings_app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R
import com.dhruv.angularapps.settings_app.LabelForFloat
import com.dhruv.angularapps.settings_app.LabelForInt

const val TAG = "Settings"

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    vm: SettingsVM
) {
    @Composable
    fun card(pageIdx: Int, icon: Int, text: String, description: String) {

        val textStyle = TextStyle(
            fontWeight = FontWeight.W800,
            fontSize = TextUnit(25f, TextUnitType.Sp)
        )
        val descriptionStyle = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = TextUnit(15f, TextUnitType.Sp),
        )

        Card(
            onClick = { vm.openPopup(pageIdx) },
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                Modifier
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 16.dp)
                        .size(28.dp),
                    painter = painterResource(id = icon),
                    contentDescription = text,
                )
                Column {
                    Text(text = text, Modifier.padding(8.dp), style = textStyle)
                    Text(text = description, Modifier.padding(8.dp), style = descriptionStyle)
                }
            }
        }
    }

    @Composable
    fun touch() {
        Column {
            LabelForFloat(key = "X", min = -20f, value = vm.touchOffset.x, max = 20f) {
                vm.touchOffset = vm.touchOffset.copy(x = it)
            }
            LabelForFloat(key = "Y", min = -20f, value = vm.touchOffset.y, max = 20f) {
                vm.touchOffset = vm.touchOffset.copy(y = it)
            }
        }
    }

    @Composable
    fun slider() {

        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
                LabelForInt(key = "height", min = 100, value = vm.slHeight, max = 1000) {
                    vm.slHeight = it
                }
                LabelForInt(key = "width", min = 100, value = vm.slWidth, max = 1000) {
                    vm.slWidth = it
                }
                LabelForInt(
                    key = "distance from bottom",
                    min = 0,
                    value = vm.slBottomPadding,
                    max = 1000
                ) {
                    vm.slBottomPadding = it
                }
            }
        }
    }

    @Composable
    fun apps() {

        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
                LabelForInt(key = "base radius", min = 10, value = vm.appsBaseRad, max = 100) {
                    vm.appsBaseRad = it
                }
                LabelForInt(
                    key = "selection radius",
                    min = 10,
                    value = vm.appsSelectionRad,
                    max = 100
                ) {
                    vm.appsSelectionRad = it
                }
                LabelForInt(key = "apps pop", min = 10, value = vm.appsPop, max = 100) {
                    vm.appsPop = it
                }
                LabelForFloat(
                    key = "first ring radius",
                    min = 50f,
                    value = vm.firstRingRadius,
                    max = 500f
                ) {
                    vm.appsPositioning = vm.appsPositioning.copy(startingRadius = it.toDouble())
                }
                LabelForFloat(
                    key = "difference between rings",
                    min = 25f,
                    value = vm.radiusDiff,
                    max = 300f
                ) {
                    vm.appsPositioning = vm.appsPositioning.copy(radiusDiff = it.toDouble())
                }
                LabelForFloat(
                    key = "distance between icons",
                    min = 25f,
                    value = vm.iconsDiff,
                    max = 300f
                ) {
                    vm.appsPositioning = vm.appsPositioning.copy(iconDistance = it.toDouble())
                }
            }
        }
    }

    @Composable
    fun groups() {

        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
                LabelForInt(key = "base radius", min = 10, value = vm.groupBaseRad, max = 100) {
                    vm.groupBaseRad = it
                }
                LabelForInt(
                    key = "selection radius",
                    min = 10,
                    value = vm.groupSelectionRad,
                    max = 100
                ) {
                    vm.groupSelectionRad = it
                }
                LabelForInt(key = "base pop", min = 0, value = vm.groupBasePop, max = 200) {
                    vm.groupBasePop = it
                }
                LabelForInt(
                    key = "selection pop",
                    min = 0,
                    value = vm.groupSelectionPop,
                    max = 200
                ) {
                    vm.groupSelectionPop = it
                }
                // todo : pick between different curves
            }
        }
    }

    Box(modifier = modifier) {
        Column(
            Modifier
        ) {

            Spacer(modifier = Modifier.height(25.dp))
            card(
                1,
                R.drawable.round_pan_tool_alt_24,
                "adjust touch offset",
                "where the finger touches the screen and what is registered can be different"
            )
            card(
                pageIdx = 2,
                icon = R.drawable.round_swipe_vertical_24,
                text = "adjust slider dimensions",
                description = "the slider is the bar present on bottom right position"
            )
            card(
                pageIdx = 3,
                icon = R.drawable.round_apps_24,
                text = "adjust apps look",
                description = "customize how the apps look"
            )
            card(
                pageIdx = 4,
                icon = R.drawable.baseline_group_work_24,
                text = "adjust groups look",
                description = "customize how the groups on slider look"
            )
        }

        if (vm.popup != 0) {
            AlertDialog(
                onDismissRequest = { vm.popup = 0 },
                text = {
                    when (vm.popup) {
                        1 -> touch()
                        2 -> slider()
                        3 -> apps()
                        4 -> groups()
                        else -> {
                            Text(text = "page not created")
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { vm.confirm() }
                    ) { Text(text = "Confirm") }
                },
                dismissButton = {
                    Button(
                        onClick = { vm.dismiss() }
                    ) { Text(text = "Dismiss") }
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 16.dp)
                            .size(28.dp),
                        painter = painterResource(
                            id = when (vm.popup) {
                                1 -> R.drawable.round_pan_tool_alt_24
                                2 -> R.drawable.round_swipe_vertical_24
                                3 -> R.drawable.round_apps_24
                                4 -> R.drawable.baseline_group_work_24
                                else -> R.drawable.round_report_gmailerrorred_24
                            }
                        ),
                        contentDescription = "popup : ${vm.popup}",
                    )
                    when (vm.popup) {
                        1 -> {}
                        2 -> {}
                        3 -> {}
                        4 -> {}
                        else -> {}
                    }
                }
            )
        }
    }
}