package desarollodeplataformasii.nutriaxDBP.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: MealEntity)
    
    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("SELECT * FROM meals WHERE date >= :startOfDay AND date < :endOfDay ORDER BY date DESC")
    fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals ORDER BY date DESC")
    fun getAllMeals(): Flow<List<MealEntity>>
}
