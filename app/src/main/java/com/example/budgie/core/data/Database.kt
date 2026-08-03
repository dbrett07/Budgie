package com.example.budgie.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.budgie.core.data.source.local.TransactionDao
import com.example.budgie.core.data.source.local.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun transactionDao() : TransactionDao
}