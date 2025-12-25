package desarollodeplataformasii.nutriaxDBP.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserGoalDao {
    @Query("SELECT * FROM user_goals WHERE id = 1 LIMIT 1")
    fun getUserGoals(): Flow<UserGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserGoals(userGoal: UserGoalEntity)
}