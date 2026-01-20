package com.ps.personne.testharness.matchers

import com.ps.personne.historique.EntreeHistorique
import io.kotest.matchers.shouldBe
import java.time.Instant

infix fun EntreeHistorique.shouldHaveDate(date: Instant): EntreeHistorique {
    this.occurredAt shouldBe date
    return this
}
