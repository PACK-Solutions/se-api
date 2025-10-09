package com.ps.personne

import com.ps.personne.fixtures.DonneesKycFixtures
import com.ps.personne.ports.driven.FakeDonneesKycRepository
import com.ps.personne.services.DonneesKycServiceImpl
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

class AppTest : ExpectSpec({
    context("Enregistrement des donnees KYC") {
        expect("Enregistrement avec succès") {
            val donneesKycService = DonneesKycServiceImpl(FakeDonneesKycRepository())
            donneesKycService.sauverEtHistoriser(DonneesKycFixtures.prochePpe()) shouldBe true
        }
    }
})
