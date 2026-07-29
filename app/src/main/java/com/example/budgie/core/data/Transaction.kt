package com.example.budgie.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "transactions")
data class Transaction (
    @PrimaryKey(true) val id : Int,
    val timestamp : Instant,
    val source : String, // E.g. Business name
    val type : Int, // Transaction type, E.g. food, misc, etc
    val amount : Float
)