package com.namasake.food.data.network

import com.namasake.food.data.entity.Food

interface Repo {
    suspend fun getFood(): List<Food>
}