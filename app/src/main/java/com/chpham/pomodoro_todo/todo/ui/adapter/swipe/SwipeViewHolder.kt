package com.chpham.pomodoro_todo.todo.ui.adapter.swipe

interface SwipeViewHolder {

    /**
     * Return true if holder can be swiped, false otherwise
     */
    fun isScrollable(): Boolean

    /**
     * Called when user swipe view,
     * if dX > 0 than this is swiping to right
     * if dX < 0 than this is swiping to left
     * if dX == 0 than this is very firs swiping touch
     */
    fun onSwipe(dX: Float)

    /**
     * Called  when the user interaction with an element is over and it also completed its animation.
     */
    fun onSwipeComplete()

    /**
     * Reset view to initial state
     */
    fun resetView(animated: Boolean = false)
}
