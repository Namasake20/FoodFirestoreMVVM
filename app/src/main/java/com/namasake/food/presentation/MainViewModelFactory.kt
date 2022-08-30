package com.namasake.food.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.namasake.food.domain.UseCase

class MainViewModelFactory(private val useCase: UseCase): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UseCase::class.java).newInstance(useCase)
    }
}