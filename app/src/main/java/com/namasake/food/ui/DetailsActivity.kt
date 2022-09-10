package com.namasake.food.ui

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.namasake.food.data.entity.Food
import com.namasake.food.data.network.RepoImpl
import com.namasake.food.databinding.ActivityDetailsBinding
import com.namasake.food.domain.UseCase
import com.namasake.food.presentation.MainViewModel
import com.namasake.food.presentation.MainViewModelFactory


class DetailsActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this,MainViewModelFactory(UseCase(RepoImpl()))).get(MainViewModel::class.java)}
    private lateinit var newBinding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(newBinding.root)


        val food = intent.getParcelableExtra<Food>("food")

        if (food != null){
            newBinding.foodTitle.text =  food.name
            newBinding.foodDesc.text = food.description
            Glide.with(applicationContext).load(food.imageUrl).into(newBinding.imgFood)
        }
        val foodId = food?.id

        newBinding.btnSaveOrDeleteFood.setOnClickListener {
            val alertDialog:AlertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Confirm Delete")
            alertDialog.setMessage("Are you sure you want to delete "+ food?.name +" ?")
            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"Confirm") { _, _ ->
                if (foodId != null) {
                    viewModel.deleteFood(foodId)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"cancel") { _, _ ->
            }
            alertDialog.show()

        }



    }
}