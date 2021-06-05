package com.octatech.bansosapp.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OCR(
    var raw : String,
    var result : String
) : Parcelable