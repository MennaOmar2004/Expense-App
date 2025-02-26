package com.example.expensetracker.room

import kotlinx.coroutines.flow.Flow

class ExpenseRepo(private val expenseDao: ExpenseDao){

    val TotalExpenses : Flow<Double?> = expenseDao.getTotalExpenses()
    val AllExpenses : Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()


    suspend fun insert(expenseEntity: ExpenseEntity){
        expenseDao.insertExpense(expenseEntity)
    }

    suspend fun clearAll(){
        expenseDao.clearAllExpenses()
    }

}