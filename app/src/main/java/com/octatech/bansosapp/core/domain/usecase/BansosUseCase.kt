package com.octatech.bansosapp.core.domain.usecase

import androidx.lifecycle.LiveData
import com.octatech.bansosapp.core.data.Resource
import com.octatech.bansosapp.core.domain.model.Bansos

interface BansosUseCase {
    fun getAllBansos(): LiveData<Resource<List<Bansos>>>
}