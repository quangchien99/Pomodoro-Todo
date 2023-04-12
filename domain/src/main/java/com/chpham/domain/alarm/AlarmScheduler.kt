package com.chpham.domain.alarm

import com.chpham.domain.model.AlarmItem

interface AlarmScheduler {

    fun schedule(item: AlarmItem)

    fun cancel(item: AlarmItem)
}
