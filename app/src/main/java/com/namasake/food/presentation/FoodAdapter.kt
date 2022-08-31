package com.namasake.food.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namasake.food.data.entity.Food
import com.namasake.food.databinding.FoodItemBinding

class FoodAdapter: ListAdapter<Food, FoodAdapter.FoodViewHolder>(FoodComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null){
            holder.bind(currentItem)

        }
    }

    class FoodViewHolder(private val binding: FoodItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food){
            binding.apply {
                tvName.text = food.name
                tvDescription.text = food.description
                Glide.with(itemView).load(food.imageUrl).into(imageLogo)
            }
        }


    }
    class FoodComparator: DiffUtil.ItemCallback<Food>(){
        override fun areItemsTheSame(oldItem: Food, newItem: Food) = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean =
                oldItem == newItem

    }


}