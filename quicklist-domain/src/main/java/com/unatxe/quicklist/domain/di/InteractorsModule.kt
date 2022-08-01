package com.unatxe.quicklist.domain.di

import com.unatxe.quicklist.domain.interactors.GetListUseCase
import com.unatxe.quicklist.domain.repository.QListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {
    @Provides
    fun provideGetListInteractor
    (listRepository: QListRepository): GetListUseCase {
        return GetListUseCase(listRepository)
    }
}
