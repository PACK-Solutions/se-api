package com.ps.personne.historique

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
