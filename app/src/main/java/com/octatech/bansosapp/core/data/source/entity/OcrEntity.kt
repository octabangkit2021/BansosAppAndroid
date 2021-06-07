package com.octatech.bansosapp.core.data.source.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "OCREntities")
data class OCREntity(
    @PrimaryKey
    @ColumnInfo(name = "raw")
    var raw :String,

    @ColumnInfo(name = "result")
    var result : String,
) : Parcelable