package com.dhruv.angularapps.settings_app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun H1 (text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier.padding( vertical = 4.dp, horizontal = 8.dp), fontSize = TextUnit(25f, TextUnitType.Sp), fontWeight = FontWeight.W800)
}

@Composable
fun H2 (text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier.padding( vertical = 2.dp, horizontal = 8.dp), fontSize = TextUnit(18f, TextUnitType.Sp), fontWeight = FontWeight.W600)
}

@Composable
fun H3 (text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier.padding( vertical = 1.dp, horizontal = 8.dp), fontSize = TextUnit(16f, TextUnitType.Sp), fontWeight = FontWeight.W400)
}

@Composable
fun H4 (text: String, modifier: Modifier = Modifier){
    Text(text = text, modifier.padding( vertical = 1.dp, horizontal = 8.dp), fontSize = TextUnit(12f, TextUnitType.Sp), fontWeight = FontWeight.W200)
}

@Composable
fun FieldContainer(modifier: Modifier = Modifier, content: @Composable (scope: ColumnScope) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Black)
    ) {
        content(this)
    }
}

@Composable
fun Collapsable(
    text: @Composable () -> Unit,
    canOpen: Boolean = true,
    initialState: Boolean = false,
    description: String? = null,
    content: @Composable () -> Unit
) {
    var opened by remember { mutableStateOf(initialState) }

    FieldContainer () {
        Column {
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { opened = !opened },
                Arrangement.SpaceBetween,
                Alignment.CenterVertically,
            ) {
                text()
                Icon(
                    imageVector = if (opened) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "openDirection",
                    Modifier,
                    tint = Color.White
                )
            }
            description?.let { Description(text = it) }
        }

        AnimatedVisibility(
            visible = opened && canOpen,
            enter = expandIn(expandFrom = Alignment.TopCenter),
            exit = shrinkOut(shrinkTowards = Alignment.TopCenter),
        ) {
            Divider(Modifier)
            content()
        }

    }
}

@Composable
fun Description(text: String) {
    FieldContainer() {
        Text(
            text = text,
            style = TextStyle(
                color = Color(0.8f, 0.8f, 0.9f),
                fontSize = TextUnit(14f, TextUnitType.Sp),

                )
        )
    }
}

@Composable
fun EditableText(
    text: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by rememberSaveable { mutableStateOf(false) }

    if (isEditing) {
        BasicTextField(
            value = text,
            onValueChange = { onUpdate(it) },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = TextUnit(16f, TextUnitType.Sp)
            ),
            modifier = modifier
                .background(Color.Black, RoundedCornerShape(8.dp)),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    isEditing = false
                }
            )
        )
    } else {
        H3(
            text = text,
            modifier = modifier
                .clickable { isEditing = true }
        )
    }
}

@Composable
fun LabelForInt(
    key : String,
    min: Int,
    value : Int,
    max: Int,
    description: String? = null,
    update: (Int)->Unit
) {
    LabelForFloat(
        key = key,
        min = min.toFloat(),
        value = value.toFloat(),
        max = max.toFloat(),
        description=description
    ) {
        update(it.roundToInt())
    }
}

@Composable
fun LabelForFloat(
    key : String,
    min: Float,
    value : Float,
    max: Float,
    description: String? = null,
    update: (Float)->Unit,
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
        ) {
            H3(text = "$key:")
            EditableText("${(value * 100.0).roundToInt() / 100.0}", onUpdate = { it.toFloatOrNull()?.let { update(it) } })
        }
        description?.let { Box ( Modifier.padding(horizontal = 8.dp) ) { Description(text = it) } }
        Slider(
            value = value,
            onValueChange = update,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            valueRange = min..max,
        )
    }
}

@Composable
fun RgbaTextFields(
    color: Color,
    onColorChange: (Color) -> Unit
) {
    var red by remember { mutableStateOf((color.red * 255).toInt().toString()) }
    var green by remember { mutableStateOf((color.green * 255).toInt().toString()) }
    var blue by remember { mutableStateOf((color.blue * 255).toInt().toString()) }
    var alpha by remember { mutableStateOf((color.alpha * 255).toInt().toString()) }

    val updateColor = {
        val r = red.toIntOrNull()?.coerceIn(0, 255) ?: 0
        val g = green.toIntOrNull()?.coerceIn(0, 255) ?: 0
        val b = blue.toIntOrNull()?.coerceIn(0, 255) ?: 0
        val a = alpha.toIntOrNull()?.coerceIn(0, 255) ?: 0
        onColorChange(Color(r / 255f, g / 255f, b / 255f, a / 255f))
    }

    fun validateAndSet(value: String, setter: (Int) -> Unit) {
        val intValue = value.toIntOrNull()?.coerceIn(0, 125)
        if (intValue != null) {
            setter(intValue)
            onColorChange(Color(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt()))
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        val textFieldModifier = Modifier
            .width(60.dp)

        TextField(
            value = red,
            onValueChange = { validateAndSet(it) { red = it.toString() } },
            modifier = textFieldModifier,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = green.toString(),
            onValueChange = { validateAndSet(it) { green = it.toString() } },
            modifier = textFieldModifier,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = blue.toString(),
            onValueChange = { validateAndSet(it) { blue = it.toString() } },
            modifier = textFieldModifier,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = alpha.toString(),
            onValueChange = { validateAndSet(it) { alpha = it.toString() } },
            modifier = textFieldModifier,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}


@Composable
fun LabelForBool(key: String, value: Boolean, description: String?, onUpdate: (Boolean) -> Unit) {
    Collapsable(text = {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black),
            Arrangement.SpaceBetween
        ){
            H3(text = key)
            Checkbox(checked = value, onCheckedChange = onUpdate)
        }
    }) {
        description?.let { Description(text = it) }
    }
}