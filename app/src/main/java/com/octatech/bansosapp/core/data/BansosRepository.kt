package com.octatech.bansosapp.core.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.octatech.bansosapp.core.data.remote.RemoteDataSource
import com.octatech.bansosapp.core.data.remote.response.ApiResponse
import com.octatech.bansosapp.core.data.remote.response.BansosResponse
import com.octatech.bansosapp.core.data.remote.response.OCRResponse
import com.octatech.bansosapp.core.data.remote.response.OCRSendModel
import com.octatech.bansosapp.core.data.source.LocalDataSource
import com.octatech.bansosapp.core.domain.model.Bansos
import com.octatech.bansosapp.core.domain.model.OCR
import com.octatech.bansosapp.core.domain.repository.IBansosRepository
import com.octatech.bansosapp.core.utils.AppExecutors
import com.octatech.bansosapp.core.utils.DataMapper

class BansosRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IBansosRepository {

    companion object {
        @Volatile
        private var instance: BansosRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): BansosRepository =
            instance ?: synchronized(this) {
                instance ?: BansosRepository(remoteData, localData, appExecutors)
            }
    }

    override fun getAllBansos(): LiveData<Resource<List<Bansos>>> =
        object : NetworkBoundResource<List<Bansos>, List<BansosResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<Bansos>> {
                return Transformations.map(localDataSource.getAllBansos()){
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Bansos>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<BansosResponse>>> =
                remoteDataSource.getAllBansos()

            override fun saveCallResult(data: List<BansosResponse>) {
                Log.d("TRAPPING", "saveCallResult: " + data)
                val bansosList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertbansos(bansosList)
            }
        }.asLiveData()

    override fun uploadGambar(fileName: String, uri: Uri) =
        appExecutors.diskIO().execute { remoteDataSource.uploadPhoto(fileName, uri) }

    override fun getOCR(link : String): LiveData<Resource<OCR>> =
        object : NetworkBoundResource<OCR, OCRResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<OCR> {
                return Transformations.map(localDataSource.getOcr()){
                    DataMapper.mapEntitiesOcrToDomain(it)
                }
            }

            override fun shouldFetch(data:OCR?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<OCRResponse>> =
                remoteDataSource.getOCR(link)

            override fun saveCallResult(data: OCRResponse) {
                Log.d("TRAPPING", "saveCallResult: " + data)
                val ocr = DataMapper.mapResponsesOcrToEntities(data)
                localDataSource.insertOCr(ocr)
            }
        }.asLiveData()

}