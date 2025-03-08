package com.ulpgc.uniMatch.data.domain.models

data class Survey(
    val title: String,
    val options: Map<String, Set<String>>
)
