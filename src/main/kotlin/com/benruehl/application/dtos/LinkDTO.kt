package com.benruehl.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class QueryLinkResponse(
    val id: String,
    val sourceDeviceId: String,
    val targetDeviceId: String
)

@Serializable
data class CreateLinkRequest(
    val sourceDeviceId: String,
    val targetDeviceId: String
)