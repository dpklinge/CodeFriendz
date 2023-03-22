package com.codefriendz.repositories

import com.codefriendz.models.project.Project
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.R2dbcRepository
import java.util.*

interface ProjectRepository : R2dbcRepository<Project, String> {
    suspend fun findAllByOwner(owner: UUID): Flow<Project>
}