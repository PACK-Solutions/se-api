package com.ps.personne.fixtures

import com.ps.personne.model.*
import java.time.Instant
import java.time.LocalDate

object DonneesKycFixtures {
    fun default(
        id: Long = 123_456_789L,
        user: String = "tester",
        date: Instant = Instant.now(),
        statutKyc: StatutKyc = StatutKyc.Standard(LocalDate.now(), SansVigilanceRenforcee),
    ): DonneesKyc = DonneesKyc(
        idPersonne = IdPersonne(id),
        update = UpdateInfo(User(user), date),
        statutKyc = statutKyc,
    )

    fun standard(
        id: Long = 1L,
        user: String = "std_user",
        date: Instant = Instant.now(),
        vigilanceRenforcee: Boolean = false,
    ): DonneesKyc = default(
        id = id,
        user = user,
        date = date,
        statutKyc = if (vigilanceRenforcee) {
            StatutKyc.Standard(
                LocalDate.now(),
                AvecVigilanceRenforcee(MotifVigilance.SANS_JUSTIFICATION),
            )
        } else {
            StatutKyc.Standard(LocalDate.now(), SansVigilanceRenforcee)
        },
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
        statutKyc = StatutKyc.Ppe(
            Mandat(
                fonction = fonction,
                dateFin = dateFin,
            ),
            LocalDate.now(),
            vigilance = AvecVigilanceRenforcee(motif),
        ),
    )

    fun prochePpe(
        id: Long = 3L,
        user: String = "proche_user",
        date: Instant = Instant.now(),
        lien: LienParente = LienParente.CONJOINT,
        ppeFonction: FonctionPPE = FonctionPPE.MEMBRE_PARLEMENT,
        ppeDateFin: LocalDate? = null,
        vigilanceRenforcee: Boolean = false,
    ): DonneesKyc = default(
        id = id,
        user = user,
        date = date,
        statutKyc = StatutKyc.ProchePpe(
            lienParente = lien,
            mandat = Mandat(
                fonction = ppeFonction,
                dateFin = ppeDateFin,
            ),
            LocalDate.now(),
            vigilance = if (vigilanceRenforcee) {
                AvecVigilanceRenforcee(
                    MotifVigilance.DEMANDE_ASSUREUR,
                )
            } else {
                SansVigilanceRenforcee
            },
        ),
    )
}
