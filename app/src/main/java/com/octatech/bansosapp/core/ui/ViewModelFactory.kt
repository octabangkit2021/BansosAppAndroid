package com.octatech.bansosapp.core.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.octatech.bansosapp.core.di.Injection
import com.octatech.bansosapp.core.domain.usecase.BansosUseCase
import com.octatech.bansosapp.ui.RegisterBansos.RegisterViewModel
import com.octatech.bansosapp.ui.admin.daftar.DaftarViewModel
import com.octatech.bansosapp.ui.home.HomeViewModel

class ViewModelFactory private constructor(private val bansosUsecase: BansosUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance
                ?: synchronized(this) {
                    instance
                        ?: ViewModelFactory(
                            Injection.provideTourismUseCase(
                                context
                            )
                        )
                }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(bansosUsecase) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(bansosUsecase) as T
            }
            modelClass.isAssignableFrom(DaftarViewModel::class.java) -> {
                DaftarViewModel(bansosUsecase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}