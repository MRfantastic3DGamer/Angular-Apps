package com.dhruv.angularapps.settings_app.groups.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.angularapps.data.models.Group
import kotlin.math.ceil
import kotlin.math.max

class GroupShape(val height: Dp, val width: Dp, val topCut: Float, val cornerRadius: Dp): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {

        val cornerRadius = with(density) { cornerRadius.toPx() }
        val height = with(density) { height.toPx() }
        val width = with(density) { width.toPx() }

        val path = Path().apply {
            // Start at the top-left corner, accounting for the corner radius
            moveTo(cornerRadius, 0f)

            // Top edge
            lineTo(width*topCut - 2 * cornerRadius, 0f)

            addRoundRect(
                RoundRect(
                    left = 0f, top = 0f, right = width*topCut, bottom = height,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            )

            addRoundRect(
                RoundRect(
                    left = 0f, top = cornerRadius * 2, right = width, bottom = height,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            )

            val path = Path()
            path.apply {
                moveTo(width*topCut, cornerRadius)
                arcTo(Rect(left = width*topCut, top = 0f, right = width*topCut + cornerRadius*2, bottom = cornerRadius*2),
                    startAngleDegrees = -180f,
                    sweepAngleDegrees = -90f,
                    forceMoveTo = true
                )
                lineTo(width*topCut + cornerRadius, cornerRadius * 2)
                lineTo(width*topCut, cornerRadius * 2)
                close()
            }
            addPath(path)
        }
        return Outline.Generic(path)
    }
}

@Composable
fun Group(
    group: Group,
    width: Dp
) {
    val height = 200.dp
    val topCut = 0.6f
    val shape = GroupShape(
        height = height,
        width = width,
        topCut = topCut,
        cornerRadius = 30.dp
    )
    Shadowed(
        Modifier
            .padding(8.dp),
        offsetX = 0.dp,
        offsetY = 3.dp,
        blurRadius = 6.dp,
        color = Color.Black,
    ) {
        Column(
            modifier = Modifier
                .size(height = height, width = width)
                .background(
                    Color(0xFFFFE539),
                    shape = shape
                ),
            Arrangement.SpaceBetween,
            Alignment.Start
        ) {
            Row(
                Modifier
                    .padding(5.dp)
                    .height(70.dp),
                Arrangement.Start,
                Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .padding(5.dp)
                        .background(Color.White, CircleShape)
                )
                Text(
                    text = group.name,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        fontWeight = FontWeight.W900
                    )
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "edit", Modifier.size(15.dp))
                }
            }

            // Give some weight to the GroupAppsLayout so it doesn't take up all the space
            GroupAppsLayout(
                Modifier
                    .padding(start = 5.dp)
                    .height(55.dp)
            ) {
                repeat(15) {
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .shadow(3.dp, shape = CircleShape)
                            .background(Color.White, CircleShape)
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                Arrangement.End,
                Alignment.CenterVertically
            ) {
                Row(Modifier.weight(topCut)){}
                Spacer(modifier = Modifier.weight(0.1f))
                Row(
                    Modifier
                        .weight(1-topCut)
                        .height(40.dp)
                        .shadow(2.dp, RoundedCornerShape(15.dp))
                        .background(Color.White, RoundedCornerShape(150.dp)),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ){
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "up")
                    }
                    VerticalDivider()
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = "down")
                    }
                    VerticalDivider()
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun GroupsLayout(
    modifier: Modifier = Modifier,
    groups: List<Group>,
) {
    val configuration = LocalConfiguration.current
    Layout(
        {
            groups.forEach {
                Group(group = it, width = (configuration.screenWidthDp - 40).dp)
            }
        },
        modifier
    ){ measurable, constrainsts ->
        val placeables = measurable.map {
            it.measure(constrainsts)
        }
        var y = 0
        layout(constrainsts.maxWidth, constrainsts.maxHeight) {
            placeables.forEach { placeable ->
                placeable.place(position = IntOffset(0, y))
                y += placeable.height - 150
            }
        }
    }
}

@Composable
fun GroupAppsLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        content,
        modifier
    ) { measurable, constraints ->
        val placeabls = measurable.map {
            it.measure(constraints)
        }
        var saveX = 40
        var x = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeabls.forEachIndexed { i, placeable ->
                placeable.place(position = IntOffset(x, 0))
                if (i == 0){
                    saveX = max(
                        (placeable.width * (placeabls.size+1) - constraints.maxWidth) / placeabls.size,
                        saveX
                    )
                }
                x += placeable.width - saveX
            }
        }
    }
}

@Preview
@Composable
private fun GroupsPrev() {
    GroupsLayout(
        modifier = Modifier,
        groups = List(8) { Group(key = "home", name = "G name", apps = emptyList()) }
    )
}

@Composable fun Shadowed(modifier: Modifier, color: Color, offsetX: Dp, offsetY: Dp, blurRadius: Dp, content: @Composable () -> Unit) {
    val density = LocalDensity.current
    val offsetXPx = with(density) { offsetX.toPx() }.toInt()
    val offsetYPx = with(density) { offsetY.toPx() }.toInt()
    val blurRadiusPx = ceil(with(density) {
        blurRadius.toPx()
    }).toInt()

    // Modifier to render the content in the shadow color, then
    // blur it by blurRadius
    val shadowModifier = Modifier
        .drawWithContent {
            val matrix = shadowColorMatrix(color)
            val paint = Paint().apply {
                colorFilter = ColorFilter.colorMatrix(matrix)
            }
            drawIntoCanvas { canvas ->
                canvas.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
                drawContent()
                canvas.restore()
            }
        }
        .blur(radius = blurRadius, BlurredEdgeTreatment.Unbounded)
        .padding(all = blurRadius) // Pad to prevent clipping blur

    // Layout based solely on the content, placing shadow behind it
    Layout(modifier = modifier, content = {
        // measurables[0] = content, measurables[1] = shadow
        content()
        Box(modifier = shadowModifier) { content() }
    }) { measurables, constraints ->
        // Allow shadow to go beyond bounds without affecting layout
        val contentPlaceable = measurables[0].measure(constraints)
        val shadowPlaceable = measurables[1].measure(Constraints(maxWidth = contentPlaceable.width + blurRadiusPx * 2, maxHeight = contentPlaceable.height + blurRadiusPx * 2))
        layout(width = contentPlaceable.width, height = contentPlaceable.height) {
            shadowPlaceable.placeRelative(x = offsetXPx - blurRadiusPx, y = offsetYPx - blurRadiusPx)
            contentPlaceable.placeRelative(x = 0, y = 0)
        }
    }
}

// Return a color matrix with which to paint our content
// as a shadow of the given color
private fun shadowColorMatrix(color: Color): ColorMatrix {
    return ColorMatrix().apply {
        set(0, 0, 0f) // Do not preserve original R
        set(1, 1, 0f) // Do not preserve original G
        set(2, 2, 0f) // Do not preserve original B

        set(0, 4, color.red * 255) // Use given color's R
        set(1, 4, color.green * 255) // Use given color's G
        set(2, 4, color.blue * 255) // Use given color's B
        set(3, 3, color.alpha) // Multiply by given color's alpha
    }
}