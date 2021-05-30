package com.octatech.bansosapp.core.data.source.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.octatech.bansosapp.core.data.source.entity.BansosEntity

@Dao
interface BansosDao {

    @Query("SELECT * FROM bansoslist")
    fun getAllBansos(): LiveData<List<BansosEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBansos(bansos: List<BansosEntity>)
}