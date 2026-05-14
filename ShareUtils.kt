package com.shilpakala.app.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

object ShareUtils {

    private const val TAG = "ShareUtils"

    /**
     * Save branded image to the device gallery under "Shilpa-Kala" album.
     */
    fun saveToGallery(context: Context, imagePath: String): Uri? {
        return try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "ShilpaKala_$timestamp.jpg"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ — use MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/ShilpaKala")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: return null

                context.contentResolver.openOutputStream(uri)?.use { out ->
                    FileInputStream(imagePath).use { it.copyTo(out) }
                }

                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, contentValues, null, null)

                uri
            } else {
                // Pre-Android 10 — direct file save
                val picturesDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "ShilpaKala"
                ).apply { mkdirs() }

                val destFile = File(picturesDir, fileName)
                File(imagePath).copyTo(destFile, overwrite = true)

                // Notify media scanner
                val uri = Uri.fromFile(destFile)
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                uri
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save to gallery", e)
            null
        }
    }

    /**
     * Share branded image via any available app (WhatsApp, Instagram, etc.)
     */
    fun shareImage(context: Context, imagePath: String) {
        try {
            val file = File(imagePath)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(
                    Intent.EXTRA_TEXT,
                    "✨ Handmade in Karnataka 🪵\n" +
                    "Authentic Indian handicraft by a local artisan.\n\n" +
                    "#ShilpaKala #MadeInIndia #KarnatakaHandicrafts #ArtisanCraft"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share your masterpiece via..."))
        } catch (e: Exception) {
            Log.e(TAG, "Share failed", e)
        }
    }

    /**
     * Share directly to WhatsApp.
     */
    fun shareToWhatsApp(context: Context, imagePath: String) {
        try {
            val file = File(imagePath)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                setPackage("com.whatsapp")
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(
                    Intent.EXTRA_TEXT,
                    "✨ Handmade in Karnataka 🪵\n" +
                    "Authentic artisan craft — genuine quality!\n" +
                    "#ShilpaKala #HandmadeInIndia"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Check if WhatsApp is installed
            if (whatsappIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(whatsappIntent)
            } else {
                // Fall back to generic share
                shareImage(context, imagePath)
            }
        } catch (e: Exception) {
            Log.e(TAG, "WhatsApp share failed", e)
            shareImage(context, imagePath)
        }
    }

    fun getFileUri(context: Context, path: String): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(path)
        )
    }
}
