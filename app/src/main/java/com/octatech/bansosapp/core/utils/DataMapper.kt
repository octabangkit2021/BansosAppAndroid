package com.octatech.bansosapp.core.utils

import com.octatech.bansosapp.core.data.remote.response.BansosResponse
import com.octatech.bansosapp.core.data.remote.response.OCRResponse
import com.octatech.bansosapp.core.data.source.entity.BansosEntity
import com.octatech.bansosapp.core.data.source.entity.OCREntity
import com.octatech.bansosapp.core.domain.model.Bansos
import com.octatech.bansosapp.core.domain.model.OCR

object DataMapper {
    fun mapResponsesToEntities(input: List<BansosResponse>): List<BansosEntity> {
        val bansosList = ArrayList<BansosEntity>()
        input.map {
            val bansos = BansosEntity(
                bansosId = it.bansosId,
                bansosName = it.bansosName,
                bansosDeskripsi = it.bansosDeskripsi,
                bansosBerlaku = it.bansosBerlaku,
                bansosPersyaratan = it.bansosPersyaratan,
                bansosGambar = it.bansosGambar,
                bansosIsi = it.bansosIsi
            )
            bansosList.add(bansos)
        }
        return bansosList
    }
    fun mapEntitiesToDomain(input: List<BansosEntity>): List<Bansos> =
        input.map {
            Bansos(
                bansosId = it.bansosId,
                bansosName = it.bansosName,
                bansosDeskripsi = it.bansosDeskripsi,
                bansosBerlaku = it.bansosBerlaku,
                bansosPersyaratan = it.bansosPersyaratan,
                bansosGambar = it.bansosGambar,
                bansosIsi = it.bansosIsi
            )
        }
    fun mapDomainToEntity(bansos: Bansos) = BansosEntity(
        bansosId = bansos.bansosId,
        bansosName = bansos.bansosName,
        bansosDeskripsi = bansos.bansosDeskripsi,
        bansosBerlaku = bansos.bansosBerlaku,
        bansosPersyaratan = bansos.bansosPersyaratan,
        bansosGambar = bansos.bansosGambar,
        bansosIsi = bansos.bansosIsi
    )

    fun mapResponsesOcrToEntities(input: OCRResponse): OCREntity {
            val ocr = OCREntity(
               raw = input.raw,
                result = input.result
            )
        return ocr
    }
    fun mapEntitiesOcrToDomain(input: OCREntity): OCR =
            OCR(
              raw = input.raw,
                result = input.result
            )
    fun mapDomainOcrToEntity(ocr: OCR) = OCREntity(
        raw = ocr.raw,
        result = ocr.result
    )
}