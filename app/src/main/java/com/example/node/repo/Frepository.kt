package com.example.node.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.node.data.NewsData1
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Frepository {
    private val db = FirebaseFirestore.getInstance()

    fun uploadData(heading: String, description: String, report: String, imageUrl: String, tags: List<String>, timeStamp: Long) {
        val data = hashMapOf(
            "heading" to heading,
            "description" to description,
            "report" to report,
            "imageUrl" to imageUrl,
            "tags" to tags,
            "time" to timeStamp
        )

        db.collection("news").add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("FirebaseRepository", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseRepository", "Error adding document", e)
            }
    }

    fun uploadData2(heading1: String, description1: String, report1: String, imageUrl1: String, timeStamp: Long, heading2: String, description2: String, report2: String, imageUrl2: String) {
        Log.d("FirebaseRepository", "Uploading data to Firebase...")
        val data = hashMapOf(
            "heading1" to heading1,
            "description1" to description1,
            "report1" to report1,
            "imageUrl1" to imageUrl1,
            "time" to timeStamp,
            "heading2" to heading2,
            "description2" to description2,
            "report2" to report2,
            "imageUrl2" to imageUrl2
        )

        db.collection("flipnews").add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("FirebaseRepository", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseRepository", "Error adding document", e)
            }
    }


    fun uploadData3(videoUrl: String, timeStamp: Long, heading: String, imageUrl: String) {
        Log.d("FirebaseRepository", "Uploading data to Firebase...")
        val data = hashMapOf(
            "VideoUrl" to videoUrl,
            "heading" to heading,
            "imageUrl" to imageUrl,
            "Time" to timeStamp
        )

        db.collection("YTvideo").add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("FirebaseRepository", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseRepository", "Error adding document", e)
            }
    }

    fun uploadUserID(uuid: String) {
        Log.d("FirebaseRepository", "Uploading data to Firebase...")
        val data = hashMapOf(
            "uuid" to uuid
        )

        db.collection("VerifiedUsers").add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("FirebaseRepository", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseRepository", "Error adding document", e)
            }
    }

    fun fetchNewsFromFirestore(): LiveData<List<NewsData1>> {
        val liveData = MutableLiveData<List<NewsData1>>()
        db.collection("news").orderBy("time", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { querySnapshot ->
                val news = querySnapshot.documents.map { document ->
                    NewsData1(
                        id = document.id,
                        description = document.getString("description") ?: "",
                        heading = document.getString("heading") ?: "",
                        imageUrl = document.getString("imageUrl") ?: "",
                        report = document.getString("report") ?: "",
                        tags = document.get("tags") as? List<String>,
                        time = document.getLong("time") ?: 0L
                    )
                }
                liveData.value = news
            }
            .addOnFailureListener { exception ->
                Log.d("FirestoreRepository", "Error getting news", exception)
                liveData.value = emptyList()
            }
        return liveData
    }

    fun deleteNews(newsId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("news").document(newsId).delete()
            .addOnSuccessListener {
                Log.d("FirestoreRepository", "News successfully deleted!")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreRepository", "Error deleting news", exception)
                onFailure(exception)
            }
    }
}