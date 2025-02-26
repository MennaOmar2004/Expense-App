package com.example.expensetracker.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    @Query("Delete From Expense_Table")
    suspend fun clearAllExpenses()

    @Query("Select SUM(price) From Expense_Table")
    fun getTotalExpenses():Flow<Double?>

    @Query("SELECT * FROM Expense_Table ORDER BY id ASC")
    fun getAllExpenses():Flow<List<ExpenseEntity>>

}