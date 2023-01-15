package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.strictmode.CredentialProtectedWhileLockedViolation
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.content.res.getStringOrThrow
import androidx.core.content.withStyledAttributes
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var backGroundColor = 0
    private var foreGoundColor = 0
    private var textColor = 0
    private var textDownload= ""
    private var textLoading = ""

    private var rotationValue = 0f
    private var textButtonState = ""
    private var progress = 0

    private var valueAnimator = ValueAnimator()

    private val rect =
        RectF(
        700f,
        50f,
        810f,
        110f
        )


   private  val paint = Paint()
        .apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create("",Typeface.BOLD)
            textSize = 55.0F
        }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Clicked ->{
            Log.e("TAG","Button Clicked")
            }
                ButtonState.Loading ->{
                    textButtonState = textLoading
                   valueAnimator.start()
                }
            ButtonState.Completed ->{
                textButtonState = textDownload
                valueAnimator.cancel()
                progress = 0
            }
        }
        invalidate()
    }


    init {

        context.withStyledAttributes(attrs,R.styleable.LoadingButton){
            backGroundColor = getColor(R.styleable.LoadingButton_backgroundColor,0)
            foreGoundColor = getColor(R.styleable.LoadingButton_foregroundColor,0)
            textColor = getColor(R.styleable.LoadingButton_textColor1,0)
            textDownload = getStringOrThrow(R.styleable.LoadingButton_textDownload)
            textLoading = getStringOrThrow(R.styleable.LoadingButton_textLoading)
        }
        buttonState = ButtonState.Completed

        valueAnimator = ValueAnimator.ofInt(0, 360).apply {
            duration = 3000
            addUpdateListener {
                progress = (it.animatedValue) as Int
                invalidate()
            }
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }

    }




    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        paint.color = backGroundColor
        canvas.drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(),paint)

        paint.color = foreGoundColor
        canvas.drawRect(0f,0f,(width*progress/360f),height.toFloat(),paint)

        paint.color = Color.RED

        canvas.drawArc(rect,0f,progress.toFloat(),true,paint)

        paint.color = textColor

        canvas.drawText(textButtonState,(width/2.0f),(height/2.0f)+20.0f,paint)
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

}