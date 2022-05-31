package com.unatxe.quicklist.data.di

import com.unatxe.quicklist.domain.interactors.GetHelloWorldInteractor
import com.unatxe.quicklist.domain.repository.QListRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DataModuleDependencies {

    fun provideQListRepository(): QListRepository
}
