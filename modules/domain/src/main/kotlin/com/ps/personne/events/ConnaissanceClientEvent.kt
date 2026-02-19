package com.ps.personne.events

import com.ps.framework.cqrs.bus.event.AuditableEvent
import com.ps.framework.cqrs.bus.event.DomainEvent
import com.ps.personne.model.ConnaissanceClient

sealed interface ConnaissanceClientEvent : DomainEvent

data class ConnaissanceClientModifiee(override val old: ConnaissanceClient, override val new: ConnaissanceClient) :
    ConnaissanceClientEvent,
    AuditableEvent<ConnaissanceClient>
