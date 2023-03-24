package com.chpham.domain.model

data class RemindOptions(
    val mode: RemindMode = RemindMode.UN_SPECIFIED,
    val interval: Int = 1,
    val repeatInMonth: Int,
    val repeatInWeek: List<String>,
    val endInt: Int
) {
    enum class RemindMode {
        UN_SPECIFIED,
        DAILY,
        WEEKLY,
        MONTHLY
    }
}
