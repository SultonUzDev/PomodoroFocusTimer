package com.sultonuzdev.pft.domain.usecase.stats

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class GetStartOfWeek {
    operator fun invoke(date: LocalDate): LocalDate {
        // Use TemporalAdjusters to get the previous or same Monday
        // This ensures Monday is always the first day of the week
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

}