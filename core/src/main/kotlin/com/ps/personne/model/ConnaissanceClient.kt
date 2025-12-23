package com.ps.personne.model

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.ps.personne.events.ConnaissanceClientEvent
import com.ps.personne.events.ConnaissanceClientModifiee

@JvmInline
value class IdPersonne(val id: Long)

//TODO rendre le constructeur privé pour garantir les invariants
data class ConnaissanceClient(
    val idPersonne: IdPersonne,
    val statutPPE: ExpositionPolitique.Ppe?,
    val statutProchePPE: ExpositionPolitique.ProchePpe?,
    val vigilance: Vigilance,
) {

    fun mettreAJour(
        statutPPE: ExpositionPolitique.Ppe?,
        statutProchePPE: ExpositionPolitique.ProchePpe?,
        vigilance: Vigilance,
    ): Result<Pair<ConnaissanceClient, ConnaissanceClientEvent>, ConnaissanceClientError> {
        if ((statutPPE != null || statutProchePPE != null) && vigilance is SansVigilanceRenforcee) {
            return Err(ConnaissanceClientError.VigilanceRenforceeObligatoire)
        }

        return ConnaissanceClient(idPersonne, statutPPE, statutProchePPE, vigilance).let {
            Ok(it to ConnaissanceClientModifiee(this, it))
        }

    }


    companion object {
        fun vierge(idPersonne: IdPersonne): ConnaissanceClient {
            return ConnaissanceClient(
                idPersonne = idPersonne,
                statutPPE = null,
                statutProchePPE = null,
                vigilance = SansVigilanceRenforcee,
            )
        }
    }
}
