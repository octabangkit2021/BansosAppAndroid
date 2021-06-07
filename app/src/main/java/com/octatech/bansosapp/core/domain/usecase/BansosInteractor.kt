package com.octatech.bansosapp.core.domain.usecase

import android.net.Uri
import androidx.lifecycle.LiveData
import com.octatech.bansosapp.core.data.Resource
import com.octatech.bansosapp.core.domain.model.OCR
import com.octatech.bansosapp.core.domain.repository.IBansosRepository

class BansosInteractor(private val bansosRepository: IBansosRepository): BansosUseCase {

    override fun getAllBansos() = bansosRepository.getAllBansos()
    override fun uploadGambar(Filename: String, uri: Uri) =
        bansosRepository.uploadGambar(Filename , uri)

    override fun getOCR(link : String) = bansosRepository.getOCR(link)


}