package com.chpham.domain.alarm

import com.chpham.domain.model.AlarmItem

interface AlarmScheduler {

    fun schedule(item: AlarmItem, isUpdate: Boolean = false)

    fun cancel(item: AlarmItem)
}
