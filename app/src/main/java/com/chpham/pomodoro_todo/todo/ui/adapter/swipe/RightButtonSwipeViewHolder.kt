package com.chpham.pomodoro_todo.todo.ui.adapter.swipe

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.utils.animateTranslationX
import com.chpham.pomodoro_todo.utils.toPx
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class RightButtonSwipeViewHolder(
    private val buttonView: ViewGroup,
    private val contentView: View,
    private val buttonMargin: Float = 0f,
    private val swipeBouncingWidth: Float = 16.toPx()
) : SwipeViewHolder {

    private var currentOffset = 0f
    private var buttonAnimator: ObjectAnimator? = null
    private var contentAnimator: ObjectAnimator? = null

    private val buttonSwipeLimit: Float get() = -buttonView.width - buttonMargin

    override fun isScrollable(): Boolean = buttonView.isVisible

    override fun resetView(animated: Boolean) {
        if (animated) {
            animateContentOffset(0f)
            animateButtonOffset(0f)
        } else {
            setContentOffset(0f)
            setButtonOffset(0f)
        }
    }

    override fun onSwipe(dX: Float) {

        if (dX == 0f) {
            currentOffset = contentView.translationX
        }

        val minOffset = buttonSwipeLimit
        val offset = getOffset(
            offset = currentOffset + dX,
            minOffset = minOffset
        )

        if (dX > 0) {
            if (contentView.translationX < minOffset) {
                setContentOffset(min(offset, minOffset))
            } else {
                setButtonOffset(offset)
                setContentOffset(offset)
            }
        } else {
            val bouncingOffset = getBouncingOffset(
                offset = currentOffset + dX,
                minOffset = minOffset
            )
            setButtonOffset(offset)
            setContentOffset(bouncingOffset)
        }
    }

    override fun onSwipeComplete() {
        buttonView.findViewById<ImageView>(R.id.swipeDelete).setOnClickListener {
            Log.e("ChienNgan", "swipeDelete check")
        }
        val minOffset = buttonSwipeLimit

        // autoscroll if reached 30% of min offset
        val autoscrollOffset = 0.3f * minOffset

        val contentOffset = contentView.translationX
        val offset = if (contentOffset + autoscrollOffset < minOffset) minOffset else 0f
        animateButtonOffset(offset)
        animateContentOffset(offset)
    }

    private fun setButtonOffset(offset: Float) {
        if (buttonAnimator?.isRunning == true) {
            buttonAnimator?.cancel()
        }

        if (buttonView.translationX != offset) {
            buttonView.translationX = offset
        }
    }

    private fun setContentOffset(offset: Float) {
        if (contentAnimator?.isRunning == true) {
            contentAnimator?.cancel()
        }

        if (contentView.translationX != offset) {
            contentView.translationX = offset
        }
    }

    private fun getOffset(minOffset: Float, offset: Float): Float = when {
        offset < minOffset -> minOffset
        offset > 0f -> 0f
        else -> offset
    }

    private fun getBouncingOffset(minOffset: Float, offset: Float): Float {
        val minBouncingOffset = minOffset - swipeBouncingWidth
        return when {
            offset > 0 -> 0f
            offset < minBouncingOffset -> minBouncingOffset

            offset < minOffset -> {
                val diff = offset - minOffset
                val decreaseFactor = interpolate(abs(diff) / swipeBouncingWidth)
                max(minBouncingOffset, minOffset + diff * decreaseFactor)
            }

            else -> offset
        }
    }

    private fun animateButtonOffset(offset: Float) {
        if (buttonAnimator?.isRunning == true) {
            buttonAnimator?.cancel()
        }

        buttonAnimator = buttonView.animateTranslationX(offset)
    }

    private fun animateContentOffset(offset: Float) {
        if (contentAnimator?.isRunning == true) {
            contentAnimator?.cancel()
        }

        contentAnimator = contentView.animateTranslationX(offset)
    }

    private fun interpolate(input: Float): Float = (1.0f - (1.0f - input) * (1.0f - input))
}
