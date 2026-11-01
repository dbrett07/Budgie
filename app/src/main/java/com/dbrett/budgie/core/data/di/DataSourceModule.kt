package com.dbrett.budgie.core.data.di

import com.dbrett.budgie.core.data.source.local.LocalBudgetDataSource
import com.dbrett.budgie.core.data.source.local.LocalBudgetDataSourceImpl
import com.dbrett.budgie.core.data.source.local.LocalTransactionDataSource
import com.dbrett.budgie.core.data.source.local.LocalTransactionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalTransactionDataSource(
        impl : LocalTransactionDataSourceImpl
    ) : LocalTransactionDataSource

    @Binds
    @Singleton
    abstract fun bindLocalBudgetDataSource(
        impl : LocalBudgetDataSourceImpl
    ) : LocalBudgetDataSource
}