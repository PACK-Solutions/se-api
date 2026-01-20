package com.ps.personne.historique

//TODO move in dedicated library

sealed class Diff {
    abstract val proprieteObjet: String

    data class Creation(override val proprieteObjet: String, val newValue: String) : Diff()
    data class Modification(override val proprieteObjet: String, val newValue: String, val oldValue: String) : Diff()
    data class Suppression(override val proprieteObjet: String, val oldValue: String) : Diff()
}


@JvmInline
value class Old<T>(val value: T)

@JvmInline
value class New<T>(val value: T)


fun <T> diff(old: Old<T>, new: New<T>, block: DiffBuilder<T>.() -> Unit): Set<Diff> = DiffBuilder(old, new).apply(block).build()

class DiffBuilder<T>(val old: Old<T>, val new: New<T>) {
    inner class PropertyDiffBuilder<P>(val nom: String) {
        /** lambda used to retrieve the actual propery value in both the old and new T instances **/
        lateinit var propertyValueGetter: (obj: T) -> P

        /** Predicated to consider the property as created in the new instance. Default returns true when old is null **/
        var considerCreatedWhen: (old: P, new: P) -> Boolean = { old: P, _: P -> old == null }

        /** Predicated to consider the property as deleted in the new instance. Default returns true when new is null  **/
        var considerDeletedWhen: (old: P, new: P) -> Boolean = { _: P, new: P -> new == null }

        /** Predicated to consider the property as modified in the new instance. Default uses `!=` to compare property in old and new instance  **/
        var considerModifiedWhen: (old: P, new: P) -> Boolean = { old: P, new: P -> old != new }

        fun build() {
            val oldProp = propertyValueGetter(old.value)
            val newProp = propertyValueGetter(new.value)
            if (considerModifiedWhen(oldProp, newProp)) {
                diffs.add(
                    when {
                        considerCreatedWhen(oldProp, newProp) -> Diff.Creation(nom, newProp.toString())
                        considerDeletedWhen(oldProp, newProp) -> Diff.Suppression(nom, oldProp.toString())
                        else -> Diff.Modification(nom, newProp.toString(), oldProp.toString())
                    },
                )
            }
        }
    }

    private val diffs = mutableSetOf<Diff>()

    fun <P> propertyDiff(label: String, block: PropertyDiffBuilder<P>.() -> Unit): Unit =
        PropertyDiffBuilder<P>(label).apply(block).build()

    fun build() = diffs
}
