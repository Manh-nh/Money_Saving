package com.example.moneymanagement.presentation.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class JarGraphicView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var percentage: Float = 0f

    private val jarStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#EAEAEA") // Soft outline for jar
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(3f)
    }

    private val jarInnerBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F9F9F9") // Very soft light grey jar inside background
        style = Paint.Style.FILL
    }

    private val lidPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#EAEAEA") // Same color as outline for consistency
        style = Paint.Style.FILL
    }

    private val liquidPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFE082") // Golden yellow liquid
        style = Paint.Style.FILL
    }

    fun setPercentage(percent: Float) {
        this.percentage = percent.coerceIn(0f, 100f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        if (w == 0f || h == 0f) return

        val pad = dpToPx(2f)

        // 1. Draw Lid
        val lidHeight = h * 0.08f
        val lidWidth = w * 0.45f
        val lidLeft = (w - lidWidth) / 2
        val lidTop = pad
        val lidRight = lidLeft + lidWidth
        val lidBottom = lidTop + lidHeight
        val lidRect = RectF(lidLeft, lidTop, lidRight, lidBottom)
        val lidCorner = dpToPx(3f)
        canvas.drawRoundRect(lidRect, lidCorner, lidCorner, lidPaint)

        // 2. Draw Neck
        val neckHeight = h * 0.04f
        val neckWidth = w * 0.35f
        val neckLeft = (w - neckWidth) / 2
        val neckTop = lidBottom
        val neckRight = neckLeft + neckWidth
        val neckBottom = neckTop + neckHeight
        val neckRect = RectF(neckLeft, neckTop, neckRight, neckBottom)
        canvas.drawRect(neckRect, lidPaint)

        // 3. Draw Body
        val strokeW = jarStrokePaint.strokeWidth
        val bodyLeft = pad + strokeW / 2
        val bodyTop = neckBottom + strokeW / 2
        val bodyRight = w - pad - strokeW / 2
        val bodyBottom = h - pad - strokeW / 2
        val bodyRect = RectF(bodyLeft, bodyTop, bodyRight, bodyBottom)
        val bodyCorner = (bodyRight - bodyLeft) / 4.5f

        // Draw jar inside background
        canvas.drawRoundRect(bodyRect, bodyCorner, bodyCorner, jarInnerBgPaint)

        // 4. Draw liquid with clipping
        val clipPath = Path().apply {
            addRoundRect(bodyRect, bodyCorner, bodyCorner, Path.Direction.CW)
        }

        canvas.save()
        canvas.clipPath(clipPath)

        val innerHeight = bodyRect.height()
        val liquidHeight = innerHeight * (percentage / 100f)
        val liquidTop = bodyRect.bottom - liquidHeight
        val liquidRect = RectF(bodyRect.left - strokeW, liquidTop, bodyRect.right + strokeW, bodyRect.bottom + strokeW)

        canvas.drawRect(liquidRect, liquidPaint)
        canvas.restore()

        // 5. Draw jar outline on top
        canvas.drawRoundRect(bodyRect, bodyCorner, bodyCorner, jarStrokePaint)
    }

    private fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }
}
