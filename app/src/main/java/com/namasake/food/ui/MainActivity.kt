package com.namasake.food.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.namasake.food.data.entity.Food
import com.namasake.food.data.network.RepoImpl
import com.namasake.food.databinding.ActivityMainBinding
import com.namasake.food.domain.UseCase
import com.namasake.food.presentation.MainViewModel
import com.namasake.food.presentation.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.collections.ArrayList

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), FoodAdapter.OnFoodClickListener {
    private val viewModel by lazy { ViewModelProvider(this,MainViewModelFactory(UseCase(RepoImpl()))).get(MainViewModel::class.java)}
    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launchWhenStarted {
            viewModel.getFoods()
            observeData()
        }

    }

    private suspend fun observeData() {
        val foodAdapter = FoodAdapter(this)
        viewModel.foodItems.collect { event ->
            when(event){
                is MainViewModel.FoodEvent.Success -> {
                    binding.progressBar.isVisible = false
                    binding.apply {
                        recyclerView.apply {
                            adapter = foodAdapter
                            layoutManager = LinearLayoutManager(this@MainActivity)
                        }
                        foodAdapter.submitList(event.result)
                        event.result?.let { foodAdapter.setFoodList(it) }
                    }

                    val tempList: ArrayList<Food> = (event.result as ArrayList<Food>?)!!

                    binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            binding.searchView.clearFocus()
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            val filteredList: ArrayList<Food> = ArrayList()
                            val searchText = newText!!.toLowerCase(Locale.getDefault())

                            if (searchText.isNotEmpty()) {
//                                for (item in tempList) {
//                                    if (item.name!!.toLowerCase(Locale.getDefault()).contains(searchText)) {
//                                        filteredList.add(item)
//                                        foodAdapter.notifyDataSetChanged()
//
//                                    }
//                                }
                                tempList.forEach {
                                    if (it.name!!.toLowerCase(Locale.getDefault()).contains(searchText)) {
                                        filteredList.add(it)
                                        foodAdapter.notifyDataSetChanged()

                                    }
                                }

                                if (filteredList.isNotEmpty()) {
                                    foodAdapter.setFoodList(filteredList)
                                    foodAdapter.submitList(filteredList)
                                    foodAdapter.notifyDataSetChanged()
                                } else {
                                    Log.e(TAG, "No data")
                                }
                            } else {
                                filteredList.clear()

                            }
                            return false
                        }

                    })
                }
                is MainViewModel.FoodEvent.Loading ->{
                    binding.progressBar.isVisible = true
                }
                is MainViewModel.FoodEvent.Failure -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, "Something wrong: "+event.errorText, Toast.LENGTH_SHORT).show()
                    Log.e(TAG,event.errorText)
                }
                is MainViewModel.FoodEvent.Empty -> Unit
            }

        }
    }

    override fun onFoodClick(food: Food, position: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("food",food)
        startActivity(intent)
    }
}