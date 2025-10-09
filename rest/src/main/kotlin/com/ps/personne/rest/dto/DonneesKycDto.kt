package com.ps.personne.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class DonneesKycDto(
    val idPersonne: Long,
    val updatedBy: String,
    val updatedAt: String,
    val statut: StatutPPEDto,
)

@Serializable
data class StatutPPEDto(
    val type: String, // STANDARD, PPE, PROCHE_PPE
    val vigilanceRenforcee: Boolean,
    val motif: String? = null,
    val fonction: String? = null,
    val dateFin: String? = null,
    val lienParente: String? = null,
)
