package com.ps.personne.events

import com.ps.framework.components.diff.New
import com.ps.framework.components.diff.Old
import com.ps.framework.components.history.Author
import com.ps.framework.components.history.HistoryEvent
import com.ps.framework.components.history.HistoryEventRepository
import com.ps.framework.components.history.HistoryProjection
import com.ps.framework.components.id.IdGenerator
import com.ps.framework.cqrs.bus.Context
import com.ps.framework.cqrs.bus.event.AuditableEvent
import com.ps.framework.cqrs.bus.event.EventHandler
import com.ps.personne.PersonneContextKey
import com.ps.personne.historique.ConnaissanceClientHistoryProjection
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.Clock

private val logger = KotlinLogging.logger {}

class StoreHistoriqueOnAuditableEvent(
    val repository: HistoryEventRepository,
    val idGenerator: IdGenerator,
    val clock: Clock,
) : EventHandler<AuditableEvent<*>> {

    override fun on(context: Context, event: AuditableEvent<*>) {
        val login = context[PersonneContextKey.Login]
        if (login == null) logger.warn { "No login found in context, this should never happen" }
        with(getHistoriqueBuilder(event)) {
            HistoryEvent(
                id = idGenerator.next(),
                typeObjet = getTypeObjet(),
                idObjet = getIdObjet(),
                diffs = getChangements(),
                performedBy = Author(login ?: "unknown"),
                occurredAt = clock.instant(),
            )
        }.also(repository::store)
    }

    private fun getHistoriqueBuilder(event: AuditableEvent<*>): HistoryProjection<*> = when (event) {
        is ConnaissanceClientModifiee -> ConnaissanceClientHistoryProjection(Old(event.old), New(event.new))

        else -> error(
            "Event ${event.javaClass.simpleName} is flagged as auditable but audit rules are not implemented",
        )
    }
}
