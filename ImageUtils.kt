package com.shilpakala.app.utils

import android.content.Context
import android.graphics.*
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

/**
 * ImageUtils - The GenAI-inspired branding engine.
 *
 * Applies professional branding overlays to artisan product photos:
 * - Semi-transparent luxury panel at bottom
 * - "Handmade in Karnataka" heritage label
 * - Artisan name, product name, wood type, price
 * - Elegant color scheme: cream, gold, dark brown
 */
object ImageUtils {

    private const val TAG = "ImageUtils"

    // Brand colors
    private val COLOR_DARK_BG = Color.argb(210, 20, 12, 5)        // Very dark brown, semi-transparent
    private val COLOR_GOLD = Color.parseColor("#C9A84C")            // Heritage gold
    private val COLOR_GOLD_LIGHT = Color.parseColor("#F0C878")      // Light gold for headings
    private val COLOR_CREAM = Color.parseColor("#FFF8E7")           // Cream for text
    private val COLOR_DIVIDER = Color.argb(150, 201, 168, 76)       // Gold divider

    fun generateBrandedImage(
        context: Context,
        sourcePath: String,
        artisanName: String,
        productName: String,
        woodType: String,
        price: String
    ): String? {
        return try {
            // Load source bitmap
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(sourcePath, options)

            // Downsample if too large (keep max 2000px)
            val maxDim = 2000
            val sampleSize = calculateSampleSize(options.outWidth, options.outHeight, maxDim, maxDim)
            val loadOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }

            val sourceBitmap = BitmapFactory.decodeFile(sourcePath, loadOptions)
                ?: return null

            // Create mutable copy
            val resultBitmap = sourceBitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(resultBitmap)

            val w = resultBitmap.width.toFloat()
            val h = resultBitmap.height.toFloat()

            // === DRAW BRANDING OVERLAY ===
            drawBrandingOverlay(canvas, w, h, artisanName, productName, woodType, price)

            // Save to cache
            val outputFile = createOutputFile(context)
            FileOutputStream(outputFile).use { out ->
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
            }

            // Clean up
            sourceBitmap.recycle()
            resultBitmap.recycle()

            outputFile.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error generating branded image", e)
            null
        }
    }

    private fun drawBrandingOverlay(
        canvas: Canvas,
        w: Float,
        h: Float,
        artisanName: String,
        productName: String,
        woodType: String,
        price: String
    ) {
        val panelHeight = h * 0.32f
        val panelTop = h - panelHeight

        // === BACKGROUND PANEL ===
        // Gradient from transparent to dark
        val gradientPaint = Paint()
        val gradient = LinearGradient(
            0f, panelTop - (h * 0.05f), 0f, h,
            intArrayOf(Color.TRANSPARENT, COLOR_DARK_BG, COLOR_DARK_BG),
            floatArrayOf(0f, 0.25f, 1f),
            Shader.TileMode.CLAMP
        )
        gradientPaint.shader = gradient
        canvas.drawRect(0f, panelTop - (h * 0.05f), w, h, gradientPaint)

        // === TOP GOLD DIVIDER LINE ===
        val dividerPaint = Paint().apply {
            color = COLOR_GOLD
            strokeWidth = 2f
            isAntiAlias = true
        }
        canvas.drawLine(w * 0.06f, panelTop + (h * 0.01f), w * 0.94f, panelTop + (h * 0.01f), dividerPaint)

        // === "HANDMADE IN KARNATAKA" HERITAGE LABEL ===
        val heritagePaint = Paint().apply {
            color = COLOR_GOLD_LIGHT
            textSize = h * 0.028f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC)
            letterSpacing = 0.12f
        }
        canvas.drawText("✦  HANDMADE IN KARNATAKA  ✦", w / 2, panelTop + (h * 0.055f), heritagePaint)

        // === PRODUCT NAME ===
        val productPaint = Paint().apply {
            color = COLOR_CREAM
            textSize = h * 0.048f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }
        canvas.drawText(productName, w / 2, panelTop + (h * 0.115f), productPaint)

        // === ARTISAN NAME ===
        val artisanPaint = Paint().apply {
            color = COLOR_GOLD
            textSize = h * 0.030f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        }
        canvas.drawText("By $artisanName", w / 2, panelTop + (h * 0.163f), artisanPaint)

        // === THIN SEPARATOR ===
        val separatorPaint = Paint().apply {
            color = COLOR_DIVIDER
            strokeWidth = 1f
        }
        canvas.drawLine(w * 0.25f, panelTop + (h * 0.183f), w * 0.75f, panelTop + (h * 0.183f), separatorPaint)

        // === WOOD TYPE | PRICE row ===
        val detailPaint = Paint().apply {
            color = COLOR_CREAM
            textSize = h * 0.026f
            isAntiAlias = true
            typeface = Typeface.DEFAULT
        }

        // Wood type - left aligned
        detailPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("🪵  $woodType", w * 0.08f, panelTop + (h * 0.225f), detailPaint)

        // Price - right aligned in gold
        detailPaint.textAlign = Paint.Align.RIGHT
        detailPaint.color = COLOR_GOLD_LIGHT
        detailPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        detailPaint.textSize = h * 0.032f
        canvas.drawText(price, w * 0.92f, panelTop + (h * 0.225f), detailPaint)

        // === BOTTOM BRAND LOGO / WATERMARK ===
        val brandPaint = Paint().apply {
            color = Color.argb(130, 201, 168, 76)
            textSize = h * 0.020f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            letterSpacing = 0.2f
        }
        canvas.drawText("SHILPA-KALA  •  DIGITAL ARTISAN PORTFOLIO", w / 2, h - (h * 0.022f), brandPaint)

        // === TOP-RIGHT QUALITY BADGE ===
        drawQualityBadge(canvas, w, h)
    }

    private fun drawQualityBadge(canvas: Canvas, w: Float, h: Float) {
        val cx = w * 0.88f
        val cy = h * 0.08f
        val radius = min(w, h) * 0.065f

        // Badge circle
        val badgePaint = Paint().apply {
            color = COLOR_DARK_BG
            isAntiAlias = true
        }
        canvas.drawCircle(cx, cy, radius, badgePaint)

        // Badge border
        val borderPaint = Paint().apply {
            color = COLOR_GOLD
            style = Paint.Style.STROKE
            strokeWidth = 2f
            isAntiAlias = true
        }
        canvas.drawCircle(cx, cy, radius, borderPaint)

        // Badge text
        val badgeTextPaint = Paint().apply {
            color = COLOR_GOLD_LIGHT
            textSize = radius * 0.42f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }
        canvas.drawText("GENUINE", cx, cy - radius * 0.12f, badgeTextPaint)
        badgeTextPaint.textSize = radius * 0.36f
        canvas.drawText("CRAFT", cx, cy + radius * 0.44f, badgeTextPaint)
    }

    private fun calculateSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
        var sampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / sampleSize) >= reqHeight && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2
            }
        }
        return sampleSize
    }

    private fun createOutputFile(context: Context): File {
        val dir = File(context.cacheDir, "branded").apply { mkdirs() }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File(dir, "ShilpaKala_${timestamp}.jpg")
    }
}
