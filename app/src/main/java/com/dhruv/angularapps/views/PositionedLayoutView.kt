package com.dhruv.angularapps.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter

data class ItemValues(
    val radius: Float,
    val offset: Offset,
    val key: String,
)

class PositionedLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    fun updateValues(size: Int, getValues: (Int) -> ItemValues) {
        val kys = mutableListOf<String>()
        val off = mutableListOf<Offset>()
        val rad = mutableListOf<Float>()

        for (i in 0 until size) {
            val values = getValues(i)
            kys.add(values.key)
            off.add(values.offset)
            rad.add(values.radius)
        }

        keys = kys
        offsets = off
        radiuses = rad
        invalidate()
    }

    fun updateVisuals(keys: List<String>, forceReset: Boolean = false, getVisuals: (String) -> Pair<Drawable?, Painter?>){
        keys.forEach {
            val res = getVisuals(it)
            if (res.first != null){
                if ((drawables.containsKey(it) && forceReset) or !drawables.containsKey(it)){
                    drawables[it] = res.first!!
                }
            }
            if (res.second != null){
                if ((painters.containsKey(it) && forceReset) or !painters.containsKey(it)){
                    painters[it] = res.second!!
                }
            }
        }
    }

    private var keys: List<String> = emptyList()
    private var offsets: List<Offset> = emptyList()
    private var radiuses: List<Float> = emptyList()
    private var drawables = mutableMapOf<String, Drawable>()
    private var painters = mutableMapOf<String, Painter>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in offsets.indices) {
            val offset = offsets[i]
            val radius = radiuses[i]
//            Log.d(TAG, "onDraw: available drawables : ${drawables.keys}")
//            Log.d(TAG, "onDraw: available painters : ${painters.keys}")
            val drawable = drawables[keys[i]]
            if (drawable == null) {
                canvas.drawCircle(offset.x, offset.y, radius, paint)
            } else {
                // Calculate the bounds for the Drawable
                val left = (offset.x - radius).toInt()
                val top = (offset.y - radius).toInt()
                val right = (offset.x + radius).toInt()
                val bottom = (offset.y + radius).toInt()

                // Set the bounds for the Drawable
                drawable.setBounds(left, top, right, bottom)

                // Draw the Drawable on the Canvas
                drawable.draw(canvas)
            }
        }
    }

    companion object {
        const val TAG = "CircleView"
    }
}