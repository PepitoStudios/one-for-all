package com.unatxe.quicklist.di

import android.content.Context
import com.unatxe.quicklist.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [DomainModuleDependencies::class])
interface TestComponent {

    fun inject(activity: MainActivity)
    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(
            // dataModuleDependencies: DataModuleDependencies,
            domainModuleDependencies: DomainModuleDependencies
        ): Builder
        fun build(): TestComponent
    }
}
