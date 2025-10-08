package com.ps.personne.ports.driving

import com.ps.personne.model.DonneesKyc

class FakeEnregistrerDonneesKyc: EnregistrerDonneesKyc {
    override fun sauverEtHistoriser(donneesKyc: DonneesKyc): Boolean {
        return true
    }
}