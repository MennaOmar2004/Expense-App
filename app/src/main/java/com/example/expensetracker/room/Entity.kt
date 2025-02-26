package com.example.expensetracker.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Expense_Table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val thing : String,
    val price :Int,
    val date : String
)
