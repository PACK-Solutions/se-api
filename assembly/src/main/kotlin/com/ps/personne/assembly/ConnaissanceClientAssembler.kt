package com.ps.personne.assembly

import com.ps.personne.database.repository.ExposedConnaissanceClientRepository
import com.ps.personne.ports.driving.ConnaissanceClientService
import com.ps.personne.services.ConnaissanceClientServiceImpl

fun configureConnaissanceClientService(): ConnaissanceClientService {
    val connaissanceClientRepository = ExposedConnaissanceClientRepository()
    return ConnaissanceClientServiceImpl(connaissanceClientRepository, connaissanceClientRepository)
}
