package com.chpham.domain.interactor.params

data class GetTaskByDayAndCategoryParams(
    val day: Long,
    val category: String? = null
)
