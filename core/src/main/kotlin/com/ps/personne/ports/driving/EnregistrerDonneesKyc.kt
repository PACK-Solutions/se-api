package com.ps.personne.ports.driving

import com.ps.personne.model.DonneesKyc

interface EnregistrerDonneesKyc {
    fun sauverEtHistoriser(donneesKyc: DonneesKyc): Boolean
}