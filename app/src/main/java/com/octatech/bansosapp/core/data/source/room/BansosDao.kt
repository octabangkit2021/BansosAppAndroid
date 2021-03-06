package com.octatech.bansosapp.core.data.source.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.octatech.bansosapp.core.data.source.entity.BansosEntity
import com.octatech.bansosapp.core.data.source.entity.BansosTerdaftarEntity
import com.octatech.bansosapp.core.data.source.entity.OCREntity

@Dao
interface BansosDao {

    @Query("SELECT * FROM bansoslist")
    fun getAllBansos(): LiveData<List<BansosEntity>>

    @Query("SELECT * FROM bansosterdaftarlist")
    fun getAllBansosTerdaftar(): LiveData<List<BansosTerdaftarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBansos(bansos: List<BansosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBansosTerdaftar(bansosTerdaftar: List<BansosTerdaftarEntity>)

    @Query("SELECT * FROM ocrentities")
    fun getOCR(): LiveData<OCREntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOcr(ocr : OCREntity)
}