package com.namasake.food.data.network

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.namasake.food.core.Constants.CREATED_AT
import com.namasake.food.core.Constants.FOOD_COLLECTION
import com.namasake.food.core.Constants.IMAGES
import com.namasake.food.core.Constants.UID
import com.namasake.food.core.Constants.URL
import com.namasake.food.core.Resource
import com.namasake.food.data.entity.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RepoImpl:Repo {
    private val firestore = FirebaseFirestore.getInstance()
    private val foodCollection = firestore.collection(FOOD_COLLECTION)
    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getFood():Flow<Resource<List<Food>>> = flow {

        emit(Resource.Loading)

        try {
            val food = foodCollection.get().await().toObjects(Food::class.java)
            emit(Resource.Success(food))
        }catch (e:Exception){
            emit(Resource.Failure(exception = e))
        }

    }

    override suspend fun deleteFood(foodId: String) {
        val query = foodCollection.whereEqualTo("id",foodId).get()
        query.addOnSuccessListener {
            for (document in it){
                foodCollection.document(document.id).delete().addOnSuccessListener {
                    println("deleted")
                }
            }
        }
    }

    override suspend fun addFood(name: String, description: String, id: String, imageUrl: String) {

        val food = hashMapOf(
                "name" to name,
                "description" to description,
                "id" to id,
                 "imageUrl" to imageUrl
        )

        foodCollection.add(food).addOnSuccessListener {
            println("added")
        }.addOnFailureListener {
            println("something wrong")
        }
    }

    fun addImageToFirebaseStorage(imageUri: Uri): Flow<Resource<Uri>> = flow {
        try {
            emit(Resource.Loading)
            val downloadUrl = storage.reference.child(IMAGES).putFile(imageUri).await()
                    .storage.downloadUrl.await()
            emit(Resource.Success(downloadUrl))

        }catch (e:Exception){
            emit(Resource.Failure(e))
        }

    }

    fun addImageToFirestore(downloadUrl: Uri): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading)
            db.collection(IMAGES).document(UID).set(mapOf(
                    URL to downloadUrl,
                    CREATED_AT to FieldValue.serverTimestamp()
            )).await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

    fun getImageFromFirestore(): Flow<Resource<String?>> = flow {
        try {
            emit(Resource.Loading)
            val imageUrl = db.collection(IMAGES).document(UID).get().await().getString(URL)
            emit(Resource.Success(imageUrl))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


}