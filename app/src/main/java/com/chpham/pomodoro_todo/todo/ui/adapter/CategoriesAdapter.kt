package com.chpham.pomodoro_todo.todo.ui.adapter

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
        fun bind(category: String) {
            binding.tvCategory.text = category
            binding.cardViewItemCategory.setOnClickListener {
                categoryClickListener.onCategoryClick(category)
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