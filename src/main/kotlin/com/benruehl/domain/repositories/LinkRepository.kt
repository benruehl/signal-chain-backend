package com.benruehl.domain.repositories

import com.benruehl.domain.entities.Link

interface LinkRepository {
    suspend fun save(link: Link): Link
    suspend fun delete(linkId: String): Boolean
}