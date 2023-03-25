package com.chpham.domain.model

data class RemindOptions(
    val mode: RemindMode = RemindMode.UN_SPECIFIED,
    val interval: Int = -1,
    val repeatInMonth: Int = -1,
    val repeatInWeek: List<String> = emptyList(),
    val endInt: Int = -1
) {
    enum class RemindMode {
        UN_SPECIFIED,
        DAILY,
        WEEKLY,
        MONTHLY
    }
}
