package com.ps.personne.database.mapper

import com.ps.personne.database.model.AjoutMotifVigilanceSer
import com.ps.personne.database.model.AjoutStatutPPESer
import com.ps.personne.database.model.AjoutStatutProchePPESer
import com.ps.personne.database.model.AjoutVigilanceSer
import com.ps.personne.database.model.ModificationConnaissanceClientSer
import com.ps.personne.database.model.ModificationDateFinFonctionPPESer
import com.ps.personne.database.model.ModificationFonctionPPESer
import com.ps.personne.database.model.ModificationFonctionProchePPESer
import com.ps.personne.database.model.ModificationLienParenteSer
import com.ps.personne.database.model.ModificationMotifVigilanceSer
import com.ps.personne.database.model.MotifVigilanceSer
import com.ps.personne.database.model.SuppressionMotifVigilanceSer
import com.ps.personne.database.model.SuppressionStatutPPESer
import com.ps.personne.database.model.SuppressionStatutProchePPESer
import com.ps.personne.database.model.SuppressionVigilanceSer
import com.ps.personne.database.model.SyntheseModificationSer
import com.ps.personne.model.AjoutMotifVigilance
import com.ps.personne.model.AjoutStatutPPE
import com.ps.personne.model.AjoutStatutProchePPE
import com.ps.personne.model.AjoutVigilance
import com.ps.personne.model.ModificationConnaissanceClient
import com.ps.personne.model.ModificationDateFinFonctionPPE
import com.ps.personne.model.ModificationFonctionPPE
import com.ps.personne.model.ModificationFonctionProchePPE
import com.ps.personne.model.ModificationLienParente
import com.ps.personne.model.ModificationMotifVigilance
import com.ps.personne.model.MotifVigilance
import com.ps.personne.model.SuppressionMotifVigilance
import com.ps.personne.model.SuppressionStatutPPE
import com.ps.personne.model.SuppressionStatutProchePPE
import com.ps.personne.model.SuppressionVigilance
import com.ps.personne.model.SyntheseModifications

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
        this.motifs.map { MotifVigilanceSer.valueOf(it.name) }
    )
    is SuppressionMotifVigilance -> SuppressionMotifVigilanceSer
    is ModificationMotifVigilance -> ModificationMotifVigilanceSer(
        this.motifs.map { MotifVigilanceSer.valueOf(it.name) }
    )
}

fun SyntheseModifications.toSer(): SyntheseModificationSer = SyntheseModificationSer(
    traceAudit = this.traceAudit.toSer(),
    modifications = this.modifications.map { it.toSer() }.toSet()
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
        this.motifs.map { MotifVigilance.valueOf(it.name) }
    )
    is SuppressionMotifVigilanceSer -> SuppressionMotifVigilance
    is ModificationMotifVigilanceSer -> ModificationMotifVigilance(
        this.motifs.map { MotifVigilance.valueOf(it.name) }
    )
}

fun SyntheseModificationSer.toDomain(): SyntheseModifications = SyntheseModifications(
    traceAudit = this.traceAudit.toDomain(),
    modifications = this.modifications.map { it.toDomain() }.toSet()
)
