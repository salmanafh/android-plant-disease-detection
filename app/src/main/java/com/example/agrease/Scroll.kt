package com.example.agrease

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.agrease.ml.Padi
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class Scroll : Fragment() {

    private var selectedLabel: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scroll, container, false)

        // get intent
        val fragment = arguments?.getString("method")

        // Setup to get the image from camera if fragment is camera if fragment is select the user will select an image from gallery
        val buttons = mapOf(
            R.id.padiBtn to "padi",
            R.id.tomatBtn to "tomat",
            R.id.kentangBtn to "kentang",
            R.id.jagungBtn to "jagung"
        )

        val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                handleResult(result.data)
            }
        }

        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                handleResult(result.data)
            }
        }

        if (fragment == "camera") {
            buttons.forEach { (buttonId, label) ->
                view.findViewById<Button>(buttonId).setOnClickListener {
                    selectedLabel = label
                    // add permission before launching the camera
                    if (activity?.checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraLauncher.launch(intent)
                    } else {
                        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 1)
                    }
                }
            }
        } else if (fragment == "select") {
            buttons.forEach { (buttonId, label) ->
                view.findViewById<Button>(buttonId).setOnClickListener {
                    // add permission before launching the gallery
                    if(activity?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        selectedLabel = label
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        galleryLauncher.launch(intent)
                    } else {
                        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    }
                }
            }
        }   
        return view
    }

    private fun handleResult(data: Intent?) {
        if (data != null) {
            val intent = Intent(activity, Detect::class.java)
            intent.putExtra("label", selectedLabel)
            if (data.data != null) {
                val imageUri = data.data
                intent.putExtra("uri", imageUri.toString())
            } else {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val path = MediaStore.Images.Media.insertImage(activity?.contentResolver, imageBitmap, "Title", null)
                intent.putExtra("uri", path)
            }
            startActivity(intent)
        }
    }
}