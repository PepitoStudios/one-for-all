package com.unatxe.quicklist.data.di

import android.content.Context
import androidx.room.Room
import com.unatxe.quicklist.data.QuickListDatabase
import com.unatxe.quicklist.data.dao.QListDao
import com.unatxe.quicklist.data.repositories.ListRepositoryImpl
import com.unatxe.quicklist.domain.repository.QListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): QuickListDatabase {
        return Room.databaseBuilder(
            applicationContext,
            QuickListDatabase::class.java, "quick-list-database"
        ).build()
    }

    @Provides
    fun provideQListDao(quickListDatabase: QuickListDatabase): QListDao {
        return quickListDatabase.qListDao()
    }

    @Provides
    fun provideQListRepository(qListDao: QListDao): QListRepository {
        return ListRepositoryImpl(qListDao)
    }
}
