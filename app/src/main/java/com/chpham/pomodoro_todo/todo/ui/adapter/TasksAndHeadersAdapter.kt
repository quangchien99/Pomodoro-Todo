package com.chpham.pomodoro_todo.todo.ui.adapter

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskPriority
import com.chpham.domain.model.TaskState
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.ItemHeaderBinding
import com.chpham.pomodoro_todo.databinding.ItemTaskBinding
import com.chpham.pomodoro_todo.todo.ui.adapter.swipe.RightButtonSwipeViewHolder
import com.chpham.pomodoro_todo.todo.ui.adapter.swipe.SwipeViewHolder
import com.chpham.pomodoro_todo.utils.Constants
import com.chpham.pomodoro_todo.utils.dpTtoPx
import com.chpham.pomodoro_todo.utils.toDayMonthYearString
import com.chpham.pomodoro_todo.utils.toPx

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
class TasksAndHeadersAdapter(
    private val context: Context,
    private val taskClickListener: TaskClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent an item.
     * @param parent The parent view that the new view will be attached to.
     * @param viewType The type of the new view.
     * @return A new [ViewHolder] that holds a view of the given type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_TASK -> {
                TaskViewHolder(
                    ItemTaskBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
            else -> {
                HeaderViewHolder(
                    ItemHeaderBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].first != null) {
            VIEW_TYPE_TASK
        } else {
            VIEW_TYPE_HEADER
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The [ViewHolder] which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> differ.currentList[position].first?.let { holder.bind(it) }
            is HeaderViewHolder -> differ.currentList[position].second?.let { holder.bind(it) }
        }
    }

    /**
     * A ViewHolder that holds the views for a task item.
     * @property binding The [ItemTaskBinding] that holds the views for a task item.
     */
    inner class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val swipeViewHolderDelegate: SwipeViewHolder = RightButtonSwipeViewHolder(
            contentView = binding.itemContent,
            buttonView = binding.layoutButtons,
            buttonMargin = binding.root.dpTtoPx(0f),
            swipeBouncingWidth = 24.toPx()
        )
    ) :
        RecyclerView.ViewHolder(binding.root), SwipeViewHolder by swipeViewHolderDelegate {

        private var lastItemUid: Int? = null

        /**
         * Binds the task data to the views.
         * @param task The task to bind to the views.
         */
        fun bind(task: Task) {
            resetView(animated = task.id == lastItemUid)
            lastItemUid = task.id
            if (task.state == TaskState.TO_DO || task.state == TaskState.IN_PROGRESS) {
                binding.imgTaskCheckBox.setOnClickListener {
                    taskClickListener.onTaskDoneClick(task.id)
                }
                binding.imgTaskCheckBox.setImageResource(R.drawable.ic_box_unchecked)
                binding.tvTaskName.text = task.name
            } else {
                binding.imgTaskCheckBox.setImageResource(R.drawable.ic_box_checked)
                val spannableString = SpannableString(task.name)
                spannableString.setSpan(
                    StrikethroughSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.tvTaskName.text = spannableString
            }
            if (!task.category.isNullOrEmpty()) {
                binding.tvCategory.text = task.category
                binding.tvCategory.visibility = View.VISIBLE
            } else {
                binding.tvCategory.visibility = View.GONE
            }
            if (task.remindOptions?.mode != RemindOptions.RemindMode.UN_SPECIFIED) {
                binding.imgRepeat.visibility = View.VISIBLE
            }
            if (!task.description.isNullOrEmpty()) {
                binding.tvTaskDescription.text = task.description
                binding.tvTaskDescription.visibility = View.VISIBLE
            } else {
                binding.tvTaskDescription.visibility = View.GONE
            }
            when (task.priority) {
                TaskPriority.LOW -> {
                    binding.imgPriority.setImageResource(R.drawable.ic_priority_low)
                    binding.imgPriority.visibility = View.VISIBLE
                }
                TaskPriority.MEDIUM -> {
                    binding.imgPriority.setImageResource(R.drawable.ic_priority_medium)
                    binding.imgPriority.visibility = View.VISIBLE
                }
                TaskPriority.HIGH -> {
                    binding.imgPriority.setImageResource(R.drawable.ic_priority_high)
                    binding.imgPriority.visibility = View.VISIBLE
                }
                else -> {
                    binding.imgPriority.visibility = View.GONE
                }
            }
            if (task.remindBefore != 0 && task.deadline != 0L) {
                binding.imgRemind.visibility = View.VISIBLE
            } else {
                binding.imgRemind.visibility = View.GONE
            }
            binding.tvTaskDate.text = task.dueDate.toDayMonthYearString()
            binding.swipeDelete.setOnClickListener {
                taskClickListener.onRemoveTaskClicked(this, task)
            }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: String) {
            when (header) {
                Constants.HEADER_TODO -> {
                    binding.cardViewItemHeader.setCardBackgroundColor(
                        context.resources.getColor(
                            com.chpham.domain.R.color.gray,
                            null
                        )
                    )
                    binding.tvHeader.text = Constants.HEADER_TODO
                    binding.tvContent.visibility = View.GONE
                }
                Constants.HEADER_IN_PROGRESS -> {
                    binding.cardViewItemHeader.setCardBackgroundColor(
                        context.resources.getColor(
                            com.chpham.domain.R.color.greenDark,
                            null
                        )
                    )
                    binding.tvHeader.text = Constants.HEADER_IN_PROGRESS
                    binding.tvContent.text = context.resources.getString(
                        R.string.text_header_content,
                        Constants.HEADER_IN_PROGRESS
                    )
                }
                Constants.HEADER_DONE -> {
                    binding.cardViewItemHeader.setCardBackgroundColor(
                        context.resources.getColor(
                            com.chpham.domain.R.color.redBeta,
                            null
                        )
                    )
                    binding.tvHeader.text = Constants.HEADER_DONE
                    binding.tvContent.text = context.resources.getString(
                        R.string.text_header_content,
                        Constants.HEADER_DONE
                    )
                }
            }
        }
    }

    companion object {
        /**
         * The [DiffUtil.ItemCallback] that determines if two tasks are the same and if their contents are the same.
         */
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Pair<Task?, String?>>() {

            override fun areItemsTheSame(
                oldItem: Pair<Task?, String?>,
                newItem: Pair<Task?, String?>
            ): Boolean {
                return if (oldItem.first != null && newItem.first != null) {
                    oldItem.first!!.id == newItem.first!!.id
                } else if (oldItem.second != null && newItem.second != null) {
                    oldItem.second == newItem.second
                } else false
            }

            override fun areContentsTheSame(
                oldItem: Pair<Task?, String?>,
                newItem: Pair<Task?, String?>
            ): Boolean {
                return if (oldItem.first != null && newItem.first != null) {
                    oldItem.first == newItem.first
                } else if (oldItem.second != null && newItem.second != null) {
                    oldItem.second == newItem.second
                } else false
            }
        }

        const val VIEW_TYPE_TASK = 1
        const val VIEW_TYPE_HEADER = 2
    }

    /**
     * The listener for click events on the tasks.
     */
    interface TaskClickListener {
        fun onTaskDoneClick(taskId: Int)
        fun onRemoveTaskClicked(holder: TaskViewHolder, task: Task)
        fun onEditTaskClicked(holder: TaskViewHolder, task: Task)
    }
}
