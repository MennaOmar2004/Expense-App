package com.example.expensetracker.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application):AndroidViewModel(application){

    val totalExpenses :Flow<Double?>
    val allExpenses :Flow<List<ExpenseEntity>>
    private val repository: ExpenseRepo

    init {
        val dao = ExpenseDataBase.getDataBase(application).expenseDao()
        repository = ExpenseRepo(dao)
        totalExpenses = repository.TotalExpenses
        allExpenses = repository.AllExpenses

    }

    fun insertExpense(expenseEntity: ExpenseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(expenseEntity)
        }
    }
    fun clearAllExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAll()
        }
    }


}