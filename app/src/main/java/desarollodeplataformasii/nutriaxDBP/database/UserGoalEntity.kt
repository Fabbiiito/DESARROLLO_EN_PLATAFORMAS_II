package desarollodeplataformasii.nutriaxDBP.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_goals")
data class UserGoalEntity(
    @PrimaryKey val id: Int = 1, // Single row for user goals
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)