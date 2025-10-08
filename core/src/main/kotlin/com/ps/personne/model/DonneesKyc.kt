package com.ps.personne.model

data class DonneesKyc(val idPersonne: IdPersonne)

@JvmInline
value class IdPersonne(val id: String) {
    init {
        require(id.isNotBlank()) { "Id personne ne peut pas être vide: $id" }
    }
}

