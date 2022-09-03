package com.namasake.food.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.namasake.food.core.Constants.FOOD_COLLECTION
import com.namasake.food.core.Resource
import com.namasake.food.data.entity.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class RepoImpl:Repo {
    override suspend fun getFood():Flow<Resource<List<Food>>> = flow {
        val firestore = FirebaseFirestore.getInstance()
        val foodCollection = firestore.collection(FOOD_COLLECTION)

        emit(Resource.Loading())

        try {
            val food = foodCollection.get().await().toObjects(Food::class.java)
            emit(Resource.Success(food))
        }catch (e:Exception){
//            emit(Resource.Failure(exception = e))
        }

    }
}