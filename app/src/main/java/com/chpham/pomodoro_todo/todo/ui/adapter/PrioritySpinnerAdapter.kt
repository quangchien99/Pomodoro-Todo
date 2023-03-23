package com.chpham.pomodoro_todo.todo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.chpham.pomodoro_todo.R

class PrioritySpinnerAdapter(
    context: Context,
    objects: Array<String>,
    imageArray: Array<Int>
) : ArrayAdapter<String?>(
    context,
    R.layout.item_spinner,
    R.id.imgSpinnerItemText,
    objects
) {

    private val ctx: Context
    private val contentArray: Array<String>
    private val imageArray: Array<Int>

    init {
        ctx = context
        contentArray = objects
        this.imageArray = imageArray
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row: View = inflater.inflate(R.layout.item_spinner, parent, false)
        val textView = row.findViewById(R.id.imgSpinnerItemText) as TextView
        textView.text = contentArray[position]
        val imageView: ImageView = row.findViewById(R.id.imgSpinnerItemIcon) as ImageView
        imageView.setImageResource(imageArray[position])
        return row
    }
}
