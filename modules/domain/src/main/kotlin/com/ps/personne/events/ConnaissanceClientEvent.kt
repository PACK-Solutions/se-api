package com.ps.personne.events

import com.ps.kommand.event.DomainEvent
import com.ps.personne.model.ConnaissanceClient

sealed interface ConnaissanceClientEvent : DomainEvent

data class ConnaissanceClientModifiee(val old: ConnaissanceClient, val new: ConnaissanceClient) :
    ConnaissanceClientEvent,
    AuditableEvent
