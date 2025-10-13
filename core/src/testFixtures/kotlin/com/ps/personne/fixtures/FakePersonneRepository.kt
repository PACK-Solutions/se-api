package com.ps.personne.fixtures

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Personne
import com.ps.personne.model.TraceAudit
import com.ps.personne.ports.driven.PersonneRepository

class FakePersonneRepository : PersonneRepository {

    override fun sauvegarder(
        idPersonne: IdPersonne,
        expositionPolitique: ExpositionPolitique,
        traceAudit: TraceAudit,
    ): Personne {
        return PersonneFactory.creerPersonne(expositionPolitique)
    }

    override fun recupererExpositionPolitiqueCourante(idPersonne: IdPersonne): ExpositionPolitique? {
        return PersonneFactory.creerExpositionPpe()
    }

    override fun recupererHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique {
        return HistoriqueExpositionPolitique(
            idPersonne = idPersonne,
            expositionPolitiques = setOf(
                PersonneFactory.creerExpositionPpe(),
                PersonneFactory.creerExpositionProchePpe(),
            ),
        )
    }

}
