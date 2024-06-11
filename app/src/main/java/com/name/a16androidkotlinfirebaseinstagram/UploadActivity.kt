package com.name.a16androidkotlinfirebaseinstagram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.name.a16androidkotlinfirebaseinstagram.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedPicture:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()


    }

    fun upload (view: View) {

    }

    fun selectImage (view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"Permission needed for Gallery ! (rational)", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()

                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                }

            }

            else {
                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //start activity for Result
                activityResultLauncher.launch(intentToGallery)

            }


        } else {

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Permission needed for Gallery ! (rational)", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()

                } else {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                }
            } else {
                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //start activity for Result
                activityResultLauncher.launch(intentToGallery)


            }

        }


    }










    private fun registerLauncher () {
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode== RESULT_OK){
                val intentFromResult=result.data
                if(intentFromResult !=null){
                    selectedPicture=intentFromResult.data
                    selectedPicture?.let {
                        binding.imageView.setImageURI(it)

                    }
                }


            }

        }

        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()) {result ->
            if(result) {
                //permisson granted
                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            } else {
                //permisson denied
                Toast.makeText(this@UploadActivity,"Permission needed !", Toast.LENGTH_LONG).show()
            }

        }


    }


}