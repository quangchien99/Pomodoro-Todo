package com.chpham.pomodoro_todo.di

import android.app.Application
import androidx.room.Room
import com.chpham.data.database.PomotodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that provides instances of database and its DAOs using Hilt dependency injection.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides an instance of [PomotodoDatabase] using the [Room.databaseBuilder] method.
     * The database name is set to "PomotodoDatabase.db".
     * @param application the application context used to build the database.
     * @return an instance of [PomotodoDatabase].
     */
    @Singleton
    @Provides
    fun providePomotodoDatabase(application: Application) =
        Room.databaseBuilder(
            application,
            PomotodoDatabase::class.java,
            "PomotodoDatabase.db"
        ).build()

    /**
     * Provides an instance of [TaskDao] by getting it from the [PomotodoDatabase].
     * @param pomotodoDatabase the [PomotodoDatabase] instance.
     * @return an instance of [TaskDao].
     */
    @Provides
    fun provideTaskDao(pomotodoDatabase: PomotodoDatabase) = pomotodoDatabase.taskDao()
}
