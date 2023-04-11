package com.chpham.data.local.preferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class BaseSharedPreferences {
    lateinit var sharedPreferences: SharedPreferences

    inline fun <reified T> get(key: String): T? =
        if (sharedPreferences.contains(key)) {
            when (T::class) {
                Boolean::class -> sharedPreferences.getBoolean(key, false) as T?
                String::class -> sharedPreferences.getString(key, null) as T?
                Float::class -> sharedPreferences.getFloat(key, 0f) as T?
                Int::class -> sharedPreferences.getInt(key, 0) as T?
                Long::class -> sharedPreferences.getLong(key, 0L) as T?
                else -> null
            }
        } else {
            null
        }

    fun <T> set(key: String, value: T) {
        sharedPreferences.execute {
            when (value) {
                is Boolean -> it.putBoolean(key, value)
                is String -> it.putString(key, value)
                is Float -> it.putFloat(key, value)
                is Long -> it.putLong(key, value)
                is Int -> it.putInt(key, value)
            }
        }
    }

    inline fun <reified T> preferencesNullable(
        key: String,
        defaultValue: T? = null
    ): ReadWriteProperty<Any, T?> {
        return object : ReadWriteProperty<Any, T?> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = get(key) ?: defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: T?
            ) {
                set(key, value)
            }
        }
    }

    inline fun <reified T> preferences(
        key: String,
        defaultValue: T
    ): ReadWriteProperty<Any, T> {
        return object : ReadWriteProperty<Any, T> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = get(key) ?: defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: T
            ) {
                set(key, value)
            }
        }
    }

    protected fun remove(key: String) {
        sharedPreferences.execute { it.remove(key) }
    }

    protected fun clearAll() {
        sharedPreferences.execute { it.clear() }
    }
}
