package com.ps.personne.config

import com.ps.personne.ports.driven.InMemoryConnaissanceClientRepository
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.repository.ExposedConnaissanceClientRepository
import com.ps.personne.services.ConnaissanceClientServiceImpl

fun configureConnaissanceClientService(sandbox: Boolean): ConnaissanceClientService {
    val repository = if (sandbox) {
        InMemoryConnaissanceClientRepository()
    } else {
        ExposedConnaissanceClientRepository()
    }

    return ConnaissanceClientServiceImpl(repository, repository)
}
