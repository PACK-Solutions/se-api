package com.ps.personne.testharness.matchers

import com.ps.framework.components.history.HistoryEvent
import io.kotest.matchers.shouldBe
import java.time.Instant

infix fun HistoryEvent.shouldHaveDate(date: Instant): HistoryEvent {
    this.occurredAt shouldBe date
    return this
}
