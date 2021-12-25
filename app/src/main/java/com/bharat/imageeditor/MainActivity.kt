package com.bharat.imageeditor

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bharat.imageeditor.databinding.ActivityMainBinding
import android.R.attr.data
import android.graphics.Bitmap

import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClick()
    }

    private fun onClick() {
        binding.btnChoose.setOnClickListener {
            uploadImage()
        }

        binding.btnHoriz.setOnClickListener {
            binding.img.scaleX = -1f
        }

        binding.btnVert.setOnClickListener {
            binding.img.scaleY = -1f
        }

        binding.btnReset.setOnClickListener {
            binding.img.scaleX = 1f
            binding.img.scaleY = 1f

        }
    }

    private fun uploadImage() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                binding.img.setImageURI(resultUri)

                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        resultUri
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val baos = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 10, baos)
                val imageBytes = baos.toByteArray()
                var imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}