package com.ps.personne.historique


fun <T> changements(old: T, new: T, block: ChangementsBuilder<T>.() -> Unit): Set<Changement> = ChangementsBuilder(old, new).apply(block).build()

class ChangementsBuilder<T>(val old: T, val new: T) {
    inner class ChangementBuilder<P>(val propriete: String) {
        lateinit var accessor: (obj: T) -> P

        var createdPredicate: (old: P, new: P) -> Boolean = { old: P, _: P -> old == null }
        var deletedPredicate: (old: P, new: P) -> Boolean = { _: P, new: P -> new == null }
        var notModifiedPredicate: (old: P, new: P) -> Boolean = { old: P, new: P -> old == new }

        fun build() {
            val oldProp = accessor(old)
            val newProp = accessor(new)
            if (notModifiedPredicate(oldProp, newProp)) return
            changements.add(
                when {
                    createdPredicate(oldProp, newProp) -> Changement.Creation(propriete, newProp.toString())
                    deletedPredicate(oldProp, newProp) -> Changement.Suppression(propriete, oldProp.toString())
                    else -> Changement.Modification(propriete, newProp.toString(), oldProp.toString())
                },
            )
        }
    }

    private val changements = mutableSetOf<Changement>()

    fun <P> changement(propriete: String, block: ChangementBuilder<P>.() -> Unit): Unit =
        ChangementBuilder<P>(propriete).apply(block).build()

    fun build() = changements
}


