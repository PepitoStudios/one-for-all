package com.unatxe.quicklist.domain.di


import com.unatxe.quicklist.domain.interactors.GetHelloWorldInteractor
import com.unatxe.quicklist.domain.interactors.getHelloWorld
import com.unatxe.quicklist.domain.repository.QListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {

    @Provides
    fun provideHelloWorld(qListRepository: QListRepository): GetHelloWorldInteractor {
        return {
            getHelloWorld(qListRepository)
        }
    }


}
