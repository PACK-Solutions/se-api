package com.ps.personne.historique

import com.ps.personne.model.AvecVigilanceRenforcee
import com.ps.personne.model.ConnaissanceClient
import com.ps.personne.model.SansVigilanceRenforcee

class ConnaissanceClientHistoriqueProjection(val old: Old<ConnaissanceClient>, val new: New<ConnaissanceClient>) : HistoriqueProjection<ConnaissanceClient> {
    override fun getTypeObjet() = typeObjet

    override fun getIdObjet() = IdObjet(new.value.idPersonne.id.toString())

    override fun getChangements() = diff(old, new) {
        propertyDiff("FonctionPPE") { propertyValueGetter = { it.statutPPE?.mandat?.fonction?.name } }
        propertyDiff("DateFinFonctionPPE") { propertyValueGetter = { it.statutPPE?.mandat?.dateFin?.toString() } }
        propertyDiff("Vigilance") { propertyValueGetter = { it.vigilance.vigilanceRenforcee } }
        propertyDiff("MotifVigilance") {
            propertyValueGetter = {
                when (it.vigilance) {
                    is AvecVigilanceRenforcee -> (it.vigilance as AvecVigilanceRenforcee).motifs
                    is SansVigilanceRenforcee -> emptyList()
                }
            }
            considerDeletedWhen = { old, new -> old.isNotEmpty() && new.isEmpty() }
            considerCreatedWhen = { old, new -> old.isEmpty() && new.isNotEmpty() }
            considerModifiedWhen = { old, new -> old != new }
        }
        propertyDiff("LienParenteProchePPE") { propertyValueGetter = { it.statutProchePPE?.lienParente?.name } }
        propertyDiff("FonctionProchePPE") { propertyValueGetter = { it.statutProchePPE?.mandat?.fonction?.name } }
        propertyDiff(
            "DateFinFonctionProchePPE"
        ) { propertyValueGetter = { it.statutProchePPE?.mandat?.dateFin?.toString() } }
    }

    companion object {
        val typeObjet = TypeObjet("ConnaissanceClient")
    }
}
