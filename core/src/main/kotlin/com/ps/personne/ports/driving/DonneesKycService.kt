package com.ps.personne.ports.driving

import com.ps.personne.model.DonneesKyc
import com.ps.personne.model.HistoriqueKyc
import com.ps.personne.model.IdPersonne

interface DonneesKycService {
    fun sauverEtHistoriser(donneesKyc: DonneesKyc): Boolean
    fun getHistorique(idPersonne: IdPersonne): HistoriqueKyc
}
