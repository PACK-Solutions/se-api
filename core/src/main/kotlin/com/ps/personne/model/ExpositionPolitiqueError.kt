package com.ps.personne.model

sealed interface ExpositionPolitiqueError {

    val message: String

    data class EntreeHistoriqueIdentiqueCourante(
        override val message: String,
    ) : ExpositionPolitiqueError

    // A terme déplacer cette erreur et sa vérification dans le domaine Personne
    data class PersonneNonTrouvee(
        override val message: String,
        val idPersonne: IdPersonne,
    ) : ExpositionPolitiqueError

    data class ErreurTechnique(
        override val message: String,
        val cause: Throwable? = null,
    ) : ExpositionPolitiqueError

}
