package com.example.zd7_v5_yungman


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zd7_v5_yungman.dao.BusDao
import com.example.zd7_v5_yungman.dao.DriverDao
import com.example.zd7_v5_yungman.dao.RouteDao
import com.example.zd7_v5_yungman.dao.DriverRouteDao

@Database(
    entities = [
        Bus::class,
        Driver::class,
        Route::class,
        DriverRouteCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun busDao(): BusDao
    abstract fun driverDao(): DriverDao
    abstract fun routeDao(): RouteDao
    abstract fun driverRouteDao(): DriverRouteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bus_dispatcher.db"
                )
                    .fallbackToDestructiveMigration() // Только для разработки!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}