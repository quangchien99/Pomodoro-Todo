package com.chpham.data.local.preferences

interface SharedPreferencesDataSource {

    fun insertCategory(category: String)

    fun getCategories(): List<String>
}
