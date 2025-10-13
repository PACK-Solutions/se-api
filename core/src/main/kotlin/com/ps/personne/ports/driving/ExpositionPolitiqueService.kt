package com.ps.personne.ports.driving

import com.ps.personne.model.ExpositionPolitique
import com.ps.personne.model.HistoriqueExpositionPolitique
import com.ps.personne.model.IdPersonne
import com.ps.personne.model.TraceAudit

interface ExpositionPolitiqueService {
    fun sauverEtHistoriser(idPersonne: IdPersonne, expositionPolitique: ExpositionPolitique, traceAudit: TraceAudit): Boolean
    fun getHistorique(idPersonne: IdPersonne): HistoriqueExpositionPolitique

    // Regle 1: Notion de mise à jour: vérification que l'exposition politique que l'on souhaite enregistrer n'est pas la même que la courante
    // Renvoyer une erreur si c'est le cas

    // Regle 2: Si on définit une nouvelle exposition politique sur le même mandat, il faut que le précédent soit cloturé (définir une date de fin)

    // Vérifier avec le métier si la date de fin est associé au mandat ou bien à l'exposition politique (à priori c'est le mandat)

    // Notion de lastUpdate (qui / quand)

    // Que se passe t'il si un gestionnaire positionne la mauvaise date de debut, qu'est ce qui fait foi concernant l'exposition politique courante
}
