package com.chpham.domain.model

data class RemindOptions(
    val mode: RemindMode = RemindMode.DAILY,
    val interval: Int = 1,
    val repeatIn: Int,
    val endInt: Int
) {
    enum class RemindMode {
        DAILY,
        WEEKLY,
        MONTHLY
    }
}
