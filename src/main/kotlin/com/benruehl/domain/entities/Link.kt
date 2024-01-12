package com.benruehl.domain.entities

data class Link(
    val id: String?,
    val sourceDeviceId: Int,
    val targetDeviceId: Int
)