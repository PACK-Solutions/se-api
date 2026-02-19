package com.ps.personne.testharness.fixtures

import com.ps.framework.components.diff.Diff
import com.ps.framework.components.history.Author
import com.ps.framework.components.history.HistoryEvent
import com.ps.framework.components.history.IdObjet
import com.ps.framework.components.history.TypeObjet
import java.time.Instant
import java.util.*

fun anHistoryEvent(typeObjet: TypeObjet, idObjet: IdObjet) = HistoryEventFixture(typeObjet, idObjet)
fun aCreation(nom: String, value: String) = Diff.Create(nom, value)
fun aModification(nom: String, oldValue: String, newValue: String) = Diff.Update(nom, newValue, oldValue)
fun aSuppression(nom: String, oldValue: String) = Diff.Delete(nom, oldValue)

class HistoryEventFixture(val typeObjet: TypeObjet, val idObjet: IdObjet) {
    private var id: UUID = UUID.randomUUID()
    private var date: Instant = Instant.now()
    private var userName = Author("unknown")
    private val changements = mutableSetOf<Diff>()

    fun withID(id: UUID) = this.apply { this.id = id }
    fun occuringAt(date: Instant) = this.apply { this.date = date }
    fun performedBy(userName: String) = this.apply { this.userName = Author(userName) }
    fun withChangement(changement: Diff) = this.apply { changements.add(changement) }
    fun withChangements(vararg changements: Diff) = this.apply { this.changements.addAll(changements) }

    fun build() = HistoryEvent(
        id,
        typeObjet,
        idObjet,
        changements,
        performedBy = userName,
        occurredAt = date,
    )
}
