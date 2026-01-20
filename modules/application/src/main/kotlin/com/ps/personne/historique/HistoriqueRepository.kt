package com.ps.personne.historique

interface HistoriqueRepository {
    fun store(entry: EntreeHistorique)
    fun get(typeObjet: TypeObjet, idObjet: IdObjet): List<EntreeHistorique>
}
