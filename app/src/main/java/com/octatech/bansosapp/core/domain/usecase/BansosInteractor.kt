package com.octatech.bansosapp.core.domain.usecase

import android.net.Uri
import com.octatech.bansosapp.core.domain.repository.IBansosRepository

class BansosInteractor(private val bansosRepository: IBansosRepository): BansosUseCase {

    override fun getAllBansos() = bansosRepository.getAllBansos()
    override fun uploadGambar(Filename: String, uri: Uri) =
        bansosRepository.uploadGambar(Filename , uri)


}