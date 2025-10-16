package com.ps.personne.ports.driven

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.Personne
import com.ps.personne.model.TraceAudit

class InMemoryPersonneRepository : PersonneRepository {

    private val personnes = mutableMapOf<IdPersonne, Personne>()

    override fun sauvegarder(idPersonne: IdPersonne, expositionPolitique: ExpositionPolitique, traceAudit: TraceAudit): Personne {
        personnes.putIfAbsent(idPersonne, Personne(idPersonne, expositionPolitique))
        return personnes.getValue(idPersonne)
    }

    override fun recupererExpositionPolitiqueCourante(idPersonne: IdPersonne): ExpositionPolitique? {
        return personnes[idPersonne]?.expositionPolitique
    }

    /* override fun recupererHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique {
         return personnes.getValue(idPersonne).expositionPolitique
     }*/
}
