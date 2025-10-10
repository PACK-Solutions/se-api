package com.ps.personne.ports.driving

import com.ps.personne.model.DonneesKyc
import com.ps.personne.model.HistoriqueKyc
import com.ps.personne.model.IdPersonne

class FakeDonneesKycService : DonneesKycService {
    override fun sauverEtHistoriser(donneesKyc: DonneesKyc): Boolean {
        return true
    }

    override fun getHistorique(idPersonne: IdPersonne): HistoriqueKyc {
        TODO("Not yet implemented")
    }
}
