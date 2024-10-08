package com.dhruv.angularapps.settings_app.groups

import android.util.Log
import android.widget.ProgressBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.R
import com.dhruv.angularapps.data.models.Group
import com.dhruv.angularapps.settings_app.H2
import com.dhruv.angularapps.utils.rememberDrawablePainter

val TAG = "Groups Editor"

@Composable
fun GroupsEditor (
    modifier: Modifier = Modifier,
    vm: GroupsEditorVM,
) {
    val groups = vm.groups.collectAsState(initial = emptyList()).value
    val context = LocalContext.current

    @Composable
    fun GroupIconChoices() {
        Card(
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.background,
            )
        ) {
            LazyVerticalGrid (
                columns = GridCells.Adaptive( minSize = 48.dp ),
                Modifier
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                items(
                    GroupIcons.keys.toList(),
                    key = { it }
                ){ icon ->
                    Image(
                        painter = painterResource(id = GroupIcons[icon]!!), contentDescription = icon,
                        Modifier
                            .size(48.dp)
                            .padding(2.dp)
                            .clickable { vm.keyValue = icon; vm.showGroupIconChoices = false },
                        Alignment.Center,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
                    )
                }
            }
        }
    }

    @Composable
    fun Group(data: Group, select: () -> Unit) {
        Card(
            onClick = select,
            modifier = Modifier
                .padding(5.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.background,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val painter: Painter = if (GroupIcons.containsKey(data.key)) {
                    painterResource(id = GroupIcons[data.key]!!)
                } else {
                    painterResource(id = R.drawable.round_report_gmailerrorred_24)
                }

                // Applying the tint using the LocalContentColor
                Image(
                    painter = painter,
                    contentDescription = data.key,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(48.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                )

                H2(text = data.name)
            }
        }
    }


    @Composable
    fun App(app: String) {
        val checked = vm.isAppSelected(app)
        val name = vm.apps[app]
        val drawable = vm.appsIcons[app]

        println("drawn app $name")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(if (checked) Color.White else Color.Black)
                .clickable {
                    if (checked) {
                        vm.removeApp(app)
                    } else {
                        vm.addApp(app)
                    }
                }
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                painter = if (drawable != null) rememberDrawablePainter(drawable = drawable) else painterResource(id = R.drawable.round_report_gmailerrorred_24),
                contentDescription = app,
            )

            Text(
                text = name ?: "",
                modifier = Modifier.padding(4.dp),
                color = if (checked) Color.Black else Color.White
            )
        }
    }


    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        LazyColumn {
            items(groups.size) { group ->
                Group(groups[group]) { vm.selectGroup(groups[group], group) }
            }
            item {
                TextButton(
                    onClick = {
                        Log.d(TAG, "Added new")
                        vm.addNewGroup()
                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(Modifier.align(Alignment.CenterVertically)) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                        Text(
                            text = "add new group",
                            Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(500.dp)) }
        }
    }

    if (vm.showGroupEditingDialog) {
        AlertDialog(
            modifier = Modifier,
            onDismissRequest = { vm.dismiss() },
            title = { Text("Edit Group") },
            text = {
                Column(
                    Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        Modifier,
                        Arrangement.Start,
                        Alignment.CenterVertically,
                    ) {

                        IconButton(
                            onClick = { vm.showGroupIconChoices = !vm.showGroupIconChoices },
                        ) {
                            val image = painterResource(id = GroupIcons.getOrDefault(vm.keyValue, R.drawable.round_report_gmailerrorred_24))
                            Image(
                                painter = image,
                                contentDescription = "groups_icon",
                                Modifier
                                    .size(60.dp),
                                Alignment.Center,
                                ContentScale.Fit,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextField(
                            value = vm.nameValue,
                            onValueChange = { vm.nameValue = it },
                            label = { Text("Enter group name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    AnimatedVisibility(visible = vm.showGroupIconChoices) {
                        GroupIconChoices()
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Select apps to add to group:")
                    Spacer(modifier = Modifier.height(8.dp))
                    if (vm.apps.isEmpty()) {
                        ProgressBar(context)
                    } else {
                        val apps = vm.apps.keys.sortedBy { vm.apps[it] }
                        LazyColumn {
                            items(
                                apps,
                                key = {it}
                            ) { app ->
                                if (vm.apps.containsKey(app) && vm.appsIcons.containsKey(app)) {
                                    App(app)
                                }
                            }
                            item { Spacer(modifier = Modifier.height(100.dp)) }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { vm.confirm() }
                ) { Text(text = "Save") }
            },
            dismissButton = {
                Button(
                    onClick = { vm.delete() }
                ) { Text(text = "Delete") }
            },
        )
    }

    if (vm.errorPop){
        AlertDialog(
            onDismissRequest = { vm.closeErrorPopup() },
            confirmButton = {  },
            text = { Text(
                text = vm.errorMessage,
                style = TextStyle(
                    color = Color(1.0f, 0.498f, 0.498f, 1.0f),
                    fontWeight = FontWeight.W700,
                    fontSize = TextUnit(15f, TextUnitType.Sp)
                )
            ) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.round_report_gmailerrorred_24),
                    contentDescription = "error",
                    tint = Color.Red,
                    modifier = Modifier.size(25.dp)
                )
            }
        )
    }
}