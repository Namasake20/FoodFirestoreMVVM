package com.namasake.food.data.network

import com.namasake.food.core.Resource
import com.namasake.food.data.entity.Food
import kotlinx.coroutines.flow.Flow

interface Repo {
    suspend fun getFood(): Flow<Resource<List<Food>>>

    suspend fun deleteFood(foodId:String)
}