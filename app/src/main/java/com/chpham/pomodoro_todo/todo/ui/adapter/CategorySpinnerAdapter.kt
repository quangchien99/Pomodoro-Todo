package com.chpham.pomodoro_todo.todo.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chpham.pomodoro_todo.R

class CategorySpinnerAdapter(context: Context, private val items: MutableList<String>) :
    ArrayAdapter<String>(context, R.layout.item_spinner, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(parent, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(parent, position)
    }

    private fun getCustomView(parent: ViewGroup, position: Int): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row: View = inflater.inflate(R.layout.item_spinner, parent, false)
        val textView = row.findViewById(R.id.imgSpinnerItemText) as TextView
        textView.text = items[position]
        val imageView: ImageView = row.findViewById(R.id.imgSpinnerItemIcon) as ImageView
        when (position) {
            count - 1 -> {
                textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.chpham.domain.R.color.blue
                    )
                )
                imageView.setImageResource(R.drawable.ic_add_label)
            }
            0 -> {
                imageView.setImageResource(R.drawable.ic_none_category)
            }
            else -> {
                imageView.setImageResource(R.drawable.ic_label)
            }
        }
        return row
    }

    fun addItem(item: String) {
        items.add(items.size - 1, item)
        notifyDataSetChanged()
    }
}
