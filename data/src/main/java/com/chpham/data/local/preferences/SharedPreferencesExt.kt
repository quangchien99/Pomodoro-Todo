package com.chpham.data.local.preferences

import android.content.SharedPreferences

internal fun SharedPreferences.execute(operation: (SharedPreferences.Editor) -> Unit) {
    with(edit()) {
        operation(this)
        apply()
    }
}
