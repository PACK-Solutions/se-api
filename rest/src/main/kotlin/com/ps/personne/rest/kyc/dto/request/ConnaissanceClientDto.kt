package com.ps.personne.rest.kyc.dto.request

import com.ps.personne.model.AvecVigilanceRenforcee
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.LienParente
import com.ps.personne.model.Mandat
import com.ps.personne.model.MotifVigilance
import com.ps.personne.model.SansVigilanceRenforcee
import com.ps.personne.model.Vigilance
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ConnaissanceClientDto(
    val statutPPE: PpeDto? = null,
    val statutProchePPE: ProchePpeDto? = null,
    val vigilance: VigilanceDto
) {
    fun toDomain(idPersonne: IdPersonne): ConnaissanceClient {
        val ppe = statutPPE?.let { dto ->
            ExpositionPolitique.Ppe(
                mandat = Mandat(
                    fonction = FonctionPPE.valueOf(dto.mandat.fonction),
                    dateFin = dto.mandat.dateFin?.let { LocalDate.parse(it) }
                )
            )
        }

        val prochePpe = statutProchePPE?.let { dto ->
            ExpositionPolitique.ProchePpe(
                lienParente = LienParente.valueOf(dto.lienParente),
                mandat = Mandat(
                    fonction = FonctionPPE.valueOf(dto.mandat.fonction),
                    dateFin = dto.mandat.dateFin?.let { LocalDate.parse(it) }
                )
            )
        }

        val vigilanceDomain: Vigilance = if (vigilance.vigilanceRenforcee) {
            AvecVigilanceRenforcee(
                motifs = vigilance.motifs?.map { MotifVigilance.valueOf(it) } ?: emptyList()
            )
        } else {
            SansVigilanceRenforcee
        }

        return ConnaissanceClient(
            idPersonne = idPersonne,
            statutPPE = ppe,
            statutProchePPE = prochePpe,
            vigilance = vigilanceDomain
        )
    }
}

fun ConnaissanceClient.toDto(): ConnaissanceClientDto {
    val ppe = this.statutPPE?.let {
        PpeDto(
            mandat = MandatDto(
                fonction = it.mandat.fonction.name,
                dateFin = it.mandat.dateFin?.toString()
            )
        )
    }

    val prochePpe = this.statutProchePPE?.let {
        ProchePpeDto(
            lienParente = it.lienParente.name,
            mandat = MandatDto(
                fonction = it.mandat.fonction.name,
                dateFin = it.mandat.dateFin?.toString()
            )
        )
    }

    val vigilance = when (this.vigilance) {
        is SansVigilanceRenforcee -> VigilanceDto(false)
        is AvecVigilanceRenforcee -> VigilanceDto(
            true,
            (this.vigilance as AvecVigilanceRenforcee).motifs.map { it.name }
        )
    }

    return ConnaissanceClientDto(
        statutPPE = ppe,
        statutProchePPE = prochePpe,
        vigilance = vigilance
    )
}

@Serializable
data class MandatDto(
    val fonction: String,
    val dateFin: String? = null
)

@Serializable
data class PpeDto(
    val mandat: MandatDto
)

@Serializable
data class ProchePpeDto(
    val lienParente: String,
    val mandat: MandatDto
)

@Serializable
data class VigilanceDto(
    val vigilanceRenforcee: Boolean,
    val motifs: List<String>? = null
)
