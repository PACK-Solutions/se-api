package com.ps.personne.events

import com.ps.kommand.event.DomainEvent

interface AuditableEvent<T> : DomainEvent {
    val old: T
    val new: T
}
