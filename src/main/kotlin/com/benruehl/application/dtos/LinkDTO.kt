package com.benruehl.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class QueryLinkResponse(
    val id: String,
    val sourceDeviceId: Int,
    val targetDeviceId: Int
)

@Serializable
data class CreateLinkRequest(
    val sourceDeviceId: Int,
    val targetDeviceId: Int
)