package com.unatxe.quicklist.data.di

import android.content.Context
import android.os.Looper
import androidx.room.Room
import com.unatxe.quicklist.data.QuickListDatabase
import com.unatxe.quicklist.data.dao.QListDao
import com.unatxe.quicklist.data.repositories.ListRepositoryImpl
import com.unatxe.quicklist.domain.repository.QListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Provider
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabaseFake
    (@ApplicationContext applicationContext: Context): QuickListDatabase {
        return Room.inMemoryDatabaseBuilder(
            applicationContext,
            QuickListDatabase::class.java
        ).build()
    }

    @Provides
    fun provideQListDao(quickListDatabase: Provider<QuickListDatabase>): QListDao {
        assert(Looper.getMainLooper() != Looper.myLooper())

        return quickListDatabase.get().qListDao()
    }

    @Provides
    fun provideQListRepository(qListDao: Provider<QListDao>): QListRepository {
        return ListRepositoryImpl(qListDao::get)
    }
}
