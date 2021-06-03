package com.octatech.bansosapp.ui.RegisterBansos

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.octatech.bansosapp.core.domain.usecase.BansosUseCase

class RegisterViewModel(private val bansosUseCase: BansosUseCase): ViewModel() {
    var nomorKTP : String = "";

    fun setKTP(nomorKTP : String){
        this.nomorKTP = nomorKTP
    }

    fun getKTP() = nomorKTP
}