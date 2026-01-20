package com.ps.personne.fixtures.historique

import com.ps.personne.historique.EntreeHistorique
import com.ps.personne.historique.HistoriqueRepository
import com.ps.personne.historique.IdObjet
import com.ps.personne.historique.TypeObjet

class InMemoryHistoriqueRepository : HistoriqueRepository {
    private val data = mutableMapOf<Pair<TypeObjet, IdObjet>, Set<EntreeHistorique>>()

    override fun store(entry: EntreeHistorique) {
        data[Pair(entry.typeObjet, entry.idObjet)] = data[Pair(entry.typeObjet, entry.idObjet)]?.plus(entry) ?: setOf(entry)
    }

    override fun get(
        typeObjet: TypeObjet,
        idObjet: IdObjet,
    ): List<EntreeHistorique> = data[Pair(typeObjet, idObjet)]?.toList() ?: emptyList()


}
