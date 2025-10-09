package com.ps.personne.services

import com.ps.personne.model.DonneesKyc
import com.ps.personne.ports.driven.DonneesKycRepository
import com.ps.personne.ports.driving.DonneesKycService

class DonneesKycServiceImpl(private val donneesKycRepository: DonneesKycRepository) : DonneesKycService {
    override fun sauverEtHistoriser(donneesKyc: DonneesKyc): Boolean {
        donneesKycRepository.sauvegarder(donneesKyc)
        return true
    }
}
