package com.ps.personne.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class MandatDto(
    val fonction: FonctionPpeDto,
    val dateFin: String?,
)

@Serializable
sealed interface ExpositionPolitiqueDto {
    @Serializable
    @SerialName("ppe")
    data class Ppe(val mandat: MandatDto) : ExpositionPolitiqueDto

    @Serializable
    @SerialName("proche_ppe")
    data class ProchePpe(
        val lienParente: LienParenteDto,
        val mandat: MandatDto,
    ) : ExpositionPolitiqueDto
}

@Serializable
enum class FonctionPpeDto {
    CHEF_ETAT,
    CHEF_GOUVERNEMENT,
    MEMBRE_GOUVERNEMENT,
    MEMBRE_PARLEMENT,
    DIRIGEANT_PARTI,
    MEMBRE_COUR_SUPREME,
    MEMBRE_COUR_COMPTES,
    DIRIGEANT_BANQUE_CENTRALE,
    AMBASSADEUR,
    OFFICIER_GENERAL,
    DIRIGEANT_ENTREPRISE_PUBLIQUE,
}

@Serializable
enum class LienParenteDto {
    CONJOINT,
    ENFANT,
    CONJOINT_ENFANT,
    PARENT,
}

@Serializable
data class AvecVigilanceRenforceeDto(val motifs: List<MotifVigilanceDto>)

@Serializable
enum class MotifVigilanceDto {
    AGE_AVANCE,
    DEMANDE_ASSUREUR,
    MONTANT_ELEVE,
    OPERATION_COMPLEXE,
    SANS_JUSTIFICATION,
}

private fun LocalDate?.toIsoString(): String? = this?.toString()

internal fun Mandat.toDto(): MandatDto = MandatDto(
    fonction = FonctionPpeDto.valueOf(this.fonction.name),
    dateFin = this.dateFin.toIsoString(),
)

fun ExpositionPolitique.Ppe.toDto(): ExpositionPolitiqueDto.Ppe = ExpositionPolitiqueDto.Ppe(
    mandat = this.mandat.toDto(),
)

fun ExpositionPolitique.ProchePpe.toDto(): ExpositionPolitiqueDto.ProchePpe = ExpositionPolitiqueDto.ProchePpe(
    lienParente = LienParenteDto.valueOf(this.lienParente.name),
    mandat = this.mandat.toDto(),
)

fun Vigilance.toDto(): AvecVigilanceRenforceeDto? = when (this) {
    is SansVigilanceRenforcee -> null
    is AvecVigilanceRenforcee -> AvecVigilanceRenforceeDto(this.motifs.map { MotifVigilanceDto.valueOf(it.name) })
}

fun MandatDto.toDomain(): Mandat = Mandat(
    fonction = FonctionPPE.valueOf(this.fonction.name),
    dateFin = this.dateFin?.let { LocalDate.parse(it) },
)

fun ExpositionPolitiqueDto.Ppe.toDomain(): ExpositionPolitique.Ppe = ExpositionPolitique.Ppe(
    mandat = this.mandat.toDomain(),
)

fun ExpositionPolitiqueDto.ProchePpe.toDomain(): ExpositionPolitique.ProchePpe = ExpositionPolitique.ProchePpe(
    lienParente = LienParente.valueOf(this.lienParente.name),
    mandat = this.mandat.toDomain(),
)

fun AvecVigilanceRenforceeDto?.toDomain(): Vigilance = when (this) {
    is AvecVigilanceRenforceeDto -> AvecVigilanceRenforcee(this.motifs.map { MotifVigilance.valueOf(it.name) })
    null -> SansVigilanceRenforcee
}
