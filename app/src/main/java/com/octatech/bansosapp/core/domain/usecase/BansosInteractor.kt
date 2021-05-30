package com.octatech.bansosapp.core.domain.usecase

import com.octatech.bansosapp.core.domain.repository.IBansosRepository

class BansosInteractor(private val bansosRepository: IBansosRepository): BansosUseCase {

    override fun getAllBansos() = bansosRepository.getAllBansos()

}