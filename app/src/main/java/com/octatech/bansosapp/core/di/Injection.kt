package com.octatech.bansosapp.core.di

import android.content.Context
import com.octatech.bansosapp.core.data.BansosRepository
import com.octatech.bansosapp.core.data.remote.RemoteDataSource
import com.octatech.bansosapp.core.data.source.LocalDataSource
import com.octatech.bansosapp.core.data.source.room.BansosDatabase
import com.octatech.bansosapp.core.domain.repository.IBansosRepository
import com.octatech.bansosapp.core.domain.usecase.BansosInteractor
import com.octatech.bansosapp.core.domain.usecase.BansosUseCase
import com.octatech.bansosapp.core.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): IBansosRepository {
        val database = BansosDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource()
        val localDataSource = LocalDataSource.getInstance(database.bansosDao())
        val appExecutors = AppExecutors()

        return BansosRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
    fun provideTourismUseCase(context: Context): BansosUseCase {
        val repository = provideRepository(context)
        return BansosInteractor(repository)
    }
}