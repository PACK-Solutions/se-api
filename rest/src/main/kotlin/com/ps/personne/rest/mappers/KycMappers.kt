package com.ps.personne.rest.mappers

import com.ps.personne.model.*
import com.ps.personne.rest.dto.DonneesKycDto
import com.ps.personne.rest.dto.StatutPPEDto

fun DonneesKyc.toDto(): DonneesKycDto = DonneesKycDto(
    idPersonne = this.idPersonne.id,
    updatedBy = this.update.user.login,
    updatedAt = this.update.date.toString(),
    statut = this.statutKyc.toDto()
)

private fun StatutKyc.toDto(): StatutPPEDto = when (this) {
    is StatutKyc.Standard -> StatutPPEDto(
        type = "STANDARD",
        vigilanceRenforcee = vigilance.vigilanceRenforcee,
        motif = (vigilance as? AvecVigilanceRenforcee)?.motif?.name
    )
    is StatutKyc.Ppe -> StatutPPEDto(
        type = "PPE",
        vigilanceRenforcee = vigilance.vigilanceRenforcee,
        motif = vigilance.motif.name,
        fonction = mandat.fonction.name,
        dateFin = mandat.dateFin?.toString()
    )
    is StatutKyc.ProchePpe -> StatutPPEDto(
        type = "PROCHE_PPE",
        vigilanceRenforcee = vigilance.vigilanceRenforcee,
        motif = (vigilance as? AvecVigilanceRenforcee)?.motif?.name,
        lienParente = lienParente.name,
    )
}
