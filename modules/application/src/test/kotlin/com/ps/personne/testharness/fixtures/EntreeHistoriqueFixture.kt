package com.ps.personne.testharness.fixtures

import com.ps.personne.historique.*
import java.time.Instant
import java.util.*

fun anEntreeHistorique(typeObjet: TypeObjet, idObjet: IdObjet) = EntreeHistoriqueFixture(typeObjet, idObjet)
fun aCreation(nom: String, value: String) = Diff.Creation(nom, value)
fun aModification(nom: String, oldValue: String, newValue: String) = Diff.Modification(nom, newValue, oldValue)
fun aSuppression(nom: String, oldValue: String) = Diff.Suppression(nom, oldValue)

class EntreeHistoriqueFixture(val typeObjet: TypeObjet, val idObjet: IdObjet) {
    private var id: UUID = UUID.randomUUID()
    private var date: Instant = Instant.now()
    private var userName = Author("unknown")
    private val changements = mutableSetOf<Diff>()

    fun withID(id: UUID) = this.apply { this.id = id }
    fun occuringAt(date: Instant) = this.apply { this.date = date }
    fun performedBy(userName: String) = this.apply { this.userName = Author(userName) }
    fun withChangement(changement: Diff) = this.apply { changements.add(changement) }
    fun withChangements(vararg changements: Diff) = this.apply { this.changements.addAll(changements) }

    fun build() = EntreeHistorique(
        id,
        typeObjet,
        idObjet,
        changements,
        performedBy = userName,
        occurredAt = date,
    )
}
