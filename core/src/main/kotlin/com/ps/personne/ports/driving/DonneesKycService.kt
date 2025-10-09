package com.ps.personne.ports.driving

import com.ps.personne.model.DonneesKyc

interface DonneesKycService {
    fun sauverEtHistoriser(donneesKyc: DonneesKyc): Boolean
}
