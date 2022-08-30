package com.namasake.food.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.namasake.food.data.entity.Food
import com.namasake.food.domain.UseCase

class MainViewModel(private val useCase: UseCase): ViewModel() {

    private val _food = MutableLiveData<List<Food>>()
    val food:LiveData<List<Food>>
    get() = _food

    suspend fun getFood(){
        _food.value = useCase.getFood()
    }

}