package com.example.bugit.model.repo.apiservice

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

object FirebaseApiService{

    private val storageReference = FirebaseStorage.getInstance().reference
        .child(UUID.randomUUID().toString())
    private val tag = "FirebaseApiService"

     fun uploadImageToGetImageLink(imageUri: Uri, onSuccess: (String?) -> Unit, onFailure: (Exception) -> Unit) {
        var bugImageUrl: String? = null
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                     bugImageUrl = uri.toString()
                    Log.d(tag, "Bug Image URL: $bugImageUrl")
                    onSuccess(bugImageUrl)
                }.addOnFailureListener { exception ->
                    Log.e(tag, "Error in downloading url", exception)
                    onFailure(exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(tag, "Error in uploading Image", exception)
                onFailure(exception)
            }
    }
}