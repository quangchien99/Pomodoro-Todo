package com.chpham.pomodoro_todo.todo.ui.adapter.swipe

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeCallback : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private var activeHolder: SwipeViewHolder? = null

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
        return true
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
        // onSwipe allowed only for active ACTION_STATE_SWIPE
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE || !isCurrentlyActive) return

        // onSwipe allowed only for SwipeViewHolder
        if (viewHolder !is SwipeViewHolder) return

        if (viewHolder != activeHolder) {
            activeHolder?.resetView(animated = true)
            activeHolder = viewHolder
        }

        viewHolder.onSwipe(dX)
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is SwipeViewHolder) viewHolder.onSwipeComplete()
    }
}
