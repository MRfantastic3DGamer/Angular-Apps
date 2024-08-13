package com.dhruv.angularapps

import android.animation.ValueAnimator

class AnimatedFloat(
    private val initialValue: Float,
    duration: Long = 300L,
    private val onUpdate: (Float) -> Unit
) {
    private var currentValue: Float = initialValue
    private var targetValue: Float = initialValue
    private val animator: ValueAnimator = ValueAnimator.ofFloat(initialValue, initialValue)

    init {
        animator.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Float
            onUpdate(currentValue)
        }
        animator.duration = duration
    }

    fun setTargetValue(newValue: Float) {
        if (newValue != targetValue) {
            targetValue = newValue
            animator.setFloatValues(currentValue, targetValue)
            animator.start()
        }
    }

    fun getValue(): Float {
        return currentValue
    }

    fun cancelAnimation() {
        animator.cancel()
        currentValue = initialValue
        onUpdate(currentValue)
    }
}
