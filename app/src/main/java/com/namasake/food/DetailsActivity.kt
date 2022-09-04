package com.namasake.food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.namasake.food.data.entity.Food
import com.namasake.food.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
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

    }
}