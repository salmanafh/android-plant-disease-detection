package com.example.agrease

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.agrease.ml.Tomat
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class TomatDetect : AppCompatActivity() {

    lateinit var solutionBtn: Button
    lateinit var predictBtn: Button
    lateinit var backBtn: Button
    lateinit var resView: TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomat_detect)
        var tomatLabels = application.assets.open("tomat_labels.txt").bufferedReader().readLines()
        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        predictBtn = findViewById(R.id.predictBtn)
        resView = findViewById(R.id.resView)
        backBtn = findViewById(R.id.backBtn)
        imageView = findViewById(R.id.imageView)

        // get uri from intent
        val uri = intent.getStringExtra("uri")
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(uri))
        imageView.setImageBitmap(bitmap)

        predictBtn.setOnClickListener {
            try {
                    val model = Tomat.newInstance(this)
                    // Creates inputs for reference.
                    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                    val tensorImage = TensorImage(DataType.FLOAT32)
                    tensorImage.load(bitmap)
                    val image = imageProcessor.process(tensorImage)
                    val byteBuffer = image.buffer

                    inputFeature0.loadBuffer(byteBuffer)

                    // Runs model inference and gets result.
                    val outputs = model.process(inputFeature0)
                    val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                    // Releases model resources if no longer used.
                    model.close()

                    // Display result
                    var max = getMax(outputFeature0.floatArray)
                    resView.text = tomatLabels[max]
                }
                catch(e: Exception) {
                    // send toast message
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getMax(arr: FloatArray): Int {
        var maxIndex = 0
        for (i in arr.indices) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i
            }
        }
        return maxIndex
    }
}