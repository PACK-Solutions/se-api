package com.ps.personne.testharness

import com.ps.framework.components.id.IdGenerator
import java.util.*

class TestUUIDGenerator : IdGenerator {

    private val generatedIds = mutableListOf<UUID>()

    override fun next(): UUID = UUID.randomUUID().also(generatedIds::add)
    operator fun get(index: Int) = generatedIds[index]
}
