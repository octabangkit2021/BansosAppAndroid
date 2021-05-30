package com.octatech.bansosapp.ui.home

import androidx.lifecycle.ViewModel
import com.octatech.bansosapp.core.domain.usecase.BansosUseCase

class HomeViewModel(bansosUseCase: BansosUseCase) : ViewModel() {
        val listBansos =bansosUseCase.getAllBansos()
}