package com.ps.personne

import com.ps.personne.model.DonneesKyc
import com.ps.personne.model.IdPersonne
import com.ps.personne.ports.driven.FakeDonneesKycRepository
import com.ps.personne.services.DonneesKycServiceImpl
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe

class AppTest : ExpectSpec({
    context("Enregistrement des donnees KYC") {
        expect("Enregistrement avec succès") {
            val donneesKycService = DonneesKycServiceImpl(FakeDonneesKycRepository())
            donneesKycService.sauverEtHistoriser(DonneesKyc(IdPersonne("123456789"))) shouldBe true
        }
    }
})
