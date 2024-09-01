package com.dhruv.angularapps.apps

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class AppsIconsPositioning {
    @Stable
    data class IconCoordinate(
        val distance: Double,
        val angle: Double
    )
    @Stable
    data class IconOffsetComputeResult(
        val qualityIndex: Int,
        val offsets: List<Offset>,
        val skips: List<Pair<Int, Int>>,
    )
    @Stable
    data class AppSelectionResult(
        val selectedApp : Int,
        val selectionValues: Map<Int, Float>,
    )

    data class IconCoordinatesGenerationScheme(
        val startingRadius:Double = 250.0,
        val radiusDiff:Double = 150.0,
        val iconDistance:Double = 150.0,
    ){

        companion object {
            fun fromString(string: String): IconCoordinatesGenerationScheme {
                val parts = string.split(",")
                return IconCoordinatesGenerationScheme(
                    parts[0].toDouble(),
                    parts[1].toDouble(),
                    parts[2].toDouble(),
                )
            }
        }
        override fun toString(): String {
            return "$startingRadius,$radiusDiff,$iconDistance"
        }
    }

    /**
     * all possible coordinates for icons for [0,0] as its center
     * @param iconOffset
     * @param indexToRC index to row and column
     */
    data class IconCoordinatesResult(
        val iconCoordinates: List<IconCoordinate>,
        val iconOffset: List<Offset>,
        val indexToRC: List<Pair<Int,Int>>,
        val coordinateToIndex: List<List<Int>>,
        val iconsPerRound: List<Int>,
        val startingPointOfRound: List<Float>,
    )

    companion object{

        fun generateIconCoordinates (input: IconCoordinatesGenerationScheme = IconCoordinatesGenerationScheme()): IconCoordinatesResult {
            val startingRadius = input.startingRadius
            val radiusDiff = input.radiusDiff
            val iconDistance = input.iconDistance
            val rounds = 10

            val startArcAngle:Double = -90.0
            val endArcAngle = 90.0

            val indexToCoordinates = mutableListOf<Pair<Int,Int>>()
            val coordinateToIndex = mutableListOf<MutableList<Int>>()
            val iconsPerRound = mutableListOf<Int>()
            val startingPointOfRound = mutableListOf<Float>()

            var row = 0
            var radius = startingRadius
            val iconCoordinates = mutableListOf<IconCoordinate>()
            var curI = 0
            (0 until rounds).forEach { _ ->
                val halfAngleForIconDistance = 0f
                val startAngle = startArcAngle + halfAngleForIconDistance
                val endAngle = endArcAngle - halfAngleForIconDistance

                startingPointOfRound.add((radius.toFloat() - input.radiusDiff/2).toFloat())
                val iconsInRing = ((endAngle - startAngle) / angleOnArc(radius, iconDistance)).toInt()
                iconsPerRound.add(iconsInRing)
                val angle = (endAngle - startAngle)/ iconsInRing
                var col = 0
                var curr = startAngle
                val currRound = mutableListOf<Int>()
                while (curr < endAngle){
                    iconCoordinates.add(IconCoordinate(radius,180 - curr - angle/2))
                    indexToCoordinates.add(Pair(row,col))
                    currRound.add(curI)
                    curI++
                    curr += angle
                    col++
                }
                coordinateToIndex.add(currRound)
                radius += radiusDiff
                row++
            }
            return IconCoordinatesResult(
                iconCoordinates,
                iconCoordinates.toList().map { c -> positionOnCircle(c.distance, c.angle) },
                indexToCoordinates.toList(),
                coordinateToIndex,
                iconsPerRound,
                startingPointOfRound,
            )
        }

        fun getUsableOffsets(
            allOffsets: List<Offset>,
            center: Offset,
            count: Int,
            sliderPosY: Float,
            sliderHeight: Float,
            right: Int,
            left: Int,
            Top: Float,
            Bot: Float,
            LabelHeight: Float,
        ): IconOffsetComputeResult {
//            val top = if (sliderPosY > LabelHeight+50f) sliderPosY + 50f else Top
            val bot = if (sliderPosY > LabelHeight+50f) Bot else sliderPosY + sliderHeight - 50f
            if (allOffsets.isEmpty()) return IconOffsetComputeResult(0, allOffsets, emptyList())
            val iconSizeOffset = Offset(2.5f, 2.5f)

                val offsets = mutableListOf<Offset>()
                val skips= mutableListOf<Pair<Int,Int>>()
                var j = 0
                for (i in 0 until count) {
                    if (j !in allOffsets.indices) break
                    var c = center + allOffsets[j] - iconSizeOffset
                    if (!(
                        (c.x > left)
                        && (c.x < right)
//                        && (c.y > top)
                        && (c.y < bot)
                        )){
                        val skipStart: Int = j
                        while (!(
                            (c.x > left)
                            && (c.x < right)
//                            && (c.y > top)
                            && (c.y < bot)
                                ))
                        {
                            j += 1
                            if (j !in allOffsets.indices) break
                            c = center + allOffsets[j] - iconSizeOffset
                        }
                        val skipEnd: Int = j
                        skips.add(Pair(skipStart,skipEnd))
                    }
                    j++
                    offsets.add(c)
                }
                return IconOffsetComputeResult(0, offsets, skips)
        }

        fun getClosest (
            touchOffset: Offset,
            allOffsets: List<Offset>,
            range: Float,
        ): Int {
            var bestDist = Float.MAX_VALUE
            var best = -1
            allOffsets.forEachIndexed{ i, it ->
                val dist = distance(touchOffset, it)
                if (dist < range) {
                    if (dist < bestDist) {
                        bestDist = dist
                        best = i
                    }
                }
            }
            return best
        }

        fun getIconSelection (
            touchOffset: Offset,
            allOffsets: List<Offset>,
            range: Float,
        ): AppSelectionResult {
            var selectedApp = -1
            var selectionDist = Float.NEGATIVE_INFINITY
            val selectionVals = mutableMapOf<Int, Float>()
            allOffsets.forEachIndexed{ i, it ->
                val dist = distance(touchOffset, it)
                if (dist < range) {
                    selectionVals[i] = 1 - dist / range
                    if (selectionVals[i]!! > selectionDist ) {
                        selectedApp = i
                        selectionDist = selectionVals[i]!!
                    }
                }
            }
            return AppSelectionResult( selectedApp = selectedApp, selectionValues = selectionVals)
        }

        private fun angleOnArc(radius: Double, distance: Double): Double {
            if (radius <= 0.0 || distance <= 0.0) {
                throw IllegalArgumentException("Both radius and distance must be positive values , radius:${radius}, distance:${distance}")
            }
            val angleInRadians = distance / radius
            val angleInDegrees = Math.toDegrees(angleInRadians)
            return angleInDegrees
        }

        private fun positionOnCircle(radius: Double, angleDegrees: Double): Offset {
            if (radius <= 0.0) { throw IllegalArgumentException("Radius must be a positive value") }
            val angleRadians = Math.toRadians(angleDegrees)
            val x = radius * cos(angleRadians)
            val y = radius * sin(angleRadians)

            return Offset(x.toFloat(), y.toFloat())
        }

        private fun distance(a: Offset, b: Offset): Float{
            val X = b.x - a.x
            val Y = b.y - a.y
            return sqrt((X*X) + (Y*Y))
        }
    }
}