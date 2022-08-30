package com.namasake.food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.namasake.food.data.network.RepoImpl
import com.namasake.food.databinding.ActivityMainBinding
import com.namasake.food.domain.UseCase
import com.namasake.food.presentation.FoodAdapter
import com.namasake.food.presentation.MainViewModel
import com.namasake.food.presentation.MainViewModelFactory

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this,MainViewModelFactory(UseCase(RepoImpl()))).get(MainViewModel::class.java)}
    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenStarted {
            viewModel.getFood()
            observeData()
        }

    }

    private fun observeData() {
        val foodAdapter = FoodAdapter()
        viewModel.food.observe(this, { result ->
            for (res in result){
                Log.i(TAG, res.toString())
            }
            binding.apply {
                recyclerView.apply {
                    adapter = foodAdapter
                    layoutManager = LinearLayoutManager(this@MainActivity)

                }
                foodAdapter.submitList(result)
            }

        })
    }
}