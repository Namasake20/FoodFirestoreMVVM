package com.namasake.food.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namasake.food.data.entity.Food
import com.namasake.food.databinding.FoodItemBinding

class FoodAdapter(private val itemClickListener: OnFoodClickListener): ListAdapter<Food, FoodAdapter.FoodViewHolder>(FoodComparator()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val holder = FoodViewHolder(binding)
        binding.root.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            itemClickListener.onFoodClick(foodList[position], position)
        }

        return holder
    }

    private var foodList = listOf<Food>()

    interface OnFoodClickListener{
        fun onFoodClick(food: Food,position: Int)
    }

    fun setFoodList(foodList: List<Food>){
        this.foodList = foodList
        notifyDataSetChanged()
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

    override fun getItemCount(): Int = foodList.size

    class FoodComparator: DiffUtil.ItemCallback<Food>(){
        override fun areItemsTheSame(oldItem: Food, newItem: Food) = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean =
                oldItem == newItem

    }


}