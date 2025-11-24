package com.ps.personne.mapper

import com.ps.personne.model.*

fun ModificationConnaissanceClient.toSer(): ModificationConnaissanceClientSer = when (this) {
    is AjoutStatutPPE -> AjoutStatutPPESer
    is SuppressionStatutPPE -> SuppressionStatutPPESer
    is ModificationFonctionPPE -> ModificationFonctionPPESer
    is ModificationDateFinFonctionPPE -> ModificationDateFinFonctionPPESer
    is AjoutStatutProchePPE -> AjoutStatutProchePPESer
    is SuppressionStatutProchePPE -> SuppressionStatutProchePPESer
    is ModificationLienParente -> ModificationLienParenteSer
    is ModificationFonctionProchePPE -> ModificationFonctionProchePPESer
    is AjoutVigilance -> AjoutVigilanceSer
    is SuppressionVigilance -> SuppressionVigilanceSer
    is AjoutMotifVigilance -> AjoutMotifVigilanceSer(
        this.motifs.map { MotifVigilanceSer.valueOf(it.name) },
    )

    is SuppressionMotifVigilance -> SuppressionMotifVigilanceSer
    is ModificationMotifVigilance -> ModificationMotifVigilanceSer(
        this.motifs.map { MotifVigilanceSer.valueOf(it.name) },
    )
}

fun SyntheseModifications.toSer(): SyntheseModificationSer = SyntheseModificationSer(
    traceAudit = this.traceAudit.toSer(),
    modifications = this.modifications.map { it.toSer() }.toSet(),
)

fun ModificationConnaissanceClientSer.toDomain(): ModificationConnaissanceClient = when (this) {
    is AjoutStatutPPESer -> AjoutStatutPPE
    is SuppressionStatutPPESer -> SuppressionStatutPPE
    is ModificationFonctionPPESer -> ModificationFonctionPPE
    is ModificationDateFinFonctionPPESer -> ModificationDateFinFonctionPPE
    is AjoutStatutProchePPESer -> AjoutStatutProchePPE
    is SuppressionStatutProchePPESer -> SuppressionStatutProchePPE
    is ModificationLienParenteSer -> ModificationLienParente
    is ModificationFonctionProchePPESer -> ModificationFonctionProchePPE
    is AjoutVigilanceSer -> AjoutVigilance
    is SuppressionVigilanceSer -> SuppressionVigilance
    is AjoutMotifVigilanceSer -> AjoutMotifVigilance(
        this.motifs.map { MotifVigilance.valueOf(it.name) },
    )

    is SuppressionMotifVigilanceSer -> SuppressionMotifVigilance
    is ModificationMotifVigilanceSer -> ModificationMotifVigilance(
        this.motifs.map { MotifVigilance.valueOf(it.name) },
    )
}

fun SyntheseModificationSer.toDomain(): SyntheseModifications = SyntheseModifications(
    traceAudit = this.traceAudit.toDomain(),
    modifications = this.modifications.map { it.toDomain() }.toSet(),
)
