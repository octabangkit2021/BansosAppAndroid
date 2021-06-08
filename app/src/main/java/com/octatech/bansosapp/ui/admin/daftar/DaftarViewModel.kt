package com.octatech.bansosapp.ui.admin.daftar

import androidx.lifecycle.ViewModel
import com.octatech.bansosapp.core.domain.usecase.BansosUseCase

class DaftarViewModel(bansosUseCase: BansosUseCase) : ViewModel() {
    val listBansos =bansosUseCase.getAllBansos()
}