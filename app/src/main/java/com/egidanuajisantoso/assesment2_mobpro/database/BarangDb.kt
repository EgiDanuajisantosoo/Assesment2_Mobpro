package com.egidanuajisantoso.assesment2_mobpro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.egidanuajisantoso.assesment2_mobpro.model.Barang
import com.egidanuajisantoso.assesment2_mobpro.model.RecycleBarang


@Database(entities = [Barang::class, RecycleBarang::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun barangDao(): BarangDao
    abstract fun recycleBarangDao(): RecycleBarangDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "barang_database"
                ).fallbackToDestructiveMigration() // Untuk versi 2
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}