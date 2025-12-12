package com.sultonuzdev.pft.domain.model

import com.sultonuzdev.pft.core.enums.TimerStyle

data class TimerOption(
    val style: TimerStyle,
    val icon: String,
    val title: String,
    val description: String,
    val features: List<String>,
)