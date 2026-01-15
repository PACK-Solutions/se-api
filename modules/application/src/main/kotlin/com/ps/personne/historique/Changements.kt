package com.ps.personne.historique

fun <T> changements(old: Old<T>, new: New<T>, block: ChangementsBuilder<T>.() -> Unit): Set<Changement> = ChangementsBuilder(
    old,
    new,
).apply(block).build()

@JvmInline
value class Old<T>(val value: T)

@JvmInline
value class New<T>(val value: T)

class ChangementsBuilder<T>(val old: Old<T>, val new: New<T>) {
    inner class ChangementBuilder<P>(val nom: String) {
        lateinit var accessor: (obj: T) -> P

        var createdPredicate: (old: P, new: P) -> Boolean = { old: P, _: P -> old == null }
        var deletedPredicate: (old: P, new: P) -> Boolean = { _: P, new: P -> new == null }
        var notModifiedPredicate: (old: P, new: P) -> Boolean = { old: P, new: P -> old == new }

        fun build() {
            val oldProp = accessor(old.value)
            val newProp = accessor(new.value)
            if (notModifiedPredicate(oldProp, newProp)) return
            changements.add(
                when {
                    createdPredicate(oldProp, newProp) -> Changement.Creation(nom, newProp.toString())
                    deletedPredicate(oldProp, newProp) -> Changement.Suppression(nom, oldProp.toString())
                    else -> Changement.Modification(nom, newProp.toString(), oldProp.toString())
                },
            )
        }
    }

    private val changements = mutableSetOf<Changement>()

    fun <P> changement(nom: String, block: ChangementBuilder<P>.() -> Unit): Unit =
        ChangementBuilder<P>(nom).apply(block).build()

    fun build() = changements
}
