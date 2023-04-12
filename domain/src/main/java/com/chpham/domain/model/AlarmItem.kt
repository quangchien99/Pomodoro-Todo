package com.chpham.domain.model

data class AlarmItem(
    val id: Int,
    val time: Long,
    val message: String,
    val remindOptions: RemindOptions? = null
)
