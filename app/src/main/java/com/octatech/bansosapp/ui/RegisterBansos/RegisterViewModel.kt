package com.octatech.bansosapp.ui.RegisterBansos

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.octatech.bansosapp.core.domain.usecase.BansosUseCase

class RegisterViewModel(private val bansosUseCase: BansosUseCase): ViewModel() {

    fun getOCR(link : String) = bansosUseCase.getOCR(link)
}