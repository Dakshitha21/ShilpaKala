package com.shilpakala.app.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Custom camera overlay view that draws:
 * - A semi-transparent dark border (outside guide)
 * - A golden guide rectangle (product placement zone)
 * - Corner accent marks
 * - Instruction text
 */
class CameraOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint().apply {
        color = Color.argb(140, 0, 0, 0)
    }

    private val guidePaint = Paint().apply {
        color = Color.parseColor("#C9A84C") // Gold
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val cornerPaint = Paint().apply {
        color = Color.parseColor("#F0C040") // Bright gold
        style = Paint.Style.STROKE
        strokeWidth = 6f
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.parseColor("#F0C040")
        textSize = 36f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val subTextPaint = Paint().apply {
        color = Color.parseColor("#CCCCCC")
        textSize = 28f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val guideRect = RectF()
    private val cornerLength = 60f
    private val cornerRadius = 12f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        // Define guide rectangle (centered, ~70% of screen width, square-ish)
        val margin = w * 0.10f
        val guideTop = h * 0.15f
        val guideBottom = h * 0.75f
        guideRect.set(margin, guideTop, w - margin, guideBottom)

        // Draw 4 darkened regions outside guide rect
        canvas.drawRect(0f, 0f, w, guideTop, backgroundPaint)            // top
        canvas.drawRect(0f, guideBottom, w, h, backgroundPaint)          // bottom
        canvas.drawRect(0f, guideTop, margin, guideBottom, backgroundPaint) // left
        canvas.drawRect(w - margin, guideTop, w, guideBottom, backgroundPaint) // right

        // Draw guide border
        guidePaint.alpha = 180
        canvas.drawRoundRect(guideRect, cornerRadius, cornerRadius, guidePaint)

        // Draw corner accents
        drawCornerAccents(canvas)

        // Draw instruction text above guide
        canvas.drawText("Place your product here", w / 2, guideTop - 24f, textPaint)

        // Draw sub text below guide
        canvas.drawText("Fill the frame for best results", w / 2, guideBottom + 56f, subTextPaint)
    }

    private fun drawCornerAccents(canvas: Canvas) {
        val l = guideRect.left
        val t = guideRect.top
        val r = guideRect.right
        val b = guideRect.bottom
        val cl = cornerLength

        // Top-left
        canvas.drawLine(l, t + cl, l, t + cornerRadius, cornerPaint)
        canvas.drawLine(l + cornerRadius, t, l + cl, t, cornerPaint)

        // Top-right
        canvas.drawLine(r - cl, t, r - cornerRadius, t, cornerPaint)
        canvas.drawLine(r, t + cornerRadius, r, t + cl, cornerPaint)

        // Bottom-left
        canvas.drawLine(l, b - cl, l, b - cornerRadius, cornerPaint)
        canvas.drawLine(l + cornerRadius, b, l + cl, b, cornerPaint)

        // Bottom-right
        canvas.drawLine(r - cl, b, r - cornerRadius, b, cornerPaint)
        canvas.drawLine(r, b - cornerRadius, r, b - cl, cornerPaint)
    }
}
