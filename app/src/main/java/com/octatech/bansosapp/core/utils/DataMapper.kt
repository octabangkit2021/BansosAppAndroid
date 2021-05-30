package com.octatech.bansosapp.core.utils

import com.octatech.bansosapp.core.data.remote.response.BansosResponse
import com.octatech.bansosapp.core.data.source.entity.BansosEntity
import com.octatech.bansosapp.core.domain.model.Bansos

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
}