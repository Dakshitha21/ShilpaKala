package com.shilpakala.app.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shilpakala.app.databinding.ActivityResultBinding
import com.shilpakala.app.utils.ShareUtils

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var brandedImagePath: String = ""

    companion object {
        const val EXTRA_BRANDED_PATH = "BRANDED_PATH"
        const val EXTRA_ARTISAN_NAME = "ARTISAN_NAME"
        const val EXTRA_PRODUCT_NAME = "PRODUCT_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        brandedImagePath = intent.getStringExtra(EXTRA_BRANDED_PATH) ?: ""
        val artisanName = intent.getStringExtra(EXTRA_ARTISAN_NAME) ?: ""
        val productName = intent.getStringExtra(EXTRA_PRODUCT_NAME) ?: ""

        setupUI(artisanName, productName)
        loadImage()
        setupClickListeners()
    }

    private fun setupUI(artisanName: String, productName: String) {
        binding.tvResultTitle.text = productName
        binding.tvArtisanCredit.text = "By $artisanName"
    }

    private fun loadImage() {
        if (brandedImagePath.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(brandedImagePath)
            binding.imgBrandedResult.setImageBitmap(bitmap)
        }
    }

    private fun setupClickListeners() {
        // Save to gallery
        binding.btnSave.setOnClickListener {
            val savedUri = ShareUtils.saveToGallery(this, brandedImagePath)
            if (savedUri != null) {
                Toast.makeText(this, "✓ Saved to Gallery (Shilpa-Kala)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save. Check permissions.", Toast.LENGTH_SHORT).show()
            }
        }

        // Share to WhatsApp / other apps
        binding.btnShare.setOnClickListener {
            ShareUtils.shareImage(this, brandedImagePath)
        }

        // Share specifically to WhatsApp
        binding.btnShareWhatsapp.setOnClickListener {
            ShareUtils.shareToWhatsApp(this, brandedImagePath)
        }

        // Take new photo
        binding.btnNewPhoto.setOnClickListener {
            // Go back to camera
            val intent = Intent(this, CameraActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
