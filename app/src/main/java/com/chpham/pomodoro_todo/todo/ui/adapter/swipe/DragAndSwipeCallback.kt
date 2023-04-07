package com.chpham.pomodoro_todo.todo.ui.adapter.swipe

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DragAndSwipeCallback(
    private val listener: ItemTouchHelperListener
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private var activeHolder: SwipeViewHolder? = null

    interface ItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = when {
        viewHolder !is SwipeViewHolder -> ItemTouchHelper.ACTION_STATE_IDLE
        viewHolder.isScrollable() -> super.getMovementFlags(recyclerView, viewHolder)
        else -> ItemTouchHelper.ACTION_STATE_IDLE
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        viewHolder.itemView.elevation = 16F
        return listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 1f // prevent item swiped
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Float.MAX_VALUE
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // onSwipe allowed only for active ACTION_STATE_SWIPE
            if (!isCurrentlyActive) return

            // onSwipe allowed only for SwipeViewHolder
            if (viewHolder !is SwipeViewHolder) return

            if (viewHolder != activeHolder) {
                activeHolder?.resetView(animated = true)
                activeHolder = viewHolder
            }

            viewHolder.onSwipe(dX)
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is SwipeViewHolder) viewHolder.onSwipeComplete()
    }
}
