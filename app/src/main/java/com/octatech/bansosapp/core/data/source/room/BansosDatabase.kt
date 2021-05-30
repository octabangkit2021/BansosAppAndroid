package com.octatech.bansosapp.core.data.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.octatech.bansosapp.core.data.source.entity.BansosEntity

@Database(entities = [BansosEntity::class], version = 1, exportSchema = false)
abstract class BansosDatabase : RoomDatabase() {

    abstract fun bansosDao(): BansosDao

    companion object {
        @Volatile
        private var INSTANCE: BansosDatabase? = null

        fun getInstance(context: Context): BansosDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BansosDatabase::class.java,
                    "Tourism.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
    }
}