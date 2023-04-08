package com.project.farmingappss.view.socialmedia

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

import com.project.farmingappss.R
import com.project.farmingappss.model.data.NewPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

class SMPostAddingActivity : AppCompatActivity() {

    private var selectedImages = mutableListOf<Uri>()
    private val postsImageStorge = Firebase.storage.reference
    private val firestore = Firebase.firestore
    private val auth = Firebase.auth



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smpost_adding)

//        val selectImagesActivityResult =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val intent = result.data
//
//                    //Multiple images selected
//                    if (intent?.clipData != null) {
//                        val count = intent.clipData?.itemCount ?: 0
//                        (0 until count).forEach {
//                            val imagesUri = intent.clipData?.getItemAt(it)?.uri
//                            imagesUri?.let { selectedImages.add(it) }
//                        }
//
//                        //One images was selected
//                    } else {
//                        val imageUri = intent?.data
//                        imageUri?.let { selectedImages.add(it) }
//                    }
//                    updateImages()
//                }
//            }
//
        val btnImagePicker = findViewById<ImageButton>(R.id.uploadImagePreview)
        btnImagePicker.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)

        }

//
        val btnCreatePost = findViewById<Button>(R.id.createPostBtnSM)
        btnCreatePost.setOnClickListener {
            val validation = validateInformation()
            if(!validation){
                Toast.makeText(this,"Check your inputs",Toast.LENGTH_LONG).show()
            }else{
                savePost()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            selectedImages.add(data?.data!!)
            val postImgPreview = findViewById<ImageButton>(R.id.uploadImagePreview)
            postImgPreview.setImageURI(selectedImages[0])
        }
    }

//    private fun updateImages() {
//        val postImgPreview = findViewById<ImageButton>(R.id.uploadImagePreview)
//        postImgPreview.setImageURI(selectedImages[0])
//    }
//
    private fun savePost() {
        val postTitle = findViewById<EditText>(R.id.postTitleSM).text.toString()
        val postDescription = findViewById<EditText>(R.id.descPostSM).text.toString()
        val imagesByteArrays = getImagesByteArrays()
        val images = mutableListOf<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showLoading()
            }
            try {
                async {
                    imagesByteArrays.forEach {
                        val id = auth.currentUser?.uid
                        launch {
                            val imageStorage = postsImageStorge.child("posts/images/$id")
                            val result = imageStorage.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl)
                        }
                    }
                }.await()
            }catch (e: Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    hideLoading()
                }
            }

            val post = NewPost(
                auth.currentUser?.displayName.toString(),
                auth.currentUser!!.uid,
                postTitle,
                postDescription,
                images
            )

            firestore.collection("Posts").add(post).addOnSuccessListener {
                hideLoading()
                Toast.makeText(this@SMPostAddingActivity,"Post Added",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                hideLoading()
                Log.e("Error",it.message.toString())
            }
        }
}

    private fun hideLoading() {
        val progressBar = findViewById<ProgressBar>(R.id.progress_create_post)
        progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        val progressBar = findViewById<ProgressBar>(R.id.progress_create_post)
        progressBar.visibility = View.VISIBLE
    }

    private fun getImagesByteArrays(): List<ByteArray> {
        val imagesByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach {
            val stream = ByteArrayOutputStream()
            val imageBmp = MediaStore.Images.Media.getBitmap(contentResolver, it)
            if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 85, stream)) {
                val imageAsByteArray = stream.toByteArray()
                imagesByteArray.add(imageAsByteArray)
            }
        }
        return imagesByteArray
    }

    private fun validateInformation(): Boolean {
        if (selectedImages.isEmpty())
            return false


        val postTitle = findViewById<EditText>(R.id.postTitleSM).text.toString()
        if(postTitle.isEmpty())
            return false

        val postDescription = findViewById<EditText>(R.id.descPostSM).text.toString()
        if(postDescription.isEmpty())
            return false

        return true
    }
}