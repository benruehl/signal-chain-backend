package com.benruehl

import com.benruehl.application.dtos.CreateLinkRequest
import com.benruehl.application.dtos.SaveDeviceRequest
import com.benruehl.domain.entities.Device
import com.benruehl.domain.entities.Link

val aDevice = Device(
    123,
    "Any Title",
    100f,
    100f,
    incomingLinks = emptyList(),
    outgoingLink = null
)

val anotherDevice = Device(
    124,
    "Another Title",
    -10f,
    0f,
    incomingLinks = emptyList(),
    outgoingLink = null
)

fun aLink(sourceDevice: Device, targetDevice: Device) = Link(
    "${sourceDevice.id}_${targetDevice.id}",
    sourceDeviceId = sourceDevice.id!!,
    targetDeviceId = targetDevice.id!!
)

fun Device.toRequestBody() = SaveDeviceRequest(
    title = this.title,
    positionX = this.positionX,
    positionY = this.positionY
)

fun Link.toRequestBody() = CreateLinkRequest(
    sourceDeviceId = this.sourceDeviceId.toString(),
    targetDeviceId = this.targetDeviceId.toString()
)