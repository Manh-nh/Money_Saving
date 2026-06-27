package com.example.moneymanagement.presentation.database.roomdb

import android.content.Context
import androidx.room.Room
import com.example.moneymanagement.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DataManager {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDataBase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "data.db"
            ).fallbackToDestructiveMigration()
            .build()
            INSTANCE = instance
            
            // Tự động khởi tạo dữ liệu 6 chiếc hũ khi cơ sở dữ liệu được mở lần đầu
            initializeDefaultJars(instance)
            
            instance
        }
    }

    private fun initializeDefaultJars(database: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val count = database.addBudget().countJar()
                if (count == 0) {
                    val defaultJars = listOf(
                        BudgetEntity(1, "Necessities", 0, R.drawable.ic_necessities),
                        BudgetEntity(2, "Education", 0, R.drawable.ic_education_budget),
                        BudgetEntity(3, "Saving", 0, R.drawable.ic_saving_budget),
                        BudgetEntity(4, "Play", 0, R.drawable.ic_play_budget),
                        BudgetEntity(5, "Investment", 0, R.drawable.ic_investment_budget),
                        BudgetEntity(6, "Give", 0, R.drawable.ic_give_budget)
                    )
                    defaultJars.forEach { jar ->
                        database.addBudget().insertBudgetDetail(jar)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}