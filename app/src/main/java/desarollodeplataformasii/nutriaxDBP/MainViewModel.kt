// MainViewModel.kt
package desarollodeplataformasii.nutriaxDBP

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import desarollodeplataformasii.nutriaxDBP.database.AppDatabase
import desarollodeplataformasii.nutriaxDBP.database.MealDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val mealDao: MealDao) : ViewModel() {
    private val _userGoals = MutableStateFlow(UserGoals())
    val userGoals = _userGoals.asStateFlow()

    fun updateGoals(newGoals: UserGoals) {
        _userGoals.value = newGoals
    }

    fun insertMeal(meal: desarollodeplataformasii.nutriaxDBP.database.MealEntity) {
        viewModelScope.launch {
            mealDao.insertMeal(meal)
        }
    }

    fun deleteMeal(meal: desarollodeplataformasii.nutriaxDBP.database.MealEntity) {
        viewModelScope.launch {
            mealDao.deleteMeal(meal)
        }
    }

    fun getMealsForDay(start: Long, end: Long): Flow<List<desarollodeplataformasii.nutriaxDBP.database.MealEntity>> {
        return mealDao.getMealsForDay(start, end)
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val db = AppDatabase.getDatabase(context)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(db.mealDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}