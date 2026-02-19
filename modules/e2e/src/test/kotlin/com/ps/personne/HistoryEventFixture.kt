package com.ps.personne

import com.ps.framework.components.diff.Diff
import com.ps.framework.components.history.Author
import com.ps.framework.components.history.HistoryEvent
import com.ps.framework.components.history.IdObjet
import com.ps.framework.components.history.TypeObjet
import com.ps.personne.rest.dto.response.ChangementDto
import com.ps.personne.rest.dto.response.EntreeHistoriqueDto
import java.time.Instant
import java.util.*

fun anHistoryEvent(typeObjet: TypeObjet, idObjet: IdObjet) = HistoryEventFixture(typeObjet, idObjet)
fun aCreation(nom: String, value: String) = Diff.Create(nom, value)
fun anUpdate(nom: String, oldValue: String, newValue: String) = Diff.Update(nom, newValue, oldValue)
fun aDeletion(nom: String, oldValue: String) = Diff.Delete(nom, oldValue)

class HistoryEventFixture(val typeObjet: TypeObjet, val idObjet: IdObjet) {
    private var id: UUID = UUID.randomUUID()
    private var date: Instant = Instant.now()
    private var userName = Author("unknown")
    private val changements = mutableSetOf<Diff>()

    fun withID(id: UUID) = this.apply { this.id = id }
    fun occuringAt(date: Instant) = this.apply { this.date = date }
    fun performedBy(userName: String) = this.apply { this.userName = Author(userName) }
    fun withChangement(changement: Diff) = this.apply { changements.add(changement) }
    fun withDiffs(vararg diffs: Diff) = this.apply { this.changements.addAll(diffs) }

    fun build() = HistoryEvent(
        id,
        typeObjet,
        idObjet,
        changements,
        performedBy = userName,
        occurredAt = date,
    )

    fun toDto() = EntreeHistoriqueDto(
        idObjet = idObjet.value,
        changements = changements.map { ChangementDto.from(it) }.toSet(),
        performedBy = userName.value,
        occurredAt = date.toString(),
    )
}
