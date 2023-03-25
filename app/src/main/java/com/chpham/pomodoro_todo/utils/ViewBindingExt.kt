package com.chpham.pomodoro_todo.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding

/**
 * @since March 15, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */

/**
 * This is an extension function on ViewBinding that creates an AlertDialog using the layout of the ViewBinding.
 * @param context The context to create the AlertDialog in.
 * @return An AlertDialog with the layout of the ViewBinding set as its view. The dialog is not dismissible by touching outside of it.
 */
fun ViewBinding.createAlertDialog(context: Context): AlertDialog {
    val dialogView = this.root
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setView(dialogView)
    val alertDialog = alertDialogBuilder.create()
    alertDialog.setCanceledOnTouchOutside(false)
    return alertDialog
}
