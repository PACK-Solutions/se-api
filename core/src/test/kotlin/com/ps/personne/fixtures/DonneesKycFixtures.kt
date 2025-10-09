package com.ps.personne.fixtures

import com.ps.personne.model.*
import java.time.Instant
import java.time.LocalDate

object DonneesKycFixtures {
    fun default(
        id: Long = 123_456_789L,
        user: String = "tester",
        date: Instant = Instant.now(),
        statut: StatutPPE = StatutPPE.STANDARD(SansVigilanceRenforcee)
    ): DonneesKyc = DonneesKyc(
        idPersonne = IdPersonne(id),
        update = UpdateInfo(User(user), date),
        statutPPE = statut,
    )

    fun standard(
        id: Long = 1L,
        user: String = "std_user",
        date: Instant = Instant.now(),
        vigilanceRenforcee: Boolean = false
    ): DonneesKyc = default(
        id = id,
        user = user,
        date = date,
        statut = if (vigilanceRenforcee) {
            StatutPPE.STANDARD(
                AvecVigilanceRenforcee(MotifVigilance.SANS_JUSTIFICATION)
            )
        } else {
            StatutPPE.STANDARD(SansVigilanceRenforcee)
        }
    )

    fun ppe(
        id: Long = 2L,
        user: String = "ppe_user",
        date: Instant = Instant.now(),
        fonction: FonctionPPE = FonctionPPE.MEMBRE_GOUVERNEMENT,
        dateFin: LocalDate? = null,
        motif: MotifVigilance = MotifVigilance.MONTANT_ELEVE,
    ): DonneesKyc = default(
        id = id,
        user = user,
        date = date,
        statut = StatutPPE.PPE(
            fonction = fonction,
            dateFin = dateFin,
            vigilance = AvecVigilanceRenforcee(motif)
        )
    )

    fun prochePpe(
        id: Long = 3L,
        user: String = "proche_user",
        date: Instant = Instant.now(),
        lien: LienParente = LienParente.CONJOINT,
        ppeFonction: FonctionPPE = FonctionPPE.MEMBRE_PARLEMENT,
        ppeDateFin: LocalDate? = null,
        vigilanceRenforcee: Boolean = false
    ): DonneesKyc = default(
        id = id,
        user = user,
        date = date,
        statut = StatutPPE.PROCHE_PPE(
            lienParente = lien,
            ppe = StatutPPE.PPE(
                fonction = ppeFonction,
                dateFin = ppeDateFin,
                vigilance = AvecVigilanceRenforcee(MotifVigilance.SANS_JUSTIFICATION)
            ),
            vigilance = if (vigilanceRenforcee) {
                AvecVigilanceRenforcee(
                    MotifVigilance.DEMANDE_ASSUREUR
                )
            } else {
                SansVigilanceRenforcee
            }
        )
    )
}
