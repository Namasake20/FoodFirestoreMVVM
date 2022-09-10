package com.namasake.food.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.namasake.food.core.Constants.FOOD_COLLECTION
import com.namasake.food.core.Resource
import com.namasake.food.data.entity.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RepoImpl:Repo {
    private val firestore = FirebaseFirestore.getInstance()
    private val foodCollection = firestore.collection(FOOD_COLLECTION)
    override suspend fun getFood():Flow<Resource<List<Food>>> = flow {

        emit(Resource.Loading())

        try {
            val food = foodCollection.get().await().toObjects(Food::class.java)
            emit(Resource.Success(food))
        }catch (e:Exception){
//            emit(Resource.Failure(exception = e))
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
}