package com.ps.personne.fixtures

import com.ps.personne.model.IdPersonne
import kotlin.random.Random

fun anIdPersonne(id: Long = Random.nextLong()) = IdPersonne(id)
