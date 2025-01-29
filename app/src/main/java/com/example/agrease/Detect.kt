package com.example.agrease

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.agrease.ml.Padi
import com.example.agrease.ml.Kentang
import com.example.agrease.ml.Jagung
import com.example.agrease.ml.Tomat
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class Detect : AppCompatActivity() {

    lateinit var solutionBtn: Button
    lateinit var predictBtn: Button
    lateinit var backBtn: Button
    lateinit var resView: TextView
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detect)

        var padiLabels = application.assets.open("padi_labels.txt").bufferedReader().readLines()
        var tomatLabels = application.assets.open("tomat_labels.txt").bufferedReader().readLines()
        var kentangLabels = application.assets.open("kentang_labels.txt").bufferedReader().readLines()
        var jagungLabels = application.assets.open("jagung_labels.txt").bufferedReader().readLines()
        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        predictBtn = findViewById(R.id.predictBtn)
        resView = findViewById(R.id.resView)
        solutionBtn = findViewById(R.id.solutionBtn)
        backBtn = findViewById(R.id.backBtn)
        imageView = findViewById(R.id.imageView)

        // get uri from intent
        val uri = intent.getStringExtra("uri")
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(uri))
        imageView.setImageBitmap(bitmap)

        // get label from intent
        val label = intent.getStringExtra("label")

        // if predictBtn Button is clicked do inference
        predictBtn.setOnClickListener {
            // Turn the solutionBtn Button into visible
            solutionBtn.visibility = View.VISIBLE
            // Load model if the label is padi load the padi model else load the tomat model
            if (label == "padi") {
                try {
                    val model = Padi.newInstance(this)
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
                    resView.text = padiLabels[max]
                }
                catch (e: Exception) {
                    // send toast message
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else if(label == "tomat"){
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
            else if(label == "kentang"){
                try {
                    val model = Kentang.newInstance(this)
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
                    resView.text = kentangLabels[max]
                }
                catch(e: Exception) {
                    // send toast message
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                try {
                    val model = Jagung.newInstance(this)
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
                    resView.text = jagungLabels[max]
                }
                catch(e: Exception) {
                    // send toast message
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
        //if the solutionBtn Button is clicked go to the solution activity
        solutionBtn.setOnClickListener {
            // trasform the label to lowercase and turn space into underscore
            val label = resView.text.toString().toLowerCase().replace(" ", "_").replace("-", "_")
            // send the label to the solution Fragment
            val bundle = Bundle()
            bundle.putString("label", label)
            val fragment = Solution()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.detect, fragment)
                .addToBackStack(null)
                .commit()
        }
        
        // if the backBtn Button is clicked go back to the main activity
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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