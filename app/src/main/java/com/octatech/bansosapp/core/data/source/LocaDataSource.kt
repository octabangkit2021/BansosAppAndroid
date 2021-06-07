package com.octatech.bansosapp.core.data.source

import androidx.lifecycle.LiveData
import com.octatech.bansosapp.core.data.source.entity.BansosEntity
import com.octatech.bansosapp.core.data.source.entity.OCREntity
import com.octatech.bansosapp.core.data.source.room.BansosDao

class LocalDataSource private constructor(private val bansosDao: BansosDao) {

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(bansosDao: BansosDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(bansosDao)
            }
    }

    fun getAllBansos(): LiveData<List<BansosEntity>> = bansosDao.getAllBansos()
    fun insertbansos(bansosList: List<BansosEntity>) = bansosDao.insertBansos(bansosList)

    fun getOcr() : LiveData<OCREntity> = bansosDao.getOCR()
    fun insertOCr(ocr : OCREntity) = bansosDao.insertOcr(ocr)
}