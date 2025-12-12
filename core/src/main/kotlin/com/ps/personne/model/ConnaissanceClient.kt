package com.ps.personne.model

import com.github.michaelbull.result.*
import com.ps.kommand.DomainEvent

@JvmInline
value class IdPersonne(val id: Long)

sealed interface ConnaissanceClientEvent : DomainEvent {
    data class ConnaissanceClientCreee(val idPersonne: IdPersonne) : ConnaissanceClientEvent
    data class ConnaissanceClientModifiee(val idPersonne: IdPersonne) : ConnaissanceClientEvent
}

data class ConnaissanceClient(
    val idPersonne: IdPersonne,
    val statutPPE: ExpositionPolitique.Ppe?,
    val statutProchePPE: ExpositionPolitique.ProchePpe?,
    val vigilance: Vigilance,
) {
    companion object {
        fun vierge(idPersonne: IdPersonne): ConnaissanceClient {
            return ConnaissanceClient(
                idPersonne = idPersonne,
                statutPPE = null,
                statutProchePPE = null,
                vigilance = SansVigilanceRenforcee,
            )
        }

        fun creer(
            idPersonne: IdPersonne,
            statutPPE: ExpositionPolitique.Ppe?,
            statutProchePPE: ExpositionPolitique.ProchePpe?,
            vigilance: Vigilance,
        ): Result<Pair<ConnaissanceClient, ConnaissanceClientEvent>, ConnaissanceClientError> =
            parse(idPersonne, statutPPE, statutProchePPE, vigilance)
                .map { it to ConnaissanceClientEvent.ConnaissanceClientModifiee(idPersonne) }

        // parse, don't validate (https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/)
        private fun parse(
            idPersonne: IdPersonne,
            statutPPE: ExpositionPolitique.Ppe?,
            statutProchePPE: ExpositionPolitique.ProchePpe?,
            vigilance: Vigilance,
        ) = when {
            (statutPPE != null || statutProchePPE != null) && vigilance is SansVigilanceRenforcee -> Err(
                ConnaissanceClientError.VigilanceRenforceeObligatoire,
            )

            else -> Ok(ConnaissanceClient(idPersonne, statutPPE, statutProchePPE, vigilance))
        }
    }

    fun mettreAJour(
        statutPPE: ExpositionPolitique.Ppe?,
        statutProchePPE: ExpositionPolitique.ProchePpe?,
        vigilance: Vigilance,
    ): Result<Pair<ConnaissanceClient, ConnaissanceClientEvent>, ConnaissanceClientError> =
        parse(idPersonne, statutPPE, statutProchePPE, vigilance)
            .map { it to ConnaissanceClientEvent.ConnaissanceClientModifiee(idPersonne) }

    fun appliquerModifications(connaissanceClient: ConnaissanceClient, traceAudit: TraceAudit): Result<ConnaissanceClient, ConnaissanceClientError> {
        return connaissanceClient.isValide().andThen(::calculerModifications).map { modifications ->
            stockerModifications(
                connaissanceClient,
                modifications,
                traceAudit,
            )
        }
    }

    private fun isValide() = when {
        (statutPPE != null || statutProchePPE != null) && vigilance is SansVigilanceRenforcee -> Err(
            ConnaissanceClientError.VigilanceRenforceeObligatoire,
        )

        else -> Ok(this)
    }

    private fun comparerStatutPPE(old: ExpositionPolitique.Ppe?, new: ExpositionPolitique.Ppe?): List<ModificationConnaissanceClient> {
        val modificationsStatutPPE = mutableListOf<ModificationConnaissanceClient>()

        if (old == null && new != null) {
            modificationsStatutPPE.add(AjoutStatutPPE)
        } else if (old != null && new == null) {
            modificationsStatutPPE.add(SuppressionStatutPPE)
        } else if (old != null && new != null) {
            if (old.mandat.fonction != new.mandat.fonction) {
                modificationsStatutPPE.add(ModificationFonctionPPE)
            }

            if (old.mandat.dateFin != new.mandat.dateFin) {
                modificationsStatutPPE.add(ModificationDateFinFonctionPPE)
            }
        }

        return modificationsStatutPPE
    }

    private fun comparerStatutProchePPE(old: ExpositionPolitique.ProchePpe?, new: ExpositionPolitique.ProchePpe?): List<ModificationConnaissanceClient> {
        val modificationsStatutProchePPE = mutableListOf<ModificationConnaissanceClient>()

        if (old == null && new != null) {
            modificationsStatutProchePPE.add(AjoutStatutProchePPE)
        } else if (old != null && new == null) {
            modificationsStatutProchePPE.add(SuppressionStatutProchePPE)
        } else if (old != null && new != null) {
            if (old.lienParente != new.lienParente) {
                modificationsStatutProchePPE.add(ModificationLienParente)
            }

            if (old.mandat.fonction != new.mandat.fonction) {
                modificationsStatutProchePPE.add(ModificationFonctionProchePPE)
            }

            if (old.mandat.dateFin != new.mandat.dateFin) {
                modificationsStatutProchePPE.add(ModificationDateFinFonctionPPE)
            }
        }

        return modificationsStatutProchePPE
    }

    private fun comparerVigilance(old: Vigilance, new: Vigilance): List<ModificationConnaissanceClient> {
        val modificationsVigilance = mutableListOf<ModificationConnaissanceClient>()
        if (old is SansVigilanceRenforcee && new is AvecVigilanceRenforcee) {
            modificationsVigilance.add(AjoutVigilance)
        } else if (old is AvecVigilanceRenforcee && new is SansVigilanceRenforcee) {
            modificationsVigilance.add(SuppressionVigilance)
        } else if (old is AvecVigilanceRenforcee && new is AvecVigilanceRenforcee) {
            if (old.motifs.isEmpty() && new.motifs.isNotEmpty()) {
                modificationsVigilance.add(AjoutMotifVigilance(new.motifs))
            } else if (old.motifs.isNotEmpty() && new.motifs.isEmpty()) {
                modificationsVigilance.add(SuppressionMotifVigilance)
            } else if (old.motifs != new.motifs) {
                modificationsVigilance.add(
                    ModificationMotifVigilance(new.motifs),
                )
            }
        }

        return modificationsVigilance
    }

    private fun calculerModifications(new: ConnaissanceClient): Result<Set<ModificationConnaissanceClient>, ConnaissanceClientError> {
        val modifications = setOf<ModificationConnaissanceClient>().plus(
            comparerStatutPPE(statutPPE, new.statutPPE),
        ).plus(
            comparerStatutProchePPE(statutProchePPE, new.statutProchePPE),
        ).plus(comparerVigilance(vigilance, new.vigilance))

        return if (modifications.isEmpty()) {
            Err(
                ConnaissanceClientError.AucuneModification(
                    "La connaissance client est identique à l'actuelle",
                ),
            )
        } else {
            Ok(modifications)
        }
    }

    private fun stockerModifications(
        connaissanceClient: ConnaissanceClient,
        modifications: Set<ModificationConnaissanceClient>,
        traceAudit: TraceAudit,
    ): ConnaissanceClient {
        return connaissanceClient
        // TODO refactor
//        return connaissanceClient.copy(
//            modification = SyntheseModifications(
//                traceAudit = traceAudit,
//                modifications = modifications,
//            ),
//        )
    }
}
