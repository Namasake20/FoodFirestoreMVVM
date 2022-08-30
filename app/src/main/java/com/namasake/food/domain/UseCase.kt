package com.namasake.food.domain

import com.namasake.food.data.entity.Food
import com.namasake.food.data.network.Repo

class UseCase(private val repo: Repo){
    suspend fun getFood(): List<Food> = repo.getFood()
}