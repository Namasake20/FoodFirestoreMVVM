package com.namasake.food.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.namasake.food.core.Constants.FOOD_COLLECTION
import com.namasake.food.core.Resource
import com.namasake.food.data.entity.Food
import kotlinx.coroutines.tasks.await

class RepoImpl:Repo {
    override suspend fun getFood(): List<Food> {
        val firestore = FirebaseFirestore.getInstance()
        val foodCollection = firestore.collection(FOOD_COLLECTION)

        return try {
            foodCollection.get().await().toObjects(Food::class.java)
        }catch (e:Exception){
            return emptyList()
        }
    }
}