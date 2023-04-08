package com.project.farmingappss.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.project.farmingappss.adapter.SMPostListAdapter
import kotlinx.android.synthetic.main.fragment_social_media_posts.*

class SocialMediaViewModel: ViewModel() {

    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseFireStore: FirebaseFirestore? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    var data = MutableLiveData<List<DocumentSnapshot>>()

    var postLiveData = MutableLiveData<List<DocumentSnapshot>>()
    fun loadPosts(){

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()

        firebaseFireStore!!.collection("posts").get()
            .addOnSuccessListener {
//                postLiveData.value = it.documents[0].data.get("")
            }
            .addOnFailureListener {

            }

    }

    fun getData(){

        val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()


        firebaseFirestore.collection("Posts").get()
            .addOnSuccessListener {
                if(it.documents.isEmpty()) return@addOnSuccessListener
                Log.d("Posts data", it.documents.toString())
                data.postValue(it.documents)
            }
            .addOnFailureListener {

            }
    }

}