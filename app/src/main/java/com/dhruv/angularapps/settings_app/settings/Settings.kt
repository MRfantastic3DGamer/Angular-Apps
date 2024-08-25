package com.dhruv.angularapps.settings_app.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R
import com.dhruv.angularapps.settings_app.LabelForFloat
import com.dhruv.angularapps.settings_app.LabelForInt
import com.dhruv.angularapps.settings_app.RgbaTextFields

const val TAG = "Settings"

@Stable
data class SettingsCard(
    val idx : Float,
    val icon : Int,
    val text: String,
    val description: String,
)

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    vm: SettingsVM
) {

    @Composable
    fun groupedCards(vararg cards: SettingsCard, title: String, description: String, icon: Int){

        val textStyle = MaterialTheme.typography.headlineMedium.copy(
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        val descriptionStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.secondary
        )

        Card(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column {
                Row(Modifier.padding(8.dp)) {
                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(50.dp),
                        painter = painterResource(id = icon),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = Color.White)
                cards.forEachIndexed { it, card ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { vm.openPopup(card.idx) },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp),
                            painter = painterResource(id = card.icon),
                            contentDescription = card.text,
//                            tint = Color.White
                        )
                        Column {
                            Text(text = card.text, Modifier.padding(top = 8.dp), style = textStyle)
                            Text(text = card.description, Modifier.padding(bottom = 8.dp), style = descriptionStyle)
                        }
                    }

                    if (it < cards.lastIndex)
                        HorizontalDivider(thickness = 1.dp, color = Color.Black)
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
    fun sliderSize() {

        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
                LabelForInt(key = "height", min = 100, value = vm.slHeight, max = 1000) {
                    vm.slHeight = it
                }
                LabelForInt(key = "width", min = 25, value = vm.slWidth, max = 150) {
                    vm.slWidth = it
                }
            }
        }
    }

    @Composable
    fun sliderOffset() {
        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
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
    fun sliderLook() {
        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
                Text(text = "Color")
                RgbaTextFields(vm.tColor){
                    vm.tColor = it
                }
            }
        }
    }

    @Composable
    fun appsPositioning() {

        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
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
    fun appsLook() {

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
            }
        }
    }

    @Composable
    fun groupsPositioning() {
        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
                LabelForInt(key = "base pop", min = 0, value = vm.groupBasePop, max = 200) {
                    vm.groupBasePop = it
                }
                LabelForInt(key = "selection pop", min = 0, value = vm.groupSelectionPop, max = 200) {
                    vm.groupSelectionPop = it
                }
            }
        }
    }

    @Composable
    fun groupsLook() {
        Row(
            Modifier,
            Arrangement.SpaceBetween,
            Alignment.Bottom
        ) {
            Column {
                LabelForInt(key = "base radius", min = 10, value = vm.groupBaseRad, max = 100) {
                    vm.groupBaseRad = it
                }
                LabelForInt(key = "selection radius", min = 10, value = vm.groupSelectionRad, max = 100) {
                    vm.groupSelectionRad = it
                }
            }
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            Modifier
        ) {

            item {
                Spacer(modifier = Modifier.height(25.dp))
            }
            item {
                groupedCards(
                    SettingsCard(
                        1.1f,
                        R.drawable.round_pan_tool_alt_24,
                        "adjust touch offset",
                        "where the finger touches the screen and what is registered can be different"
                    ),
                    title = "Interaction",
                    description = "change how you interact with the app",
                    icon = R.drawable.round_pan_tool_alt_24,
                )
            }
            item {
                groupedCards(
                    SettingsCard(
                        2.1f,
                        icon = R.drawable.round_open_in_full_24,
                        text = "Size",
                        description = "the size of the slider"
                    ),
                    SettingsCard(
                        2.2f,
                        icon = R.drawable.round_location_searching_24,
                        text = "Positioning",
                        description = "starting point of interaction"
                    ),
//                    SettingsCard(
//                        2.3f,
//                        icon = R.drawable.round_looks_24,
//                        text = "Look",
//                        description = "Trigger is that thing that will always be there on the bottom right corner from where you can start your interaction."
//                    ),
                    title = "adjust slider",
                    description = "the slider is the bar present on bottom right position",
                    icon = R.drawable.round_swipe_vertical_24
                )
            }

            item {
                groupedCards(
                    SettingsCard(
                        3.1f,
                        icon = R.drawable.round_location_searching_24,
                        text = "Positioning",
                        description = "customize how the apps look"
                    ),
                    SettingsCard(
                        3.2f,
                        icon = R.drawable.round_looks_24,
                        text = "Look",
                        description = "customize how the apps look"
                    ),
                    title = "adjust apps",
                    description = "the apps of the selected group",
                    icon = R.drawable.round_apps_24
                )
            }

            item {
                groupedCards(
                    SettingsCard(
                        4.1f,
                        icon = R.drawable.round_location_searching_24,
                        text = "Positioning",
                        description = "where they appear"
                    ),
                    SettingsCard(
                        4.2f,
                        icon = R.drawable.round_looks_24,
                        text = "Look",
                        description = "how they appear"
                    ),
                    title = "adjust groups",
                    description = "customize how the groups on slider look",
                    icon = R.drawable.baseline_group_work_24,
                )
            }

            item{ Spacer(modifier = Modifier.height(500.dp)) }
        }

        if (vm.popup != 0f) {
            AlertDialog(
                onDismissRequest = { vm.popup = 0f },
                text = {
                    when (vm.popup) {
                        1.1f -> touch()
                        2.1f -> sliderSize()
                        2.2f -> sliderOffset()
                        2.3f -> sliderLook()
                        3.1f -> appsPositioning()
                        3.2f -> appsLook()
                        4.1f -> groupsPositioning()
                        4.2f -> groupsLook()
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
                                1.1f -> R.drawable.round_pan_tool_alt_24
                                2.1f ,2.2f, 2.3f -> R.drawable.round_swipe_vertical_24
                                3.1f ,3.2f -> R.drawable.round_apps_24
                                4.1f ,4.2f -> R.drawable.baseline_group_work_24
                                else -> R.drawable.round_report_gmailerrorred_24
                            }
                        ),
                        contentDescription = "popup : ${vm.popup}",
                    )
                }
            )
        }
    }
}