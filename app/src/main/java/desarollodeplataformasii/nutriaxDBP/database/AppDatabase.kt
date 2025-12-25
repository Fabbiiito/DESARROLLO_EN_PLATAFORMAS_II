package desarollodeplataformasii.nutriaxDBP.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MealEntity::class, UserGoalEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun userGoalDao(): UserGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutriax_database"
                )
                .fallbackToDestructiveMigration() // For development simplicity, wipes data on schema change
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}