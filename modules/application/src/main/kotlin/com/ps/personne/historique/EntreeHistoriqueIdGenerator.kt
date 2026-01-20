package com.ps.personne.historique

import java.util.*


fun interface EntreeHistoriqueIdGenerator {
    fun next(): UUID
}
