package com.unatxe.quicklist.di

import com.unatxe.quicklist.domain.interactors.GetHelloWorldInteractor
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DomainModuleDependencies {

    fun provideHelloWorldInteractor(): GetHelloWorldInteractor
}
