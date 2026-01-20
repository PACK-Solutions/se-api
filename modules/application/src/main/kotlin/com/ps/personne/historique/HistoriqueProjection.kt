package com.ps.personne.historique

interface HistoriqueProjection<T> {
    fun getTypeObjet(): TypeObjet
    fun getIdObjet(): IdObjet
    fun getChangements(): Set<Diff>
}
