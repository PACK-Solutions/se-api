package com.ps.personne.rest.mappers

import com.ps.personne.model.*
import com.ps.personne.rest.dto.DonneesKycDto
import com.ps.personne.rest.dto.StatutPPEDto

fun DonneesKyc.toDto(): DonneesKycDto = DonneesKycDto(
    idPersonne = this.idPersonne.id,
    updatedBy = this.update.user.login,
    updatedAt = this.update.date.toString(),
    statut = this.statutPPE.toDto()
)

private fun StatutPPE.toDto(): StatutPPEDto = when (this) {
    is StatutPPE.STANDARD -> StatutPPEDto(
        type = "STANDARD",
        vigilanceRenforcee = vigilance.vigilanceRenforcee,
        motif = (vigilance as? AvecVigilanceRenforcee)?.motif?.name
    )
    is StatutPPE.StatutPPE -> StatutPPEDto(
        type = "PPE",
        vigilanceRenforcee = vigilance.vigilanceRenforcee,
        motif = vigilance.motif.name,
        fonction = fonction.name,
        dateFin = dateFin?.toString()
    )
    is StatutPPE.PROCHE_PPE -> StatutPPEDto(
        type = "PROCHE_PPE",
        vigilanceRenforcee = vigilance.vigilanceRenforcee,
        motif = (vigilance as? AvecVigilanceRenforcee)?.motif?.name,
        lienParente = lienParente.name,
    )
}
