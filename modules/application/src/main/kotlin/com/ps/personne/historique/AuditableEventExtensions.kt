package com.ps.personne.historique

import com.ps.personne.events.AuditableEvent
import com.ps.personne.events.ConnaissanceClientModifiee
import com.ps.personne.model.AvecVigilanceRenforcee
import com.ps.personne.model.SansVigilanceRenforcee

fun AuditableEvent.getTypeObjet(): String = when (this) {
    is ConnaissanceClientModifiee -> "ConnaissanceClient"
    else -> error("Event ${this.javaClass.simpleName} is flagged as auditable but audit rules are not implemented")
}

fun AuditableEvent.getIdObjet(): String = when (this) {
    is ConnaissanceClientModifiee -> this.new.idPersonne.id.toString()
    else -> error("Event ${this.javaClass.simpleName} is flagged as auditable but audit rules are not implemented")
}

fun AuditableEvent.getChangements(): Set<Changement> = when (this) {
    is ConnaissanceClientModifiee -> changements(old, new) {
        changement("fonction_ppe") { accessor = { it.statutPPE?.mandat?.fonction?.name } }
        changement("date_fin_fonction_ppe") { accessor = { it.statutPPE?.mandat?.dateFin?.toString() } }
        changement("vigilance") { accessor = { it.vigilance.vigilanceRenforcee } }
        changement("motifs_vigilance") {
            accessor = {
                when (it.vigilance) {
                    is AvecVigilanceRenforcee -> (it.vigilance as AvecVigilanceRenforcee).motifs
                    is SansVigilanceRenforcee -> emptyList()
                }
            }
            deletedPredicate = { old, new -> old.isNotEmpty() && new.isEmpty() }
            createdPredicate = { old, new -> old.isEmpty() && new.isNotEmpty() }
            notModifiedPredicate = { old, new -> old == new }
        }
        changement("parente_proche_ppe") { accessor = { it.statutProchePPE?.lienParente?.name } }
        changement("fonction_proche_ppe") { accessor = { it.statutProchePPE?.mandat?.fonction?.name } }
        changement("date_fin_fonction_proche_ppe") { accessor = { it.statutProchePPE?.mandat?.dateFin?.toString() } }
    }

    else -> error("Event ${this.javaClass.simpleName} is flagged as auditable but audit rules are not implemented")
}
