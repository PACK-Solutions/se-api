package com.ps.personne.events

import com.ps.kommand.Context
import com.ps.kommand.event.EventHandler
import com.ps.personne.historique.*
import java.time.Instant
import java.util.*

class StoreHistoriqueOnAuditableEvent(val repository: HistoriqueRepository) :
    EventHandler<AuditableEvent> {

    override fun on(context: Context, event: AuditableEvent) {
        repository.store(
            EntreeHistorique(
                id = UUID.randomUUID(),
                typeObjet = event.getTypeObjet(),
                idObjet = event.getIdObjet(),
                changements = event.getChangements(),
                performedBy = "unknown", // TODO record user
                occurredAt = Instant.now(),
            ),
        )
    }
}
