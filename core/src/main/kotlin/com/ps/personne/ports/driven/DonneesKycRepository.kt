package com.ps.personne.ports.driven

import com.ps.personne.model.DonneesKyc

interface DonneesKycRepository {
    fun sauvegarder(donneesKyc: DonneesKyc): DonneesKyc
}
