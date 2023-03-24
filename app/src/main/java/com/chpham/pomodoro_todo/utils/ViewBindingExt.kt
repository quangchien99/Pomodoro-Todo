package com.chpham.pomodoro_todo.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding

fun ViewBinding.createAlertDialog(context: Context): AlertDialog {
    val dialogView = this.root
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setView(dialogView)
    val alertDialog = alertDialogBuilder.create()
    alertDialog.setCanceledOnTouchOutside(false)
    return alertDialog
}
