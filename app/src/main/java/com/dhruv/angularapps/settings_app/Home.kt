package com.dhruv.angularapps.settings_app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R

@Composable
fun Home(
    modifier: Modifier = Modifier,
    haveOverlayPermission:Boolean,
    enableOverlayPermission:()->Unit,
    startOverlayService: ()->Unit,
    stopOverlayService: ()->Unit,
    isOverlayServiceRunning: ()->Boolean,
) {

    var isOverlayRunning by remember { mutableStateOf(isOverlayServiceRunning()) }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "start the service to get quick access to your apps.")

        if (!haveOverlayPermission) {
            Text(text = "You have not given the overlay permission to use the app")
            Button(onClick = enableOverlayPermission) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    Text(text = "Give permission to draw over other apps")
                    Icon(
                        painter = painterResource(R.drawable.round_arrow_outward_24),
                        contentDescription = "out_link"
                    )
                }
            }
        }

        when (isOverlayRunning) {
            true -> Button(
                onClick = {
                    stopOverlayService()
                    isOverlayRunning = isOverlayServiceRunning()
                },
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "Remove from home",
                    fontSize = TextUnit(6f, TextUnitType.Em),
                    fontWeight = FontWeight.W700
                )
            }
            false -> Button(
                onClick = {
                    startOverlayService()
                    isOverlayRunning = isOverlayServiceRunning()
                },
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "Set at home",
                    fontSize = TextUnit(8f, TextUnitType.Em),
                    fontWeight = FontWeight.W900
                )
            }
        }
    }
}