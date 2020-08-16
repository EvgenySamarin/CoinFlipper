package com.example.hellokitty

import android.graphics.Camera
import android.graphics.Matrix
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView


class FlipAnimation(val view: ImageView) : Animation(), Animation.AnimationListener {
    private var camera: Camera? = null
    private var centerX = 0f
    private var centerY = 0f
    private var forward = true

    private var once: Int = 0

    private val drawableTail: Int = R.drawable.ic_coin_tails
    private val drawableHead: Int = R.drawable.ic_coin_heads
    private var repeatReverseTic: Int = 0
    private var currentTic: Int = 0
    private var randomSpin: Double = 0.0

    fun reverse() {
        forward = false
    }

    override fun initialize(
        width: Int,
        height: Int,
        parentWidth: Int,
        parentHeight: Int
    ) {
        super.initialize(width, height, parentWidth, parentHeight)
        centerX = width / 2.toFloat()
        centerY = height / 2.toFloat()
        camera = Camera()
        once = 0
    }

    override fun applyTransformation(
        interpolatedTime: Float,
        t: Transformation
    ) {
        // Angle around the y-axis of the rotation at the given time
        // calculated both in radians and degrees.
        val radians = Math.PI * interpolatedTime
        var degrees = (180.0 * radians / Math.PI).toFloat()

        // Once we reach the midpoint in the animation, we need to hide the
        // source view and show the destination view. We also need to change
        // the angle by 180 degrees so that the destination does not come in flipped around

        val isMiddleAnimation: Boolean =
            if (repeatReverseTic % 2 == 0)
                interpolatedTime >= 0.5f
            else interpolatedTime <= 0.5f

        if (isMiddleAnimation) {
            degrees -= 180f

            if (once == 0) {
                if (view.tag == drawableHead) {
                    view.tag = drawableTail
                    view.setImageDrawable(view.context.getDrawable(drawableTail))
                } else {
                    view.tag = drawableHead
                    view.setImageDrawable(view.context.getDrawable(drawableHead))
                }
            }
            once++
        }
        if (forward) degrees = -degrees //determines direction of rotation when flip begins
        val matrix: Matrix = t.matrix
        camera!!.save()

        when {
            randomSpin < 0.3 -> camera!!.rotateY(degrees)
            randomSpin < 0.6 -> camera!!.rotateX(degrees)
            randomSpin < 1 -> {
                camera!!.rotateY(degrees)
                camera!!.rotateX(degrees)
            }
        }

        camera!!.getMatrix(matrix)
        camera!!.restore()


        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)
    }


    override fun onAnimationRepeat(animation: Animation?) {
        if (repeatMode == REVERSE)
            repeatReverseTic++
        currentTic++

        when {
            repeatCount - currentTic <= 2 -> duration += 75
            repeatCount - currentTic <= 5 -> duration += 35
            repeatCount - currentTic <= 10 -> duration += 20
        }
        once = 0
    }

    override fun onAnimationEnd(animation: Animation?) {
        repeatReverseTic = 0
        currentTic = 0
        duration = 100
        randomSpin = Math.random()
    }

    override fun onAnimationStart(animation: Animation?) {

    }

    /**
     * Creates a 3D flip animation between two views.
     *
     * @param fromView First view in the transition.
     * @param toView   Second view in the transition.
     */
    init {
        view.tag = drawableHead
        view.setImageDrawable(view.context.getDrawable(drawableHead))
        duration = 100
        randomSpin = Math.random()
        fillAfter = false
        interpolator = AccelerateDecelerateInterpolator()
        this.setAnimationListener(this)
    }
}