package com.chpham.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * This is a data class called RemindOptions which defines various options for a reminder.
 * @property mode An enum property of type [RemindMode] which specifies the reminder mode. The default value is [RemindMode.UN_SPECIFIED].
 * @property interval An integer property which specifies the interval of the reminder. The default value is -1.
 * @property repeatInMonth An integer property which specifies the number of times the reminder should repeat in a month. The default value is -1.
 * @property repeatInWeek A list of string property which specifies the days of the week that the reminder should repeat. The default value is an empty list.
 * @property endInt An integer property which specifies the end time of the reminder. The default value is -1.
 * The [RemindMode] enum class specifies the different reminder modes, including:
 *  UN_SPECIFIED: This is the default value, and indicates that the reminder mode is not specified.
 *  DAILY: Indicates that the reminder should repeat daily.
 *  WEEKLY: Indicates that the reminder should repeat weekly.
 *  MONTHLY: Indicates that the reminder should repeat monthly.
 *
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Parcelize
data class RemindOptions(
    val mode: RemindMode = RemindMode.UN_SPECIFIED,
    val interval: Int = -1,
    val repeatInMonth: Int = -1,
    val repeatInWeek: List<String> = emptyList(),
    val endInt: Int = -1
) : Parcelable {
    enum class RemindMode {
        UN_SPECIFIED,
        DAILY,
        WEEKLY,
        MONTHLY
    }
}
