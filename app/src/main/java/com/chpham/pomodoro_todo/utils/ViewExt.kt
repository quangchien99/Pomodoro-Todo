package com.chpham.pomodoro_todo.utils

import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import androidx.core.content.ContextCompat
import com.chpham.pomodoro_todo.R

/**
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */

/**
 * Rotates this view 360 degrees around its center point.
 * @return Unit
 */

fun View.rotateView() {
    val rotate = RotateAnimation(
        0f,
        360f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    rotate.duration = 1000
    rotate.repeatCount = Animation.ABSOLUTE
    this.startAnimation(rotate)
}

/**
 * Changes the background and text color of this Button based on the [isSelected] boolean flag.
 * @param isSelected Boolean flag that specifies whether the button is selected or not.
 * @return Unit
 */
fun Button.changeBackground(isSelected: Boolean) {
    if (isSelected) {
        background = ContextCompat.getDrawable(context, R.drawable.bg_btn_selected)
        setTextColor(Color.BLACK)
    } else {
        background = ContextCompat.getDrawable(context, R.drawable.bg_btn_unselected)
        setTextColor(Color.WHITE)
    }
}
