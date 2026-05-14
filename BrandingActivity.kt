package com.shilpakala.app.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shilpakala.app.databinding.ActivityBrandingBinding
import com.shilpakala.app.utils.ImageUtils

class BrandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBrandingBinding
    private var imagePath: String = ""

    private val woodTypes = arrayOf(
        "Sandalwood (Chandan)",
        "Rosewood (Sheesham)",
        "Teak (Sagwan)",
        "Neem",
        "Mango Wood",
        "Bamboo",
        "Jackfruit Wood",
        "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        imagePath = intent.getStringExtra(CameraActivity.EXTRA_IMAGE_PATH) ?: ""

        setupWoodTypeSpinner()
        setupPreviewImage()
        setupClickListeners()
    }

    private fun setupWoodTypeSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, woodTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWoodType.adapter = adapter
    }

    private fun setupPreviewImage() {
        if (imagePath.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.imgPreview.setImageBitmap(bitmap)
        }
    }

    private fun setupClickListeners() {
        binding.btnGenerate.setOnClickListener {
            generateBrandedImage()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun generateBrandedImage() {
        val artisanName = binding.etArtisanName.text.toString().trim()
        val productName = binding.etProductName.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()
        val woodType = binding.spinnerWoodType.selectedItem.toString()

        // Validation
        if (artisanName.isEmpty()) {
            binding.etArtisanName.error = "Please enter artisan name"
            return
        }
        if (productName.isEmpty()) {
            binding.etProductName.error = "Please enter product name"
            return
        }
        if (price.isEmpty()) {
            binding.etPrice.error = "Please enter price"
            return
        }

        // Show loading
        binding.btnGenerate.isEnabled = false
        binding.btnGenerate.text = "Creating your catalog photo..."

        // Generate branded image using ImageUtils
        Thread {
            val brandedPath = ImageUtils.generateBrandedImage(
                context = this,
                sourcePath = imagePath,
                artisanName = artisanName,
                productName = productName,
                woodType = woodType,
                price = "₹$price"
            )

            runOnUiThread {
                binding.btnGenerate.isEnabled = true
                binding.btnGenerate.text = "Generate Professional Photo"

                if (brandedPath != null) {
                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra(ResultActivity.EXTRA_BRANDED_PATH, brandedPath)
                        putExtra(ResultActivity.EXTRA_ARTISAN_NAME, artisanName)
                        putExtra(ResultActivity.EXTRA_PRODUCT_NAME, productName)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to generate image. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
