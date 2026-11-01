package com.dbrett.budgie.core.data.di

import android.content.Context
import androidx.room3.Room
import com.dbrett.budgie.core.data.source.local.BudgieDatabase
import com.dbrett.budgie.core.data.source.local.dao.BudgetDao
import com.dbrett.budgie.core.data.source.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context : Context
    ) : BudgieDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = BudgieDatabase::class.java,
            name = "budgie_app.db").build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database : BudgieDatabase) : TransactionDao {
        return database.transactionDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(database : BudgieDatabase) : BudgetDao {
        return database.budgetDao()
    }
}