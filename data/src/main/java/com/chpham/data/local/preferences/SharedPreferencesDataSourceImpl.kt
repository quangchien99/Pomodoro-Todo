package com.chpham.data.local.preferences

import android.content.Context

class SharedPreferencesDataSourceImpl(context: Context) :
    BaseSharedPreferences(),
    SharedPreferencesDataSource {

    companion object {
        private const val PREFS_NAME = "pomodoro_todo_prefs"
        private const val PREF_CATEGORY_KEY = "category"
        private const val DEFAULT_CATEGORY = "All"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private var categories: String by preferences(PREF_CATEGORY_KEY, DEFAULT_CATEGORY)

    override fun insertCategory(category: String) {
        val currentCategories = getCategories().toMutableList()
        currentCategories.add(category)

        val delimiter = ","
        val serialized = currentCategories.joinToString(delimiter)

        this.categories = serialized
    }

    override fun getCategories(): List<String> {
        val delimiter = ","
        return categories.split(delimiter)
    }
}
