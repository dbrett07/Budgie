package com.dbrett.budgie.core.data.di

import com.dbrett.budgie.core.data.repository.AuthRepository
import com.dbrett.budgie.core.data.repository.AuthRepositoryImpl
import com.dbrett.budgie.core.data.repository.BudgetRepository
import com.dbrett.budgie.core.data.repository.BudgetRepositoryImpl
import com.dbrett.budgie.core.data.repository.TransactionRepository
import com.dbrett.budgie.core.data.repository.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindTransactionsRepository(
        impl : TransactionRepositoryImpl
    ) : TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        impl : BudgetRepositoryImpl
    ) : BudgetRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl : AuthRepositoryImpl
    ) : AuthRepository
}