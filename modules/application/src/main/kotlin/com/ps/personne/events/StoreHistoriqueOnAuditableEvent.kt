package com.ps.personne.events

import com.ps.kommand.Context
import com.ps.kommand.event.EventHandler
import com.ps.personne.PersonneContextKey
import com.ps.personne.historique.*
import java.time.Clock

class StoreHistoriqueOnAuditableEvent(val repository: HistoriqueRepository, val idGenerator: EntreeHistoriqueIdGenerator, val clock: Clock) :
    EventHandler<AuditableEvent<*>> {

    override fun on(context: Context, event: AuditableEvent<*>) {
        with(getHistoriqueBuilder(event)) {
            EntreeHistorique(
                id = idGenerator.next(),
                typeObjet = getTypeObjet(),
                idObjet = getIdObjet(),
                changements = getChangements(),
                performedBy = Author(context[PersonneContextKey.Login] ?: "unknown"),
                occurredAt = clock.instant(),
            )
        }.also(repository::store)
    }

    private fun getHistoriqueBuilder(event: AuditableEvent<*>): HistoriqueProjection<*> =
        when (event) {
            is ConnaissanceClientModifiee -> ConnaissanceClientHistoriqueProjection(Old(event.old), New(event.new))
            else -> error(
                "Event ${event.javaClass.simpleName} is flagged as auditable but audit rules are not implemented"
            )
        }
}
