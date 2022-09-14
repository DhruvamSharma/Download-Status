package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var progress = 0.0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color = Color.BLACK
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    private var radius = 20f


    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()    // redraw the screen
        requestLayout() // when rectangular progress dimension changes
    }


    init {
        isClickable = true
        valueAnimator.addUpdateListener(updateListener)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas?.drawText("Download", 0f, 0f, paint)

        if (buttonState == ButtonState.Loading) {
            paint.color = Color.YELLOW
            canvas?.drawRect(
                0f, 0f,
                (widthSize * (progress / 100)).toFloat(), heightSize.toFloat(), paint
            )
        }

        paint.color = Color.YELLOW
        canvas?.drawCircle((widthSize / 2).toFloat(), (heightSize / 2).toFloat(), radius, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    // call after downloading is completed
    fun hasCompletedDownload() {
        // cancel the animation when file is downloaded
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
        requestLayout()
    }

    override fun performClick(): Boolean {
        super.performClick()
//        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
//        animation()
        buttonState = ButtonState.Loading
        progress = 40.0
        invalidate()
        requestLayout()
        return true
    }

    private fun animation() {
        valueAnimator.start()
    }

}