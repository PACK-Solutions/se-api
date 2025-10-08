package com.ps.personne.ports.driven

import com.ps.personne.model.DonneesKyc

class FakeDonneesKycRepository : DonneesKycRepository {
    override fun sauvegarder(donneesKyc: DonneesKyc): DonneesKyc {
        return donneesKyc
    }
}
