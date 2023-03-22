package com.chpham.domain.model

data class RemindOptions(
    val mode: RemindMode = RemindMode.UN_SPECIFIED,
    val interval: Int = 1,
    val repeatIn: Int,
    val endInt: Int
) {
    enum class RemindMode {
        UN_SPECIFIED,
        DAILY,
        WEEKLY,
        MONTHLY
    }
}
