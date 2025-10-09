package com.ps.personne.model

import java.time.Instant

data class UpdateInfo(
    val user: User,
    val date: Instant,
)

@JvmInline
value class User(val login: String)
