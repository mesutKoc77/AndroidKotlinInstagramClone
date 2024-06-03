package com.name.a16androidkotlinfirebaseinstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.name.a16androidkotlinfirebaseinstagram.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    fun upload (view: View) {

    }

    fun selectImage (view: View) {

    }


}