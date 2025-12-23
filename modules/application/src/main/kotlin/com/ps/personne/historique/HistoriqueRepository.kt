package com.ps.personne.historique

interface HistoriqueRepository {
    fun store(entry: EntreeHistorique)
}
