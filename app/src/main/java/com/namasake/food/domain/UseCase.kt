package com.namasake.food.domain

import com.namasake.food.core.Resource
import com.namasake.food.data.entity.Food
import com.namasake.food.data.network.Repo
import kotlinx.coroutines.flow.Flow

class UseCase(private val repo: Repo){
    suspend fun getFood(): Flow<Resource<List<Food>>> = repo.getFood()

    suspend fun deleteFood(foodId:String) = repo.deleteFood(foodId)
}