package com.ps.personne.events

import com.ps.kommand.Context
import com.ps.kommand.event.EventHandler
import com.ps.personne.PersonneContextKey
import com.ps.personne.historique.*
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.Clock

val logger = KotlinLogging.logger {}

class StoreHistoriqueOnAuditableEvent(val repository: HistoriqueRepository, val idGenerator: EntreeHistoriqueIdGenerator, val clock: Clock) :
    EventHandler<AuditableEvent<*>> {

    override fun on(context: Context, event: AuditableEvent<*>) {

        val login = context[PersonneContextKey.Login]
        if (login == null) logger.warn { "No login found in context, this should never happen" }
        with(getHistoriqueBuilder(event)) {
            EntreeHistorique(
                id = idGenerator.next(),
                typeObjet = getTypeObjet(),
                idObjet = getIdObjet(),
                changements = getChangements(),
                performedBy = Author(login ?: "unknown"),
                occurredAt = clock.instant(),
            )
        }.also(repository::store)
    }

    private fun getHistoriqueBuilder(event: AuditableEvent<*>): HistoriqueProjection<*> =
        when (event) {
            is ConnaissanceClientModifiee -> ConnaissanceClientHistoriqueProjection(Old(event.old), New(event.new))
            else -> error(
                "Event ${event.javaClass.simpleName} is flagged as auditable but audit rules are not implemented",
            )
        }
}
