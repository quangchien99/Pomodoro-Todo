package com.chpham.pomodoro_todo.todo.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chpham.pomodoro_todo.databinding.ItemCategoryBinding

/**
 * An adapter for a RecyclerView that displays a list of categories.
 * @property categoryClickListener The listener for click events on the category.
 * @property differ An AsyncListDiffer that calculates the differences between two lists.
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class CategoriesAdapter(private val categoryClickListener: CategoryClickListener) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var selectedCategory: String = "All"

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent an item.
     * @param parent The parent view that the new view will be attached to.
     * @param viewType The type of the new view.
     * @return A new [ViewHolder] that holds a view of the given type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The [ViewHolder] which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    /**
     * A ViewHolder that holds the views for a task item.
     * @property binding The [ItemCategoryBinding] that holds the views for a task item.
     */
    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the category data to the views.
         * @param category The task to bind to the views.
         */
        @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
        fun bind(category: String) {
            binding.tvCategory.text = category
            if (selectedCategory == category) {
                binding.cardViewItemCategory.setCardBackgroundColor(Color.BLUE)
                binding.tvCategory.setBackgroundColor(Color.BLUE)
            } else {
                binding.cardViewItemCategory.setCardBackgroundColor(Color.GRAY)
                binding.tvCategory.setBackgroundColor(Color.GRAY)
            }
            binding.cardViewItemCategory.setOnClickListener {
                categoryClickListener.onCategoryClick(category)
                selectedCategory = category
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        /**
         * The [DiffUtil.ItemCallback] that determines if two tasks are the same and if their contents are the same.
         */
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }

    /**
     * The listener for click events on the category.
     */
    interface CategoryClickListener {
        fun onCategoryClick(category: String)
    }
}
