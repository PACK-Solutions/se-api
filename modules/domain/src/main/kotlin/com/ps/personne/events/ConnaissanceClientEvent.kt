package com.ps.personne.events

import com.ps.kommand.event.DomainEvent
import com.ps.personne.model.ConnaissanceClient

sealed interface ConnaissanceClientEvent : DomainEvent

data class ConnaissanceClientModifiee(override val old: ConnaissanceClient, override val new: ConnaissanceClient) :
    ConnaissanceClientEvent,
    AuditableEvent<ConnaissanceClient>
