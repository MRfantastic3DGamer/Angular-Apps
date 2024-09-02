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
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class AppsAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var offset: Offset = Offset(0f, 0f)
    private var selectionPos: Float = 0f
    private var minRad: Float = 0f
    private var maxRad: Float = 0f
    private var selectionPop: Float = 0f
    private var vertexCount: Int = 50
    private var bottomCutY: Float = 2000f
    private var topCutY: Float = 100f


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
        minRad: Float, maxRad: Float,
        selectionPop: Float,
        vertexCount: Int,
        bottomCutY: Float,
        topCutY: Float
    ) {
        this.offset = offset
        this.selectionPos = selectionPos
        this.minRad = minRad
        this.maxRad = maxRad
        this.selectionPop = selectionPop
        this.vertexCount = vertexCount
        this.bottomCutY = bottomCutY
        this.topCutY = topCutY

        invalidate() // Invalidate the view to trigger a redraw
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val path = areaPath(
            offset = offset,
            selectionPos = selectionPos,
            minRad = minRad,
            maxRad = maxRad,
            selectionPop = selectionPop,
            bottomCutY = bottomCutY,
            topCutY = topCutY
        )
//        canvas.clipPath(path)
        canvas.drawPath(path, nullPainterPaint)
        canvas.drawPath(path, strokePaint)
//        canvas.drawRect(0f,0f,10000f,10000f, nullPainterPaint)
    }
}

/**
 * Function to calculate the angle between the line from the center of the circle
 * to the left intersection point and the y-axis.
 *
 * @param radius The radius of the circle.
 * @param lineYOffset The vertical offset of the line from the center of the circle.
 * @return The angle in degrees between the line and the y-axis.
 */
fun calculateAngleWithYAxis(radius: Double, lineYOffset: Double): Double? {
    // Check if the line intersects the circle
    if (lineYOffset.absoluteValue > radius) {
        return null
    }

    // Calculate x-coordinate of the left intersection point
    val x = -sqrt(radius * radius - lineYOffset * lineYOffset)

    // Calculate the angle in radians between the new line and the y-axis
    val angleInRadians = atan2(x, lineYOffset)

    // Convert angle from radians to degrees
    return Math.toDegrees(angleInRadians)
}

private fun areaPath(
    offset: Offset,
    selectionPos: Float,

    minRad: Float,
    maxRad: Float,
    selectionPop: Float,
    bottomCutY: Float,
    topCutY: Float,
): Path {

    val centerOffset = offset + Offset(-selectionPop, selectionPos)
    val triggerOpenPath = Path()

    val pushR = 30
    triggerOpenPath.moveTo(centerOffset.x + pushR,centerOffset.y + minRad)
    triggerOpenPath.lineTo(centerOffset.x,centerOffset.y + minRad)

    val topLeftS = centerOffset + Offset( -minRad, -minRad)
    val bottomRightS = centerOffset + Offset(minRad, minRad)

    // selection arc
    triggerOpenPath.addArc(
        RectF(topLeftS.x, topLeftS.y, bottomRightS.x, bottomRightS.y),
        90f,
        180f,
    )
    triggerOpenPath.lineTo(centerOffset.x + pushR,centerOffset.y - minRad)

    val topLeftB = centerOffset + Offset(-maxRad, -maxRad)
    val bottomRightB = centerOffset + Offset(maxRad, maxRad)

    val angleT = (calculateAngleWithYAxis(maxRad.toDouble(), (centerOffset.y - topCutY).toDouble())?.toFloat() ?: 0f) - 90f
    val angleB = (calculateAngleWithYAxis(maxRad.toDouble(), (centerOffset.y - bottomCutY).toDouble())?.toFloat() ?: -180f) - 90f
    val sweepAngle = angleB - angleT

    val botLimit = min(bottomCutY, bottomRightB.y)
    val topLimit = max(topCutY, topLeftB.y)

    triggerOpenPath.lineTo(centerOffset.x + pushR,topLimit)
    triggerOpenPath.lineTo(centerOffset.x - sin(((angleT - 90) * PI / 180)).toFloat() * maxRad, topLimit)

    // selection arc
    triggerOpenPath.addArc(
        RectF(topLeftB.x, topLeftB.y, bottomRightB.x, bottomRightB.y),
        angleT,
        sweepAngle,
    )
    triggerOpenPath.lineTo(centerOffset.x + pushR, botLimit)
    triggerOpenPath.lineTo(centerOffset.x + pushR,centerOffset.y + minRad)
    triggerOpenPath.lineTo(centerOffset.x,centerOffset.y + minRad)

    return triggerOpenPath
}