package com.octatech.bansosapp.ui.home

import androidx.lifecycle.ViewModel
import com.octatech.bansosapp.core.domain.usecase.BansosUseCase

class HomeViewModel(private val bansosUseCase: BansosUseCase) : ViewModel() {
        val listBansos =bansosUseCase.getAllBansos()
        fun listBansosTerdaftar(nomor_ktp : String) = bansosUseCase.getBansosTerdaftar(nomor_ktp)
}