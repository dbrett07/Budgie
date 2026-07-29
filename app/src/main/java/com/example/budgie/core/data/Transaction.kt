package com.example.budgie.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction (
    @PrimaryKey(true) val id : Int,
    val timestamp : Int,
    val source : String,
    val amount : Float
)