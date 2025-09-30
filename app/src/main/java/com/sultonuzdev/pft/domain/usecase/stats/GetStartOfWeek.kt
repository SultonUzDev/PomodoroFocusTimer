package com.sultonuzdev.pft.domain.usecase.stats

import java.time.LocalDate

class GetStartOfWeek {
    operator fun invoke(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek
        val daysUntilMonday = dayOfWeek.value % 7
        return date.minusDays(daysUntilMonday.toLong())
    }

}