package com.ps.personne.rest.persistence

import com.ps.personne.model.FonctionPPE
import com.ps.personne.model.LienParente
import com.ps.personne.model.MotifVigilance
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

/**
 * Person table definition
 */
@Suppress("MagicNumber")
object PersonTable : Table("personne") {
    // Check strategy for ids
    val id = uuid("id").uniqueIndex()
    val id_oracle = integer("id_oracle").uniqueIndex()
}

/**
 * ExpositionPolitique table definition
 */
@Suppress("MagicNumber")
object ExpositionPolitiqueTable : Table("exposition_politique") {
    // Identifiants
    val id = uuid("id").uniqueIndex()
    val personId = reference("id_personne", PersonTable.id)

    // Données communes
    val dateDebut = date("date_debut")

    // Vigilance
    val vigilanceRenforcee = bool("vigilance_renforcee")
    val motifVigilance = enumerationByName<MotifVigilance>("motif_vigilance", 50).nullable()

    // Typage de l'exposition politique (PPE, PROCHE_PPE, STANDARD)
    val type = varchar("type", length = 20)

    // Mandat (présent pour PPE et ProchePPE)
    val mandatFonction = enumerationByName<FonctionPPE>("mandat_fonction", 50).nullable()
    val mandatDateFin = date("mandat_date_fin").nullable()

    // Lien de parenté (uniquement pour ProchePPE)
    val lienParente = enumerationByName<LienParente>("lien_parente", 50).nullable()

    override val primaryKey = PrimaryKey(id)
}
