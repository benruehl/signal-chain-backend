package com.benruehl.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SaveDeviceRequest(
    val title: String,
    val positionX: Float,
    val positionY: Float
)

@Serializable
data class QueryDeviceResponse(
    val id: Int,
    val title: String,
    val positionX: Float,
    val positionY: Float
)