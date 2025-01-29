package com.example.agrease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    lateinit var selectBtn: Button
    lateinit var openCamera: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectBtn = findViewById(R.id.selectBtn)
        openCamera = findViewById(R.id.openCamera)

        val buttonClickListener = { method: String ->
            val frag = Scroll()
            val bundle = Bundle()
            bundle.putString("method", method)
            frag.arguments = bundle
            replaceFragment(frag)
        }

        selectBtn.setOnClickListener {
            buttonClickListener("select")
        }

        openCamera.setOnClickListener {
            buttonClickListener("camera")
        }
    }

        
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainActivity, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        // Check if there are fragments in the back stack to pop
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}