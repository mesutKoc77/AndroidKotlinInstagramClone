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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.name.a16androidkotlinfirebaseinstagram.databinding.ActivityUploadBinding
import java.sql.Time
import java.sql.Timestamp
import java.util.UUID

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedPicture:Uri?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()

        auth=Firebase.auth
        firestore=Firebase.firestore
        storage=Firebase.storage

        //Firebase = "  firestore + storage " in toplamindan olusan bir yapidir.







    }

    fun upload (view: View) {

        val uuid = UUID.randomUUID()
        val imageName="$uuid.jpg"

        val reference=storage.reference
        val imageReference=reference.child("images").child(imageName)

        if (selectedPicture!=null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                //simdi bu belgeyi stiorGE E ALDIK ve yukledik, storage kayit olan dosyanin uri nin bize url layim ki firestore saddce hafifi dosyalari kabulediyor. yani bir url lazim bize.
                //download url lazim bize biunu firestore e kayit edebimek icin
                val uploadedPictureReference=storage.reference.child("images").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadedUrl=it.toString()
                    //artik yuklenmis olan file in Url ini aldik ve firestore umuza ekleyebiulirz. Onccelikle url, comment ve diger bilgileri bir hasmap e alalim ve bu hasmap i firestore a
                    //atalim cunku firestore daki her nesene anahtar deger eslemsi yani bir hashmap i kabul ediyor.

                    if (auth.currentUser !=null){
                        val postMap= hashMapOf<String, Any>()
                        postMap.put("downloadedUrl", downloadedUrl)
                        postMap.put("userEmail", auth.currentUser!!.email!!)
                        postMap.put("comment", binding.commentText.text.toString())
                        postMap.put("date", com.google.firebase.Timestamp.now())

                        //biz simdi bir firestore da muhafaza edebilecgimiz bir nesne olusturduk simdi bu nesne yi firestore kutusunun icerisine atacagiz
                        firestore.collection("Posts").add(postMap).addOnSuccessListener {
                            finish()

                        }.addOnFailureListener{
                            Toast.makeText(this@UploadActivity,it.localizedMessage, Toast.LENGTH_LONG).show()
                        }


                    }


                }



            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()

            }
        }








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