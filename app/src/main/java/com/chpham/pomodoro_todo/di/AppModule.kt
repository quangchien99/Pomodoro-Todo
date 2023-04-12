package com.chpham.pomodoro_todo.di

import android.app.AlarmManager
import android.content.Context
import com.chpham.data.alarm.AlarmSchedulerImpl
import com.chpham.domain.alarm.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(
            AlarmManager::class.java,
        )
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager
    ): AlarmScheduler {
        return AlarmSchedulerImpl(context = context, alarmManager = alarmManager)
    }
}
