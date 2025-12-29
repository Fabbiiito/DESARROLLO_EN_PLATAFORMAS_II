package desarollodeplataformasii.nutriaxDBP.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val grams: Double,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val mealType: String, // "Desayuno", "Almuerzo", "Cena"
    val date: Long // Timestamp en milisegundos
)
