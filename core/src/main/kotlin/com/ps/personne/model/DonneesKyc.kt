package com.ps.personne.model

data class DonneesKyc(
    val idPersonne: IdPersonne,
    val update: UpdateInfo,
    val statutKyc: StatutKyc,
)

@JvmInline
value class IdPersonne(val id: Long)
