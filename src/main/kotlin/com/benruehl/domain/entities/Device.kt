package com.benruehl.domain.entities

data class Device(
    val id: Int?,
    val title: String,
    val positionX: Float,
    val positionY: Float,
    val incomingLinks: List<Link> = emptyList(),
    val outgoingLink: Link?
)
