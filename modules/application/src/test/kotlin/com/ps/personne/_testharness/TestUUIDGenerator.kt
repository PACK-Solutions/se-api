package com.ps.personne._testharness

import com.ps.personne.historique.EntreeHistoriqueIdGenerator
import java.util.*

class TestUUIDGenerator : EntreeHistoriqueIdGenerator {

    private val generatedIds = mutableListOf<UUID>()

    override fun next(): UUID = UUID.randomUUID().also(generatedIds::add)
    operator fun get(index: Int) = generatedIds[index]
}
