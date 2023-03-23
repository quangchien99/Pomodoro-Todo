package com.chpham.pomodoro_todo.todo.ui.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.ItemTaskBinding

/**
 * An adapter for a RecyclerView that displays a list of tasks.
 * @property taskClickListener The listener for click events on the tasks.
 * @property differ An AsyncListDiffer that calculates the differences between two lists.
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class TasksAdapter(
    private val taskClickListener: TaskClickListener
) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent an item.
     * @param parent The parent view that the new view will be attached to.
     * @param viewType The type of the new view.
     * @return A new [ViewHolder] that holds a view of the given type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

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
    override fun onBindViewHolder(holder: TasksAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    /**
     * A ViewHolder that holds the views for a task item.
     * @property binding The [ItemTaskBinding] that holds the views for a task item.
     */
    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the task data to the views.
         * @param task The task to bind to the views.
         */
        fun bind(task: Task) {
            if (task.state == TaskState.TO_DO || task.state == TaskState.IN_PROGRESS) {
                binding.imgTaskCheckBox.setOnClickListener {
                    taskClickListener.onTaskDoneClick(task)
                }
                binding.imgTaskCheckBox.setImageResource(R.drawable.ic_box_unchecked)
                binding.tvTaskName.text = task.name
            } else {
                binding.imgTaskCheckBox.setOnClickListener {
                    taskClickListener.onTaskDoingClick(task)
                }
                val spannableString = SpannableString(task.name)
                spannableString.setSpan(
                    StrikethroughSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.tvTaskName.text = spannableString
            }
            binding.tvTaskDate.text = task.deadline.toString()
            binding.cardViewTask.setOnClickListener {
                taskClickListener.onTaskClick(task.id, binding.cardViewTask)
            }
        }
    }

    companion object {
        /**
         * The [DiffUtil.ItemCallback] that determines if two tasks are the same and if their contents are the same.
         */
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
        }
    }

    /**
     * The listener for click events on the tasks.
     */
    interface TaskClickListener {
        fun onTaskClick(taskId: Int, card: CardView)
        fun onTaskDoneClick(task: Task)
        fun onTaskDoingClick(task: Task)
    }
}
