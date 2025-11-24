package com.ps.personne.config

import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.repository.ExposedConnaissanceClientRepository
import com.ps.personne.services.ConnaissanceClientServiceImpl

fun configureConnaissanceClientService(): ConnaissanceClientService {
    val connaissanceClientRepository = ExposedConnaissanceClientRepository()
    return ConnaissanceClientServiceImpl(connaissanceClientRepository, connaissanceClientRepository)
}
