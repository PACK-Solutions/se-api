package com.ps.personne.historique

import com.ps.framework.components.history.HistoryEvent
import com.ps.framework.components.history.HistoryEventRepository
import com.ps.framework.components.history.IdObjet
import com.ps.framework.components.history.TypeObjet

class InMemoryHistoryEventRepository : HistoryEventRepository {
    private val data = mutableMapOf<Pair<TypeObjet, IdObjet>, Set<HistoryEvent>>()

    override fun store(entry: HistoryEvent) {
        data[
            Pair(
                entry.typeObjet,
                entry.idObjet,
            ),
        ] = data[Pair(entry.typeObjet, entry.idObjet)]?.plus(entry) ?: setOf(entry)
    }

    override fun get(typeObjet: TypeObjet, idObjet: IdObjet): List<HistoryEvent> = data[Pair(typeObjet, idObjet)]?.toList() ?: emptyList()
}
