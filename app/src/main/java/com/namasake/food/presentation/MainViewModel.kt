package com.namasake.food.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namasake.food.core.Resource
import com.namasake.food.data.entity.Food
import com.namasake.food.domain.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val useCase: UseCase): ViewModel() {

    sealed class FoodEvent {
        class Success(val result: List<Food>?): FoodEvent()
        class Failure(val errorText: String): FoodEvent()
        object Loading : FoodEvent()
        object Empty : FoodEvent()
    }

    private val _foodItems = MutableStateFlow<FoodEvent>(FoodEvent.Empty)
    val foodItems: StateFlow<FoodEvent> = _foodItems

    fun getFoods(){
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getFood().collect { result ->
                when(result){
                    is Resource.Success ->{
                        val response = result.data
                        _foodItems.value = FoodEvent.Success(response)
                    }
                    is Resource.Loading ->
                        _foodItems.value = FoodEvent.Empty

                    is Resource.Failure -> _foodItems.value = FoodEvent.Failure(result.exception.toString())
                }

            }
        }
    }
    fun deleteFood(id:String){
        viewModelScope.launch {
            useCase.deleteFood(foodId = id)
        }
    }

}