package com.dhruv.angularapps.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class SliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var offset: Offset = Offset(0f, 0f)
    private var selectionPos: Float = 0f
    private var width: Float = 0f
    private var height: Float = 0f
    private var radius: Float = 20f
    private var selectionPop: Float = 0f
    private var vertexCount: Int = 20


    private val nullPainterPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    fun updateVisuals(
        offset: Offset,
        selectionPos: Float,
        width: Float, height: Float,
        radius: Float,
        selectionPop: Float,
        vertexCount: Int
    ) {
        this.offset = offset
        this.selectionPos = selectionPos
        this.width = width
        this.height = height
        this.radius = radius
        this.selectionPop = selectionPop
        this.vertexCount = vertexCount

        invalidate() // Invalidate the view to trigger a redraw
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = SliderPath(
            offset = offset,
            selectionPos = selectionPos,
            width = width,
            height = height,
            radius = radius,
            selectionPop = selectionPop,
            vertexCount = vertexCount
        )
//        canvas.clipPath(path)
        canvas.drawPath(path, nullPainterPaint)
        canvas.drawPath(path, strokePaint)
//        canvas.drawRect(0f,0f,10000f,10000f, nullPainterPaint)
    }
}


private fun SliderPath(
    offset: Offset,
    selectionPos: Float,

    width: Float, height: Float,
    radius: Float,
    selectionPop: Float,

    vertexCount: Int
): Path {

    val triggerOffset = offset
    val triggerSize = Size(width, height)

    val maxAngle = 180f

    // region trigger arc

    val triggerArcTop = mutableListOf<Offset>()
    val triggerArcBot = mutableListOf<Offset>()
    val triggerArcRadius = triggerSize.width / 2
    val deltaAngle_trigg = (maxAngle / vertexCount) * (Math.PI / 180).toFloat()
    val triggerTopArcMid = triggerOffset + Offset(triggerArcRadius, min(0f, selectionPos - radius + triggerArcRadius))
    val triggerDownArcMid = triggerOffset + Offset(triggerArcRadius, max(height, selectionPos + radius - triggerArcRadius))

    repeat(vertexCount) {
        val angle = deltaAngle_trigg * it
        triggerArcTop.add(triggerTopArcMid + Offset(-(cos(angle) * triggerArcRadius), -(sin(angle) * triggerArcRadius)))
        triggerArcBot.add(triggerDownArcMid + Offset((cos(angle) * triggerArcRadius), (sin(angle) * triggerArcRadius)))
    }

    // endregion

    val triggerOpenPath = Path()

    triggerOpenPath.moveTo(offset.x - selectionPop, offset.y + selectionPos - radius)

    val topLeft = triggerOffset + Offset(-selectionPop - radius, selectionPos - radius)
    val bottomRight = triggerOffset + Offset(-selectionPop + radius, selectionPos + radius)

    // selection arc
    triggerOpenPath.addArc(
        RectF(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y),
        90f,
        180f,
    )

    val minY = offset.y + selectionPos - radius
    val maxY = offset.y + selectionPos + radius

    triggerOpenPath.lineTo( if (triggerArcTop.size > 0) triggerArcTop.first().x else 0f, triggerOffset.y + selectionPos - radius)

    // top arc
    for (i in 0 until triggerArcTop.size / 2)
        triggerOpenPath.lineTo(triggerArcTop[i].x, min(minY, triggerArcTop[i].y))
    for (i in triggerArcTop.size / 2 + 1 until triggerArcTop.size)
        triggerOpenPath.lineTo(triggerArcTop[i].x, triggerArcTop[i].y)

    // down arc
    for (i in 0 until triggerArcBot.size / 2)
        triggerOpenPath.lineTo(triggerArcBot[i].x, triggerArcBot[i].y)
    for (i in triggerArcBot.size / 2 + 1 until triggerArcBot.size)
        triggerOpenPath.lineTo(triggerArcBot[i].x, max(maxY, triggerArcBot[i].y))

    triggerOpenPath.lineTo(triggerArcTop.first().x, triggerOffset.y + selectionPos + radius)

    triggerOpenPath.close()
    return triggerOpenPath
}