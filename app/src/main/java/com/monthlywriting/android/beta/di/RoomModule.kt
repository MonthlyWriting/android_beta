package com.monthlywriting.android.beta.di

import android.content.Context
import androidx.room.Room
import com.monthlywriting.android.beta.repository.GoalRepository
import com.monthlywriting.android.beta.repository.WritingRepository
import com.monthlywriting.android.beta.room.AppDataBase
import com.monthlywriting.android.beta.room.MonthlyGoalDao
import com.monthlywriting.android.beta.room.MonthlyWritingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "monthly_goal").build()
    }

    @Provides
    @Singleton
    fun provideMonthlyGoalDao(appDatabase: AppDataBase): MonthlyGoalDao {
        return appDatabase.monthlyGoalDao()
    }

    @Provides
    @Singleton
    fun provideGoalRepository(monthlyGoalDao: MonthlyGoalDao): GoalRepository {
        return GoalRepository(monthlyGoalDao)
    }

    @Provides
    @Singleton
    fun provideMonthlyWritingDao(appDatabase: AppDataBase): MonthlyWritingDao {
        return appDatabase.monthlyWritingDao()
    }

    @Provides
    @Singleton
    fun provideMonthlyWritingRepository(monthlyWritingDao: MonthlyWritingDao): WritingRepository {
        return WritingRepository(monthlyWritingDao)
    }
}