package com.ps.personne.model

data class DonneesKyc(
    val idPersonne: IdPersonne,
    val update: UpdateInfo,
    val statutPPE: StatutPPE,
)

@JvmInline
value class IdPersonne(val id: Long)
