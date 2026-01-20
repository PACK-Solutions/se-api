package com.ps.personne

import com.ps.personne.historique.EntreeHistoriqueIdGenerator
import java.util.*

class RandomUUIDGenerator : EntreeHistoriqueIdGenerator {
    override fun next(): UUID = UUID.randomUUID()
}
