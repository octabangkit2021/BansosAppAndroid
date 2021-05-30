package com.octatech.bansosapp.core.domain.repository

import androidx.lifecycle.LiveData
import com.octatech.bansosapp.core.data.Resource
import com.octatech.bansosapp.core.domain.model.Bansos

interface IBansosRepository {
    fun getAllBansos(): LiveData<Resource<List<Bansos>>>
}