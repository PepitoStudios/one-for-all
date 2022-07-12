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
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Volatile
    private var INSTANCE: QuickListDatabase? = null

    fun getInstance(@ApplicationContext applicationContext: Context): QuickListDatabase =
        synchronized(this) {
            INSTANCE ?: buildDatabase(applicationContext).also { INSTANCE = it }
        }

    @Provides
    fun provideDatabase
    (@ApplicationContext applicationContext: Context): QuickListDatabase {
        return getInstance(applicationContext)
    }

    private fun buildDatabase(@ApplicationContext applicationContext: Context): QuickListDatabase {
        assert(Looper.getMainLooper() != Looper.myLooper())

        return Room.databaseBuilder(
            applicationContext,
            QuickListDatabase::class.java,
            "list_table"
        ).createFromAsset("list_table.db")
            /*.addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val handler = CoroutineExceptionHandler { _, exception ->
                        println("Caught during database creation --> $exception")
                    }
                    CoroutineScope(Dispatchers.IO).launch(handler) {
                        DatabasePopulation.onDatabaseIsEmpty(getInstance(applicationContext))
                    }
                }
            })*/
            .build().also {
                it.query("select 1", null)
            }
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
