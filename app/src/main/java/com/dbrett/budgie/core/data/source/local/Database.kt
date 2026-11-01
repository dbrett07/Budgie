package com.dbrett.budgie.core.data.source.local

import androidx.room3.AutoMigration
import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.migration.Migration
import com.dbrett.budgie.core.data.source.local.dao.BudgetDao
import com.dbrett.budgie.core.data.source.local.dao.TransactionDao
import com.dbrett.budgie.core.data.source.local.entity.BudgetEntity
import com.dbrett.budgie.core.data.source.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class, BudgetEntity::class], version = 1)
@ColumnTypeConverters(Converters::class)
abstract class BudgieDatabase : RoomDatabase() {
    abstract fun transactionDao() : TransactionDao

    abstract fun budgetDao() : BudgetDao

    companion object {
        @Volatile
        private var INSTANCE : BudgieDatabase? = null

        fun getDatabase(context : android.content.Context) : BudgieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room3.Room.databaseBuilder<BudgieDatabase>(
                    context = context.applicationContext,
                    name = "budgie_database"
                )
                    .setDriver(androidx.sqlite.driver.AndroidSQLiteDriver())
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
